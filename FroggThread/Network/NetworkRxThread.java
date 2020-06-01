package FroggThread.Network;

//java SDK imports
import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

//project imports
import FroggThread.Network.*;

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