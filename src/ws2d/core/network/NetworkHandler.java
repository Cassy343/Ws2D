package ws2d.core.network;

import ws2d.core.generic.Client;
import ws2d.init.Ws2D;
import ws2d.core.server.Server;
import ws2d.util.Logger;
import ws2d.util.Utils;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.streams.StreamBase;
import java.util.Arrays;
import stg.buffer.ByteBuffer;
import stg.reflect.ReflectionHelper;

/**
 * The chief class that manages network interactions in the Ws2D platform. This
 * class formats and passes packets to the appropriate listeners, and also manages
 * error handling.
 * 
 * @author Ian
 */
public class NetworkHandler {
    /**
     * The logger for the <code>NetworkHandler</code>.
     */
    private static final Logger LOG = new Logger("NET");
    
    /**
     * Attaches the socket to the network system of Ws2D so that packets are directed
     * to the correct listeners.
     * 
     * @param socket the socket to attach.
     * @param server the current running server instance.
     */
    public void bindMessageHandler(ServerWebSocket socket, Server server) {
        socket.handler(buf -> {
            int pid = buf.getByte(0), uid = buf.getByte(1);
            if(pid >= Ws2D.getRegistry().getRegisteredPackets().size()) {
                LOG.warn("A client sent an invalid packet. IP: " + socket.remoteAddress().toString());
                return;
            }
            PacketData<Packet> pd = Ws2D.getRegistry().getRegisteredPackets().get(pid);
            ByteBuffer buffer = new ByteBuffer(Arrays.copyOfRange(buf.getBytes(), 2, buf.length()));
            Packet packet = (Packet)ReflectionHelper.instantiate(pd.packetClass);
            packet.deserialize(buffer);
            Client client = server.getClient(uid);
            if(!Utils.socketAddressEquals(socket.remoteAddress(), client.getSocket().remoteAddress())) {
                LOG.info("A client attempted to act as another client. IP: " + socket.remoteAddress().host());
                socket.close();
                return;
            }
            Packet response = pd.handler.onMessage(server, client, packet);
            if(response != null)
                sendPacket(response, socket);
        });
    }
    
    /**
     * Attaches the default error handler to the specified socket.
     * 
     * @param stream the socket stream base.
     */
    public void bindErrorHandler(StreamBase stream) {
        stream.exceptionHandler(t -> LOG.warn("Encountered a network error.", t));
    }
    
    /**
     * Formats and sends a packet through the specified socket.
     * 
     * @param packet the packet to send.
     * @param socket the socket to send the packet through.
     */
    public void sendPacket(Packet packet, ServerWebSocket socket) {
        ByteBuffer buffer = new ByteBuffer();
        buffer.append(getPacketData(packet).id);
        packet.serialize(buffer);
        socket.write(Utils.buffer(buffer.toArray()));
    }
    
    /**
     * Gets the packet data for a specified instantiated packet.
     * 
     * @param packet the packet to retrieve data or.
     * 
     * @return the packet data for the specified packet.
     */
    private PacketData getPacketData(Packet packet) {
        for(PacketData pd : Ws2D.getRegistry().getRegisteredPackets()) {
            if(packet.getClass().equals(pd.packetClass))
                return pd;
        }
        return null;
    }
}
