/**
 * \file    NetworkThread.java
 * \authors Zachary Hayden
 * \date    May 17, 2020
 * \brief   *
 * \details *
 */

package FroggThread.Network;

//java SDK imports
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.io.*;
import java.net.*;

//project imports
import FroggThread.*;
import FroggThread.Network.*;
import FroggThread.IO.*;

/**
 * \class   NetworkThread
 * \brief   Thread that handles network traffic for frogg
 * \details *
 */
public class NetworkThread extends Thread{

    //define constants
    /**
     * @brief   Maximum number of connected sockets
     * @details When NetworkThread acts as a server, this is the maximum number
     *          of clients that are allowed to connect
     */
    public static final int MAX_CONNECTIONS = 8;
    private static final int TX_BUFFER = 128;
    private static final int RX_BUFFER = 128;

    //commumications with control thread
    private final BlockingQueue<NetworkPacketData> dataOut;
    private final PriorityBlockingQueue<NetworkPacketData> dataIn;
    private final BlockingQueue<ThreadCommandData> commIn;
    private final BlockingQueue<ThreadCommandData> commOut;
    private final byte threadId;

    //communications with rx and tx subthreads
    private AtomicBoolean rxStop;
    private AtomicBoolean rxSuspend;
    private AtomicBoolean rxStopped;
    private AtomicBoolean rxSuspended;
    private AtomicBoolean txStop;
    private AtomicBoolean txSuspend;
    private AtomicBoolean txStopped;
    private AtomicBoolean txSuspended;

    //socket objects (stream)
    private Socket clientSocket;
    private ByteArrayOutputStream clientOutStream;
    private ByteArrayInputStream clientInStream;
    private Socket[] connectedSockets;
    private AtomicBoolean[] validSockets;
    private ByteArrayOutputStream[] serverOutStream;
    private ByteArrayInputStream[] serverInStream;
    private ServerSocket serverSocket;

    //internet addressing objects
    private SocketAddress localAddr;
    private InetAddress remoteIP;
    private InetSocketAddress remoteAddr;


    //Socket options?

    //RX and TX threads
    private NetworkRxThread rxThread;
    private NetworkTxThread txThread;

    //connection status information
    private AtomicBoolean isServer;
    private NetworkStatus netStatus;
    private Vector<Double> latencyInfo;


    /**
     * @brief   Creates a NetworkThread object
     * @details 
     * @param   thread      ID of the thread, defined by the thread that creates it. (used for certain ThreadCommands)
     * @param   queueOut    Queue containing NetworkPacketData objects to transmit
     * @param   queueIn     Queue containing revieved NetworkPacketData objects
     * @param   commQueue   Queue of ThreadCommands from the creating/controlling thread
     * @param   replyQueue  Queue of replies to the creating/controlling thread
     * @note    replyQueue can be shared with other threads but NetworkThread does not support sharing of commQueue
     */
    public NetworkThread(byte thread, BlockingQueue<NetworkPacketData> queueOut, PriorityBlockingQueue<NetworkPacketData> queueIn, 
                         BlockingQueue<ThreadCommandData> commQueue, BlockingQueue<ThreadCommandData> replyQueue){

        //communications interfaces
        dataOut = queueOut;
        dataIn  = queueIn;
        commIn  = commQueue;
        commOut = replyQueue;
        threadId = thread;

        //create new flags for subthread interfaces
        rxStop = new AtomicBoolean(false);
        rxSuspend = new AtomicBoolean(true);
        rxStopped = new AtomicBoolean(false);
        rxSuspended = new AtomicBoolean(false);
        txStop = new AtomicBoolean(false);
        txSuspend = new AtomicBoolean(true);
        txStopped = new AtomicBoolean(false);
        txSuspended = new AtomicBoolean(false);

        //
        isServer = new AtomicBoolean(false);
        netStatus = NetworkStatus.NO_INTERFACE;

        //create new sockets
        clientSocket = new Socket();
        clientOutStream = new ByteArrayOutputStream(TX_BUFFER);
        clientInStream = new ByteArrayInputStream(new byte[RX_BUFFER]);
        connectedSockets = new Socket[MAX_CONNECTIONS];
        validSockets = new AtomicBoolean[MAX_CONNECTIONS];
        for(int i = 0; i < MAX_CONNECTIONS; i++){
            serverOutStream[i] = new ByteArrayOutputStream(TX_BUFFER);
            serverInStream[i] = new ByteArrayInputStream(new byte[RX_BUFFER]);
        }
        serverSocket = new ServerSocket();

        //create new Threads
        rxThread = new NetworkRxThread( dataIn, rxStop, rxSuspend, rxStopped, rxSuspended, isServer, clientSocket,
                                        clientInStream, validSockets, serverInStream);
        txThread = new NetworkTxThread( dataOut, txStop, txSuspend, txStopped, txSuspended, isServer, clientSocket,
                                        clientOutStream, validSockets, serverOutStream);

        
    }

