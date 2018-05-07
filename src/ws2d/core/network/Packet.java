package ws2d.core.network;

import stg.buffer.ByteBuffer;

/**
 * The description of all packets that be registered for the network system. Both
 * methods of this interface default to do nothing due to that fact that some packets
 * will not need those methods due to their origin or destination.
 * 
 * @author Ian
 */
public interface Packet {
    /**
     * Serializes this packet instance to a byte buffer if possible.
     * 
     * @param buffer the buffer to serialize to.
     */
    default void serialize(ByteBuffer buffer) { }
    
    /**
     * Deserializes the packet from a buffer if possible.
     * 
     * @param buffer the buffer to deserialize from.
     */
    default void deserialize(ByteBuffer buffer) { }
}
