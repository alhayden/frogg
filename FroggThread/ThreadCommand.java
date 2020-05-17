package FroggThread;

public enum ThreadCommand{
    //A ThreadCommand is sent as in integer and may be followed by a number of 
    //integer data words

    //replies for commands that require verification---------------------
    ACK,
    NACK,
    STATUS,
    //general thread commands ---------------------------------------------
    STOP, //stops execution of the thread (the thread should return as soon as possible)
    SUSPEND,
    RESUME,
    //socket control commands----------------------------------------------
    SET_MY_PORT,
    SET_REMOTE_PORT,
    SET_REMOTE_IP,
    SET_SERVER,
    SET_CLIENT,
    CONNECT,            //client: tries to connect to given IP and port
                        //server: allows the server to accept connections
    FREEZE,             //server only: stops server from accepting new connections
    DISCONNECT,         //client: disconnects from server   
                        //server: tries to disconnect from the specified IP and port
    GET_NET_STATUS,
    //keyboard control commands
    SET_KEYBIND,

    INVALID;

//     private final int code;

//     //ThreadCommand constructor from int
//     ThreadCommand( int commCode )
//     {
//         this.code = commCode;
//     }

//     //get the code for transmission
//     public int code()
//     {
//         return this.code;
//     }
}