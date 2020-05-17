package FroggThread.Network;

//java SDK imports
import java.util.*;
import java.util.concurrent.*;
import java.net.*;

//project imports
import FroggThread.*;
import FroggThread.Network.*;
import FroggThread.IO.*;

public class NetworkRxThread extends Thread{
    //queue->network
    
    //network config data
    private boolean isServer;
    private InetSocketAddress serverAddr;
    
    //internal communications
    private final PriorityBlockingQueue<NetworkPacketData> dataIn;
    private final BlockingQueue<Integer> commIn;
    
    //external communications
    private Socket clientTx;
    private ArrayList<Socket> serverTx;
    private ServerSocket serverConn;
    
    public NetworkRxThread()
    {

    }

    public void run()
    {
        ThreadCommand command;
        //read commands
        command = ThreadCommand( commIn.poll() );

        //determine if/how to send data to the sockets
    }
}