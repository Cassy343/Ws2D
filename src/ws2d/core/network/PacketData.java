package ws2d.core.network;

/**
 * Contains metadata for a given packet type. This includes the class of the packet,
 * the handler, and the packet ID.
 * 
 * @author Ian
 * @param <T> the packet type.
 */
public final class PacketData<T extends Packet> {
    /**
     * The packet's class.
     */
    public final Class<T> packetClass;
    /**
     * The packet's handler.
     */
    public final PacketHandler<T> handler;
    /**
     * The packet's ID.
     */
    public final int id;

    /**
     * Constructs a new instance of <code>PacketData</code> with the specified packet
     * class, the handler, and the packet ID.
     * 
     * @param packetClass the packet class.
     * @param handler the packet handler.
     * @param id the packet ID.
     */
    public PacketData(Class<T> packetClass, PacketHandler<T> handler, int id) {
        this.packetClass = packetClass;
        this.handler = handler;
        this.id = id;
    }
}