    /**
     * \brief   Starts execution of NetworkThread
     * \details Starts execution of NetworkThread, which will then start its subthreads
     */
    public void run()
    {
        //variables
        ThreadCommandData command;

        //start threads (in suspended state)

        //check for commands on the command Queue (do this forever)
        while(true){
            command = commIn.poll();
            //execute commands
            if( command != null ){
                switch( command.command ){
                    case STOP:
                        comm_stop();
                        break;
                    case SUSPEND:
                        comm_suspend();
                        break;
                    case RESUME:
                        rxSuspend.set(false);
                        txSuspend.set(false);
                        break;
                    case SET_MY_PORT:
                        comm_set_port();
                        break;
                    case SET_REMOTE_PORT:
                        break;
                    case SET_REMOTE_IP:
                        break;
                    case SET_SERVER:
                        comm_set_server();
                        break;
                    case SET_CLIENT:
                        comm_set_client();
                        break;
                    case CONNECT:
                        comm_connect();
                        break;
                    case DISCONNECT:
                        comm_disconnect();
                        break;
                    case GET_NET_STATUS:
                        comm_status();
                        break;
                    default:
                        //put the command back in the queue because it isn't meant for me?
                }
            }
            //server: accept connections

            //monitor connections
        }
    }

    private void comm_stop(){
        //set signals
        rxSuspend.set(false);   //suspended threads can't detect stop signal
        txSuspend.set(false);
        rxStop.set(true);
        txStop.set(true);
        //wait for response
        while( !( txStopped.get() && rxStopped.get() ) ){}
        //clean up variables
    }

    private void comm_suspend(){
        //set signals
        rxSuspend.set(true);
        txSuspend.set(true);
        //wait for repsonse
        while( !( txSuspended.get() && rxSuspended.get() ) ){}
        //NOTE: this does not suspend this thread (otherwise it couldn't be restarted)
        //it suspends all of the TX/RX operations of this thread's subthreads
    }

    private void comm_set_port(){
        //check connection state
        switch(netStatus){
            //no interface to connect to
            case NO_INTERFACE:
                send_nack();
                return;
            //no errors; not connected (ok to change port)
            case INSUFF_ADDR:
            case NO_CONN:
                //
                send_ack();
                break;
            //connected (not okay to change port)
            case ACCEPTING:
            case CONNECTED:
                send_nack();
                return;
            //suspended (?)
            case SUSPENDED:
                break;
        }

    }

    private void comm_connect(){
        //check connection state
        switch(netStatus){
            //no interface to connect to
            case NO_INTERFACE:
                return;
            //no errors; not connected (ok to change port)
            case INSUFF_ADDR:
            case NO_CONN:
                break;
            //connected (not okay to change port)
            case ACCEPTING:
            case CONNECTED:
                return;
            //suspended (?)
            case SUSPENDED:
                break;
        }
    }

    private void comm_disconnect(){
        //check connection state
        switch(netStatus){
            //no interface to connect to
            case NO_INTERFACE:
                return;
            //no errors; not connected (ok to change port)
            case INSUFF_ADDR:
            case NO_CONN:
                break;
            //connected (not okay to change port)
            case ACCEPTING:
            case CONNECTED:
                return;
            //suspended (?)
            case SUSPENDED:
                break;
        }
    }

    private void comm_set_server(){
        //check connection state
        switch(netStatus){
            //no interface to connect to
            case NO_INTERFACE:
                return;
            //no errors; not connected (ok to change port)
            case INSUFF_ADDR:
            case NO_CONN:
                break;
            //connected (not okay to change port)
            case ACCEPTING:
            case CONNECTED:
                return;
            //suspended (?)
            case SUSPENDED:
                break;
        }
    }

    private void comm_set_client(){
        //check connection state
        switch(netStatus){
            //no interface to connect to
            case NO_INTERFACE:
                return;
            //no errors; not connected (ok to change port)
            case INSUFF_ADDR:
            case NO_CONN:
                break;
            //connected (not okay to change port)
            case ACCEPTING:
            case CONNECTED:
                return;
            //suspended (?)
            case SUSPENDED:
                break;
        }
    }

    private void comm_status(){
        //check connection state
        switch(netStatus){
            //no interface to connect to
            case NO_INTERFACE:
                return;
            //no errors; not connected (ok to change port)
            case INSUFF_ADDR:
            case NO_CONN:
                break;
            //connected (not okay to change port)
            case ACCEPTING:
            case CONNECTED:
                return;
            //suspended (?)
            case SUSPENDED:
                break;
        }
    }

    private void send_ack(){
        ThreadCommandData response = new ThreadCommandData(ThreadCommand.ACK, threadId);
        commOut.put(response);
    }

    private void send_nack(){
        ThreadCommandData response = new ThreadCommandData(ThreadCommand.NACK, threadId);
        commOut.put(response);
    }
}