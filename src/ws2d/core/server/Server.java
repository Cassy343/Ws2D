package ws2d.core.server;

import ws2d.core.generic.Client;
import ws2d.init.Ws2D;
import ws2d.core.network.NetworkHandler;
import ws2d.core.network.packet.BPacketHeartbeat;
import ws2d.core.network.packet.SPacketClientUid;
import ws2d.util.Logger;
import ws2d.util.UidSet;
import ws2d.util.Utils;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;
import ws2d.init.Config;

/**
 * The main class of Ws2D besides the API class <code>ws2d.init.Ws2D</code>, and
 * the launcher class <code>ws2d.main.Main</code>. This class contains the HTTP
 * server instance, manages the network handler, and runs the game loop.
 * 
 * @author Ian
 */
public class Server extends Thread {
    /**
     * The Ws2D configuration.
     */
    private final Config config;
    /**
     * The HTTP server.
     */
    private final HttpServer httpServer;
    /**
     * The network handler.
     */
    private final NetworkHandler net;
    /**
     * The current set of WebSocket clients.
     */
    private final UidSet<Client> clients;
    /**
     * The server scheduler.
     */
    private final Scheduler scheduler;
    /**
     * Whether or not the game loop is running.
     */
    private volatile boolean run;
    
    /**
     * The server logger.
     */
    private static final Logger LOG = new Logger("SERVER");
    
    /**
     * Constructs a new instance of <code>Server</code> with the specified, fully
     * initialized configuration.
     * 
     * @param config the configuration of this Ws2D instance.
     */
    public Server(Config config) {
        this.config = config;
        this.httpServer = Utils.VERTX.createHttpServer();
        this.net = new NetworkHandler();
        this.clients = new UidSet(config.getMaxClients());
        this.scheduler = new Scheduler();
        this.run = true;
    }
    
    /**
     * Starts the HTTP server and game loop.
     */
    @Override
    public void start() {
        httpServer.requestHandler(req -> {
            net.bindErrorHandler(req);
            String path = req.path();
            if(path.isEmpty() || "/".equals(path))
                path = "/index.html";
            path = Ws2D.getGameManager().getClientFile(path);
            if(path == null)
                LOG.info("Client requested file " + req.path() + ", which does not exist.");
            else
                req.response().sendFile(path);
        }).websocketHandler(socket -> {
            net.bindErrorHandler(socket);
            net.bindMessageHandler(socket, this);
            onClientConnect(socket);
        }).listen(config.getHttpPort(), result -> {
            if(result.failed()) {
                LOG.error("Failed to start server.");
                LOG.error(result.cause());
                Ws2D.getInstance().shutdown();
            }else
                LOG.info("Hosting HTTP service on port " + config.getHttpPort());
        });
        super.start();
    }
    
    /**
     * Runs the game loop.
     */
    @Override
    public void run() {
        long ticks = 0;
        final long tps = config.getTps();
        long delay = 1000L / tps;
        long tpsStart = System.currentTimeMillis();
        
        if(config.getHeartbeatInterval() > 0) {
            scheduler.runTaskRepeatedly(() -> {
                clients.forEach(client -> {
                    if(!client.isConnectionVerified()) {
                        disconnectClient(client);
                        return;
                    }
                    client.setConnectionVerified(false);
                    net.sendPacket(new BPacketHeartbeat(), client.getSocket());
                });
            }, config.getHeartbeatInterval() * tps);
        }
        
        scheduler.runTaskLater(() -> LOG.info("Successfully started server."), 30L);
        
        while(run) {
            tick();
            
            ++ ticks;
            if(ticks % tps == 0) {
                long ctmillis = System.currentTimeMillis();
                delay -= ((ctmillis - tpsStart) - 1000L) / tps;
                tpsStart = ctmillis;
            }
            
            sleep0(delay);
        }
        
        interrupt();
        System.exit(0);
    }
    
    /**
     * Stops the HTTP server and game loop.
     */
    @Override
    public void interrupt() {
        httpServer.close();
        super.interrupt();
    }
    
    /**
     * Stops the game loop, which allows <code>interrupt</code> to be called.
     */
    public void shutdown() {
        run = false;
    }
    
    /**
     * This is the code that is called once per tick in the game loop.
     */
    private void tick() {
        scheduler.tick();
    }
    
    /**
     * Called when a client connects to the server through a WebSocket connection.
     * 
     * @param socket the socket.
     */
    private void onClientConnect(ServerWebSocket socket) {
        if(clients.isFull()) {
            socket.close();
            return;
        }
        socket = socket.resume();
        final Client client = new Client(socket);
        clients.add(client);
        client.getSocket().closeHandler(unused -> onClientDisconnect(client));
        net.sendPacket(new SPacketClientUid(client.getUid()), socket);
    }
    
    /**
     * Terminates a client's connection.
     * 
     * @param client the client whose connection to terminate.
     */
    private void disconnectClient(Client client) {
        client.getSocket().close();
        clients.remove(client);
    }
    
    /**
     * Called when a client's connection terminates.
     * 
     * @param client the client whose connection terminated.
     */
    private void onClientDisconnect(Client client) {
        clients.remove(client);
    }
    
    /**
     * Returns the scheduler for this server instance.
     * 
     * @return the scheduler for this server instance.
     */
    public Scheduler getScheduler() {
        return scheduler;
    }
    
    /**
     * Returns the network handler for this server instance.
     * 
     * @return the network handler for this server instance.
     */
    public NetworkHandler getNetworkHandler() {
        return net;
    }
    
    /**
     * Gets the client with the specified unique ID.
     * 
     * @param uid the unique ID.
     * @return the client with the unique ID.
     */
    public Client getClient(int uid) {
        return clients.get(uid);
    }
    
    /**
     * This method is equivalent to <code>Thread.sleep</code>, however, it silences
     * any exceptions.
     * 
     * @param delay the amount of milliseconds to wait.
     */
    private static void sleep0(long delay) {
        if(delay <= 0)
            return;
        try {
            Thread.sleep(delay);
        }catch(Throwable t) { }
    }
}
