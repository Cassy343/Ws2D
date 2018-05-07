package ws2d.init;

import java.io.File;
import ws2d.core.command.CommandHandler;
import ws2d.core.command.CommandStop;
import ws2d.core.network.packet.BPacketHeartbeat;
import ws2d.core.network.packet.SPacketClientUid;
import ws2d.core.server.Server;

/**
 * This class manages all unconnected parts of Ws2D, including the physics engine,
 * server, and more. This class is self-contained, so its instance can be accessed
 * from itself.
 * 
 * @author Ian
 */
public final class Ws2D {
    /**
     * The Ws2D config.
     */
    private Config config;
    /**
     * The game manager.
     */
    private GameManager gameManager;
    /**
     * The registry.
     */
    private final Registry registry;
    /**
     * The console command handler.
     */
    private CommandHandler commandHandler;
    /**
     * The server.
     */
    private Server server;
    /**
     * The current initialization state of Ws2D.
     */
    private InitializationState initState;
    
    /**
     * The only instance of Ws2D.
     */
    private static final Ws2D INSTANCE = new Ws2D();
    
    /**
     * Constructs a new instance of <code>Ws2D</code>.
     */
    private Ws2D() {
        this.config = null;
        this.gameManager = null;
        this.registry = new Registry();
        this.commandHandler = null;
        this.server = null;
        this.initState = InitializationState.BOOT;
    }
    
    /**
     * Boots the Ws2D platform from a given game jar file.
     * 
     * @param gameFile the game file.
     */
    public void boot(File gameFile) {
        if(initState != InitializationState.BOOT)
            return;
        initState = InitializationState.PRE_INIT; // prevent booting during runtime
        this.gameManager = new GameManager(gameFile);
        
        preInit();
        initState = InitializationState.INIT;
        init();
        initState = InitializationState.POST_INIT;
        postInit();
        initState = InitializationState.FINISHED;
        start();
    }
    
    /**
     * Initializes loggers, the server, and the command handler.
     */
    private void preInit() {
        this.config = gameManager.getConfig();
        this.server = new Server(config);
        this.commandHandler = new CommandHandler(server);
        gameManager.preInit();
    }
    
    /**
     * Registers defaults to the registry and initializes the game.
     */
    private void init() {
        registerPresets();
        gameManager.init();
    }
    
    /**
     * Calls post-initialization on the game.
     */
    private void postInit() {
        gameManager.postInit();
    }
    
    /**
     * Starts the server, the command handler, and the game.
     */
    private void start() {
        server.start();
        commandHandler.start();
        gameManager.startGame();
    }
    
    /**
     * Ends the current session of Ws2D.
     */
    public void shutdown() {
        if(gameManager != null)
            gameManager.endGame();
        if(server != null && server.isAlive())
            server.shutdown();
        if(commandHandler != null && commandHandler.isAlive())
            commandHandler.interrupt();
    }
    
    /**
     * Returns the one and only instance of Ws2D.
     * 
     * @return the instance of Ws2D.
     */
    public static Ws2D getInstance() {
        return INSTANCE;
    }
    
    /**
     * Returns the configuration of Ws2D.
     * 
     * @return the configuration of Ws2D.
     */
    public static Config getConfig() {
        return INSTANCE.config;
    }
    
    /**
     * Returns the registry of Ws2D.
     * 
     * @return the registry of Ws2D.
     */
    public static Registry getRegistry() {
        return INSTANCE.registry;
    }
    
    /**
     * Returns the current running server instance.
     * 
     * @return the current running server instance.
     */
    public static Server getServer() {
        return INSTANCE.server;
    }
    
    /**
     * Returns the game manager.
     * 
     * @return the game manager.
     */
    public static GameManager getGameManager() {
        return INSTANCE.gameManager;
    }
    
    /**
     * Register presets, called during initialization.
     */
    private static void registerPresets() {
        Registry r = INSTANCE.registry;
        
        /* Command Stuff */
        r.registerCommand(new CommandStop());
        
        /* Network Stuff */
        r.registerPacket(BPacketHeartbeat.class, (server, client, packet) -> {
            client.setConnectionVerified(true);
            return null;
        });
        r.registerPacket(SPacketClientUid.class, null);
    }
}
