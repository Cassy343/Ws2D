package ws2d.core.network.packet;

import stg.buffer.ByteBuffer;
import ws2d.core.network.Packet;

/**
 * Sends the client their unique ID for the server.
 * 
 * @author Ian
 */
public class SPacketClientUid implements Packet {
    /**
     * The client's unique ID.
     */
    private int clientUid;
    
    /**
     * Constructs a new instance of <code>SPacketClientUid</code> with the specified
     * unique ID.
     * 
     * @param clientUid the client's unique ID.
     */
    public SPacketClientUid(int clientUid) {
        this.clientUid = clientUid;
    }
    
    /**
     * Constructs a new instance of <code>SPacketClientUid</code> with a unique
     * ID of <code>-1</code>.
     */
    public SPacketClientUid() {
        this(-1);
    }
    
    /**
     * Serializes this packet's data to a byte buffer.
     * 
     * @param buffer the buffer to serialize to.
     */
    @Override
    public void serialize(ByteBuffer buffer) {
        buffer.append(clientUid);
    }
}
