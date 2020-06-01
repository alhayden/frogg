/**
 * \file    NetworkRxThread.java
 * \authors Zachary Hayden
 * \date    May 31, 2020
 * \brief   Thread for receiving packets
 * \details This file defines a thread that handles receiving packets over the network.
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
 * \class   NetworkRxThread
 * \brief   Thread that receives packets
 * \note    Do not instantiate this class. NetworkThread will create and start NetworkTxThread automatically.
 */
public class NetworkRxThread extends Thread{
    //queue<-network

    //internal communications
    private final BlockingQueue<NetworkPacketData> dataIn;

    //state control
    private AtomicBoolean rxStop;
    private AtomicBoolean rxSuspend;
    private AtomicBoolean rxStopped;
    private AtomicBoolean rxSuspended;
    private AtomicBoolean isServer;
       
    //external communications
    private Socket clientSocket;
    private ByteArrayInputStream clientStream;
    // private Socket[] connectedSockets;
    private AtomicBoolean[] validSockets;
    private ByteArrayInputStream[] serverStreams;
    
    /**
     * \brief   Creates a NetworkRxThread object
     * \note    Do not instantiate this class. NetworkThread will create and start NetworkTxThread automatically.
     * \param   dataQueue   Queue of NetworkPacketData objects to transmit
     * \param   stopIn      Flag to request that ths thread stops and terminates itself
     * \param   stopOut     Flag to acknowledge that this thread is stopping and terminating
     * \param   suspIn      Flag to request that this thread suspends its execution
     * \param   suspOut     Flag to acknowledge that this thread is suspending
     * \param   server      Flag that determines whether the thread should act as a client or server
     * \param   client      Client socket object
     * \param   cStream     Input stream of the client socket
     * \param   vSockets    Array of flags that indicate whether server sockets are properly connected
     * \param   sStream     Array of input streams for the server sockets
     */
    public NetworkRxThread( BlockingQueue<NetworkPacketData> dataQueue, AtomicBoolean stopIn, AtomicBoolean stopOut,
    AtomicBoolean suspIn, AtomicBoolean suspOut, AtomicBoolean server, Socket client, 
    ByteArrayInputStream cStream, AtomicBoolean[] vSockets, ByteArrayInputStream[] sStream)
    {
        //initialize static variables in order they are listed
        //communications
        dataIn = dataQueue;
        //state
        rxStop = stopIn;
        rxSuspend = suspIn;
        rxStopped = stopOut;
        rxSuspended = suspOut;
        isServer = server;
        //TCP socket
        clientSocket = client;
        clientStream = cStream;
        validSockets = vSockets;
        serverStreams = sStream;
        //There are currently no purely internal variables, so setup is complete
    }

    /**
     * \brief   Starts execution of the NetworkRxThread object
     * \note    Do not use this function. NetworkThread will create and start NetworkTxThread automatically.
     */
    public void run()
    {
        boolean cached_isServer;
        boolean hasData;

        //loop until STOP command
        while( !rxStop.get() )
        {
            //suspend thread
            while( rxSuspend.get() )
            {
                rxSuspended.set(true);
            }
            //change flag when exiting suspened state
            rxSuspended.set(false);

            cached_isServer = isServer.get();
            hasData = true;
            do{
                //set hasData to false for default check
                if(cached_isServer){
                    //check each connected socket
                    for(int i = 0; i < NetworkThread.MAX_CONNECTIONS; i++){
                        if(validSockets[i].get()){
                            if(serverStreams[i].available() != 0){
                                hasData = true;
                                read(serverStreams[i]);
                            }
                        }
                    }
                }
                else{
                    //just check the one connected socket
                    if(clientSocket.isConnected()){
                        if(clientStream.available() != 0){
                            hasData = true;
                            read(clientStream);
                        }
                    }
                }
            } while(hasData);


            
        }
        //signal thread is terminating
        rxStopped.set(true);
        return;
    }

    private void read(ByteArrayInputStream stream)
    {
        byte[] buffer;
        byte[] id = new byte[1];
        int length;
        NetworkPacketData data;
        //read 1st by to determine packet type
        stream.read(id, 0, 1);
        length = NetworkPacketData.numBytes(id[0]);
        buffer = new byte[length];
        //fill buffer
        buffer[0] = id[0];
        stream.read(buffer, 1, length-1);
        //assemble the rest of the packet
        data = new NetworkPacketData(buffer);
        //push onto the queue
        try{
            dataIn.put(data);
        }
        catch(Exception e){
            //call a standard network error function?
        }
    }
}