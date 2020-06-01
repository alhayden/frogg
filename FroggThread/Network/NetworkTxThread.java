/**
 * \file    NetworkTxThread.java
 * \authors Zachary Hayden
 * \date    May 31, 2020
 * \brief   Thread for transmitting packets
 * \details This file defines a thread that handles sending packets over the network.
 *          This class should not be instantiated directly but is automatically created by
 *          NetworkThread when an object of that class is instantiated.
 */

package FroggThread.Network;

//java SDK imports
import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

//project imports
import FroggThread.Network.*;

/**
 * \class   NetworkTxThread
 * \brief   Thread that transmits packets
 * \note    Do not instantiate this class. NetworkThread will create and start NetworkTxThread automatically.
 */
public class NetworkTxThread extends Thread{
    //queue->network
    
    //internal communications
    private final BlockingQueue<NetworkPacketData> dataOut;
    
    //state control
    private AtomicBoolean txStop;
    private AtomicBoolean txSuspend;
    private AtomicBoolean txStopped;
    private AtomicBoolean txSuspended;
    private AtomicBoolean isServer;

    //external communications
    private Socket clientSocket;
    private ByteArrayOutputStream clientStream;
    // private Socket[] connectedSockets;
    private AtomicBoolean[] validSockets;
    private ByteArrayOutputStream[] serverStreams;
    
    /**
     * \brief   Creates a NetworkTxThread object
     * \note    Do not instantiate this class. NetworkThread will create and start NetworkTxThread automatically.
     * \param   dataQueue   Queue of NetworkPacketData objects to transmit
     * \param   stopIn      Flag to request that ths thread stops and terminates itself
     * \param   stopOut     Flag to acknowledge that this thread is stopping and terminating
     * \param   suspIn      Flag to request that this thread suspends its execution
     * \param   suspOut     Flag to acknowledge that this thread is suspending
     * \param   server      Flag that determines whether the thread should act as a client or server
     * \param   client      Client socket object
     * \param   cStream     Output stream of the client socket
     * \param   vSockets    Array of flags that indicate whether server sockets are properly connected
     * \param   sStream     Array of output streams for the server sockets
     */
    public NetworkTxThread( BlockingQueue<NetworkPacketData> dataQueue, AtomicBoolean stopIn, AtomicBoolean stopOut,
                            AtomicBoolean suspIn, AtomicBoolean suspOut, AtomicBoolean server, Socket client, 
                            ByteArrayOutputStream cStream, AtomicBoolean[] vSockets, ByteArrayOutputStream[] sStream)
    {
        //initialize static variables in order they are listed
        //communications
        dataOut = dataQueue;
        //state
        txStop = stopIn;
        txSuspend = suspIn;
        txStopped = stopOut;
        txSuspended = suspOut;
        isServer = server;
        //TCP socket
        clientSocket = client;
        clientStream = cStream;
        validSockets = vSockets;
        serverStreams = sStream;
        //There are currently no purely internal variables, so setup is complete
    }

    /**
     * \brief   Starts execution of the NetworkTxThread object
     * \note    Do not use this function. NetworkThread will create and start NetworkTxThread automatically.
     */
    public void run()
    {
        boolean cached_isServer;
        NetworkPacketData packetData;
        byte[] byteData;

        //loop until STOP command
        while( !txStop.get() )
        {
            //suspend thread
            while( txSuspend.get() )
            {
                txSuspended.set(true);
            }
            //change flag when exiting suspened state
            txSuspended.set(false);

            cached_isServer = isServer.get();

            //copy data from queue
            packetData = dataOut.poll();
            do{
                byteData = packetData.toBytes();
                if(cached_isServer){
                    //copy data to all connected sockets
                    for(int i = 0; i < NetworkThread.MAX_CONNECTIONS; i++){
                        if( validSockets[i].get() ){
                            try{
                                serverStreams[i].writeBytes(byteData);
                                serverStreams[i].flush();
                            }
                            catch(IOException e){
                                //call a standard network error function?
                            }
                        }
                    }
                }
                else{
                    //copy data to connected socket
                    if( clientSocket.isConnected() ){
                        try{
                            clientStream.writeBytes(byteData);
                            clientStream.flush();
                        }
                        catch(IOException e){
                                //call a standard network error function?
                        }
                    }
                }
                packetData = dataOut.poll();
            }while( packetData != null );

        }
        //signal thread is terminating
        txStopped.set(true);
        return;
    }
}