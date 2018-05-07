package ws2d.core.generic;

import ws2d.util.UniquelyIdentifiableObject;
import io.vertx.core.http.ServerWebSocket;

/**
 * A wrapper for a <code>ServerWebSocket</code> that allows for connection verification.
 * This class also extends <code>UniquelyIdentifiableObject</code>, so it has a
 * unique ID and can be used in a <code>UidSet</code>.
 * 
 * @author Ian
 */
public class Client extends UniquelyIdentifiableObject {
    /**
     * The server web socket.
     */
    private final ServerWebSocket socket;
    /**
     * Whether or not the client's connection is still valid.
     */
    private boolean connectionVerified;
    
    /**
     * Constructs a new instance of <code>Client</code> with a server web socket.
     * 
     * @param socket the server web socket.
     */
    public Client(ServerWebSocket socket) {
        super();
        this.socket = socket;
        this.connectionVerified = true;
    }
    
    /**
     * Returns the server web socket.
     * 
     * @return the server web socket.
     */
    public ServerWebSocket getSocket() {
        return socket;
    }
    
    /**
     * Updates the verification state of this client to the specified value.
     * 
     * @param verified whether or not the connection is verified.
     */
    public void setConnectionVerified(boolean verified) {
        connectionVerified = verified;
    }
    
    /**
     * Returns whether or not the connection of this client is valid.
     * 
     * @return <code>true</code>, if the connection is verified, <code>false</code>
     * otherwise.
     */
    public boolean isConnectionVerified() {
        return connectionVerified;
    }
}
