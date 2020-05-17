package FroggThread.Network;

//java SDK imports
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.net.*;

//project imports
import FroggThread.*;
import FroggThread.Network.*;
import FroggThread.IO.*;

public class NetworkThread extends Thread{

    //commumications with control thread
    private final PriorityBlockingQueue<NetworkPacketData> dataOut;
    private final PriorityBlockingQueue<NetworkPacketData> dataIn;
    private final BlockingQueue<ThreadCommandData> commIn;

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
    private Vector<Socket> connectedSockets;
    private ServerSocket serverSocket;

    //internet addressing objects
    private NetworkInterface net;
    private InetAddress localAddr;
    private InetAddress remoteAddr;

    //Socket options?

    //RX and TX threads
    private NetworkRxThread rxThread;
    private NetworkTxThread txThread;

    //connection status information
    private AtomicBoolean isServer;
    private NetworkStatus netStatus;
    private Vector<Double> latencyInfo;



    public NetworkThread(PriorityBlockingQueue<NetworkPacketData> queueOut, PriorityBlockingQueue<NetworkPacketData> queueIn, 
                         BlockingQueue<ThreadCommandData> commQueue){
        //communications interfaces
        dataOut = queueOut;
        dataIn  = queueIn;
        commIn  = commQueue;

        //create new flags for subthread interfaces
        rxStop = new AtomicBoolean(false);
        rxSuspend = new AtomicBoolean(true);
        rxStopped = new AtomicBoolean(false);
        rxSuspended = new AtomicBoolean(false);
        txStop = new AtomicBoolean(false);
        txSuspend = new AtomicBoolean(true);
        txStopped = new AtomicBoolean(false);
        txSuspended = new AtomicBoolean(false);

        //create new sockets
        clientSocket = new Socket();
        connectedSockets = new Vector<Socket>();
        serverSocket = new ServerSocket();

        //create new Threads
        rxThread = new NetworkRxThread();
        txThread = new NetworkTxThread();

        //find the network state
        //setup local address
        //start threads (in suspended state)
    }

    public void run(){
        //variables
        ThreadCommandData command;
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
                    case SET_REMOTE_IP:
                    case SET_SERVER:
                    case SET_CLIENT:
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
        }
    }

    private void comm_stop(){
        //set signals
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
    }

    private void comm_set_port(){

    }

    private void comm_connect(){

    }

    private void comm_disconnect(){

    }

    private void comm_status(){

    }
}