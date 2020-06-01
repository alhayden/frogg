package FroggThread.Network;

public class NetworkPacketData {
    
    //determines how the data should be interpreted
    //and which fields are used
    public NetworkPacketType type;

    public NetworkPacketData(){
        //standard constructor
    }

    public NetworkPacketData(byte[] stream){

    }

    public byte[] toBytes(){
        return new byte[1];
    }

    public static int numBytes(byte id)
    {
        return 0;
    }
}