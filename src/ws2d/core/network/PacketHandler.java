package ws2d.core.network;

import ws2d.core.generic.Client;
import ws2d.core.server.Server;

/**
 * Defines the protocol for handling packets.
 * 
 * @author Ian
 * @param <T> the packet type.
 */
@FunctionalInterface
public interface PacketHandler<T extends Packet> {
    /**
     * The method for handing a packet. This method does not require a packet to
     * be returned; if there is no packet to return, return <code>null</code>.
     * 
     * @param server the current running server instance.
     * @param sender the client who sent the packet.
     * @param msg the packet that was sent.
     * @return a response packet if there is one, <code>null</code> otherwise.
     */
    Packet onMessage(Server server, Client sender, T msg);
}
