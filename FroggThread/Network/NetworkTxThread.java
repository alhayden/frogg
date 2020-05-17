package FroggThread.Network;

public class NetworkTxThread extends Thread{
    //queue->network
    
    //network config data
    private boolean isServer;
    private InetSocketAddress serverAddr;
    
    //internal communications
    private final PriorityBlockingQueue<FrogPacketData> dataIn;
    private final BlockingQueue<Integer> commIn;
    
    //external communications
    private Socket clientTx;
    private ArrayList<Socket> serverTx;
    private ServerSocket serverConn;
    
    public NetworkTxThread()
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