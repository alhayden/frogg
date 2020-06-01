package FroggThread.Network;

public class NetworkPacketData {
    

    /**
     * \brief   Determines how the data should be interpreted and which fields are used
     */
    public byte type;

    //private definition of total length of each packet
    private static final int SETUP_LENGTH = 0;
    private static final int STANDARD_LENGTH = 0;
    private static final int STATUS_LENGTH = 0;
    private static final int TEST_LENGTH = 0;

    //hold all of the internals as a byte array
    private byte[] data;

    /**
     * @brief   Creates a NetworkPacketData object of type SETUP
     * @param a
     */
    public NetworkPacketData(String a){
        //standard constructor
    }

    /**
     * @brief   Creates a NetworkPacketData object of type STANDARD
     * @param a
     */
    public NetworkPacketData(int a){
        //standard constructor
    }

    /**
     * @brief   Creates a NetworkPacketData object of type STATUS
     * @param a
     */
    public NetworkPacketData(byte[] a){
        //standard constructor
    }

    /**
     * @brief   Creates a NetworkPacketData object of type TEST
     * @param a
     */
    public NetworkPacketData(byte a){
        //standard constructor
    }

    /**
     * @brief   Creates a NetworkPacketData object from a byte array
     * @param   stream  input byte array
     * @note    This constructor should ONLY be used by the network threads
     */
    public NetworkPacketData(byte[] stream){
        //run checks?
        data = stream;
        type = data[0];
        //I know this is bad form for object-oriented code
        //but I don't want to copy the buffers during TX/RX
        //if I can avoid it
    }

    /**
     * @brief Returns the packet as an array of bytes 
     * @return  The packet expressed as an array of bytes
     * @note    This function should ONLY be called by the network threads
     */
    public byte[] toBytes(){
        //make this zero-copy (for now, at least)
        //the other objects interacting with this (network threads) will use the same object
        //use the functions that return primitives if possible for application code
        return data;
    }

    /**
     * @brief   Returns that length of the specified type of packet
     * @param   type    Byte representing the type ID
     * @return  Length of the byte representation of this object (in bytes)
     */
    public static int numBytes(byte type)
    {
        //return the private length definition
        //for the passed packet type id
        switch(type){
            case NetworkPacketType.SETUP:
                return SETUP_LENGTH;
            case NetworkPacketType.STANDARD:
                return STANDARD_LENGTH;
            case NetworkPacketType.STATUS:
                return STATUS_LENGTH;
            case NetworkPacketType.TEST:
                return TEST_LENGTH;
            default:
                return 0;
        }
    }
}