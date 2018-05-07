package ws2d.init;

import stg.json.JsonObject;

/**
 * The configuration class for Ws2D.
 * 
 * @author Ian
 */
public final class Config {
    /**
     * The HTTP server port.
     */
    private int httpPort;
    /**
     * The ticks per second of the game loop.
     */
    private int tps;
    /**
     * The maximum number of clients that can connect to the server.
     */
    private int maxClients;
    /**
     * How often (in seconds) a heartbeat packet is emitted.
     */
    private int heartbeatInterval;
    
    /**
     * Constructs a new instance of <code>Config</code> with an optional <code>JsonObject</code>
     * that can override default settings.
     * 
     * @param data the optional JSON settings.
     */
    public Config(JsonObject data) {
        this.httpPort = 8484;
        this.tps = 20;
        this.maxClients = 20;
        this.heartbeatInterval = 60;
        if(data == null)
            return;
        if(data.hasTag("httpPort"))
            setHttpPort(data.getNumberAsInteger("httpPort"));
        if(data.hasTag("tps"))
            setTps(data.getNumberAsInteger("tps"));
        if(data.hasTag("maxClients"))
            setMaxClients(data.getNumberAsInteger("maxClients"));
        if(data.hasTag("heartbeatInterval"))
            setHeartbeatInterval(data.getNumberAsInteger("heartbeatInterval"));
    }

    /**
     * Returns the HTTP server port.
     * 
     * @return the HTTP server port.
     */
    public int getHttpPort() {
        return httpPort;
    }

    /**
     * Sets the server HTTP port to the specified value. If this value does not
     * satisfy the condition <code>0 &lt; port &lt; 65536</code> no action is taken.
     * It is also important to note that this function has no effect on the used
     * port if it is called after the server starts.
     * 
     * @param httpPort the new HTTP port.
     */
    public void setHttpPort(int httpPort) {
        if(httpPort < 1 || httpPort > 65535)
            return;
        this.httpPort = httpPort;
    }

    /**
     * Returns the desired ticks per second (TPS) of the server.
     * 
     * @return the desired TPS of the server.
     */
    public int getTps() {
        return tps;
    }

    /**
     * Sets the server ticks per second (TPS) to the specified value. If this value
     * does not satisfy the condition <code>0 &lt; tps &lt; 951</code> no action
     * is taken. It is also important to note that this function has no effect on
     * the TPS of the game loop if it is called after the server starts.
     * 
     * @param tps the new TPS.
     */
    public void setTps(int tps) {
        if(tps < 1 || tps > 950)
            return;
        this.tps = tps;
    }

    /**
     * Returns the maximum number of clients the server can support.
     * 
     * @return the maximum number of clients the server can support.
     */
    public int getMaxClients() {
        return maxClients;
    }

    /**
     * Sets the server max clients to the specified value. If this value does not
     * satisfy the condition <code>0 &lt; maxClients</code> no action is taken.
     * It is also important to note that this function has no effect on the effective
     * maximum number of clients if it is called after the server starts.
     * 
     * @param maxClients the new maximum number of clients.
     */
    public void setMaxClients(int maxClients) {
        if(maxClients < 0)
            return;
        this.maxClients = maxClients;
    }

    /**
     * Returns the heartbeat interval of the server. This is how often a packet
     * is emitted to check a client's connection.
     * 
     * @return the heartbeat interval of the server.
     */
    public int getHeartbeatInterval() {
        return heartbeatInterval;
    }

    /**
     * Sets the server heartbeat interval to the specified value. If this value
     * does not satisfy the condition <code>9 &lt; interval</code> no action is
     * taken. It is also important to note that this function has no effect on the
     * effective heartbeat interval if it is called after the server starts.
     * 
     * @param heartbeatInterval the new heartbeat interval.
     */
    public void setHeartbeatInterval(int heartbeatInterval) {
        if(heartbeatInterval < 10)
            return;
        this.heartbeatInterval = heartbeatInterval;
    }
}
