package FroggThread;

public class ThreadCommandData {
    
    //determines how the data should be interpreted
    //and which fields are used
    public ThreadCommand command;

    //byte array to hold the actual data
    private byte[] data;

    //command ID
    private static int lastID;
    private final int dataId;

    //constructor for the following commands:
    //STOP, SUSPEND, RESUME, SET_SERVER, SET_CLIENT, 
    //CONNECT, FREEZE, DISCONNECT, GET_NET_STATUS
    public ThreadCommandData(ThreadCommand comm, byte id)
    {
        command = comm;
        //only data required is the source/dest thread id
        data = new byte[]{id};
    }

    //constructor for the following commands:
    //ACK, NACK
    public ThreadCommandData(ThreadCommand comm, byte id, byte[]uid)
    {
        
    }

}