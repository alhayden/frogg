package FroggThread.Network;

//java SDK imports
import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

//project imports
import FroggThread.Network.*;

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