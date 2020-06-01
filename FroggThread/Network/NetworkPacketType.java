/**
 * \file    NetworkPacketType.java
 * \authors Zachary Hayden
 * \date    May 31, 2020
 * \brief   Defines a set of types of network packets
 * \details Defines static bytes that can be used to determine the type, size and contents
 *          of packets transfered over the network
 */

package FroggThread.Network;

/**
 * \class   NetworkPacketType
 * \brief   Defines constant type ID's for NetworkPacketData objects
 */
public class NetworkPacketType {
    /**
     * @brief Packet containing settings/setup information
     */
    public static final byte SETUP = 0; 
    /**
     * @brief Standard packet containing game state/action data
     */    
    public static final byte STANDARD = 1;  
    /**
     * @brief Returns results of the network test 
     */
    public static final byte STATUS = 2;   
    /**
     * @brief Request a test of the network from the server
     */ 
    public static final byte TEST = 3;      
}