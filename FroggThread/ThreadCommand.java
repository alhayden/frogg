/**
 * \file    ThreadCommand.java
 * \authors Zachary Hayden
 * \date    May 17, 2020
 * \brief   Defines a set of commands for thread control
 * \details Defines an enum that contains a common pool of commands for controlling the threads in this
 *          project. Not all threads must implement all functions, for example a keyboard thread would
 *          not implement socket control commands, but all should implement STOP, SUSPEND and RESUME.
 */


package FroggThread;

/**
 * \class   ThreadCommand
 * \brief   Defines commands for thread control
 * \details *
 */
public enum ThreadCommand{
    //replies for commands that require verification----------------------------------------------------
    ACK,                /**< Acknowledges successful completion of command 
                         *  \brief Acknowledges successful completion of command
                         */
    NACK,               /**< Acknowledges unsuccessful completion of command 
                         *  \brief Acknowledges unsuccessful completion of command
                         */
    STATUS,             /**< Returns detailed information 
                         *  \brief Returns detailed information
                         */
    //general thread commands --------------------------------------------------------------------------
    STOP,               /**< Stops execution of the thread (the thread should terminate as soon as possible)
                         *  \brief Stops execution of the thread
                         */
    SUSPEND,            /**< Pauses execution of the thread (the thread should wait for RESUME)
                         *  \brief Pauses execution of the thread
                         */
    RESUME,             /**< Resumes execution of the thread
                         *  \brief Resumes execution of the thread
                         */
    //socket control commands---------------------------------------------------------------------------
    SET_MY_PORT,        /**< Attempts to set local port to the specified port if disconnected
                         *  \brief Attempts to set local port to the specified port if disconnected
                         *  \result ACK or NACK, depending on success  
                         */
    SET_REMOTE_PORT,    /**< Sets the remote port to the specified port if disconnected
                         *  \brief Sets the remote port to the specified port if disconnected
                         *  \result ACK if disconnected, or NACK if connected 
                         */
    SET_REMOTE_IP,      /**< Sets the remote IP to the selected IP if disconnected
                         *  \brief Sets the remote IP to the selected IP if disconnected
                         *  \result ACK if disconnected, or NACK if connected 
                         */
    SET_SERVER,         /**< Sets network connection to server mode
                         *  \brief client: Disconnects and changes mode to server \n
                         *         server: No effect
                         */
    SET_CLIENT,         /**< Sets network connection to client mode
                         *  \brief client: No effect \n
                         *         server: Disconnects all and changes mode to client
                         */
    CONNECT,            /**< Tries to connect over the network
                         *  \brief client: Tries to connect to stored IP and port \n
                         *         server: Allows the server to accept connections
                         *  \result client: ACK or NACK, depending on success
                         *  \result server: ACK
                         */
    FREEZE,             /**< server only: Stops server from accepting new connections
                         *  \brief Stops server from accepting new connections
                         */
    DISCONNECT,         /**< Attempts to terminate network connection
                         *  \brief client: Disconnects from server \n
                         *         server: Tries to disconnect from the specified IP and port
                         *  \result client: ACK
                         *  \result server: ACK or NACK, depending on success
                         */
    GET_NET_STATUS,     /**< Gets status info from the network thread
                            \brief Gets status info from the network thread
                            \result STATUS, with network status info 
                        */
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
