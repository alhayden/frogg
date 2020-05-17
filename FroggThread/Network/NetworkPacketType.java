package FroggThread.Network;

public enum NetworkPacketType {
    SETUP,      //initial packet containing settings/setup information
    STANDARD,   //standard packet containing game state/action data
    TEST;       //network test results
}