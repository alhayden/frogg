package FroggThread;

public enum ThreadCommand{
    //replies for commands that require verification----------------------------------------------------
    ACK,
    NACK,
    STATUS,
    //general thread commands --------------------------------------------------------------------------
    STOP,               //stops execution of the thread (the thread should return as soon as possible)
    SUSPEND,            //pauses execution of the thread (the thread should wait for RESUME)
    RESUME,             //resumes execution of the thread
    //socket control commands---------------------------------------------------------------------------
    SET_MY_PORT,        //attempts to set local port to the specified port if disconnected
                        //  responds: ACK or NACK, depending on success
    SET_REMOTE_PORT,    //sets the remote port to the specified port if disconnected
                        //  responds: ACK or NACK, depending on success
    SET_REMOTE_IP,      //sets the remote IP to the selected IP if disconnected
                        //  responds: ACK or NACK, depending on success
    SET_SERVER,         //client: disconnects and changes mode to server
                        //server: no effect
    SET_CLIENT,         //client: no effect
                        //server: disconnects all and changes mode to client
    CONNECT,            //client: tries to connect to given IP and port
                        //  responds: ACK or NACK, depending on success
                        //server: allows the server to accept connections
                        //  responds: ACK
    FREEZE,             //server only: stops server from accepting new connections
    DISCONNECT,         //client: disconnects from server   
                        //  responds: ACK 
                        //server: tries to disconnect from the specified IP and port
                        //  responds: ACK or NACK, depending on success
    GET_NET_STATUS,     //gets status info from the network thread
                        //  responds: STATUS, with network status info
    //keyboard control commands--------------------------------------------------------------------------
    SET_KEYBIND,//?

    //catch-all invalid command
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