package ws2d.init;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import stg.generic.Minifier;
import stg.generic.Pair;
import stg.json.JsonFormatException;
import stg.json.JsonObject;
import stg.reflect.ReflectionHelper;
import ws2d.init.reflect.Init;
import ws2d.init.reflect.OnEnd;
import ws2d.init.reflect.OnStart;
import ws2d.init.reflect.PostInit;
import ws2d.init.reflect.PreInit;
import ws2d.util.ErrorCodes;
import ws2d.util.FileSystem;
import ws2d.util.Logger;

/**
 * The wrapper around a given game jar. This class loads and stores the data needed
 * to run a provided game file.
 * 
 * @author Ian
 */
public class GameManager {
    /**
     * The Ws2D configuration.
     */
    private Config config;
    /**
     * The game jar file.
     */
    private JarFile jar;
    /**
     * The game's data file.
     */
    private JsonObject gameData;
    /**
     * The class loader to load files from the game jar.
     */
    private URLClassLoader classLoader;
    /**
     * The instantiated main class of the game.
     */
    private Object gameObject;
    /**
     * Whether or not the game is running.
     */
    private boolean isGameRunning;
    
    /**
     * The manager's logger.
     */
    private static final Logger LOG0 = new Logger("GAME-LOADER");
    /**
     * The game's logger.
     */
    private static final Logger LOG = new Logger("GAME");
    /**
     * The required tags for the game's JSON data file.
     */
    private static final String[] REQUIRED_GAME_DATA_TAGS = {"clientDir", "mainHTML", "main", "name"};
    /**
     * The directory for caching requested web files.
     */
    private static final String WEB_CACHE = ".ws2d-webcache";
    
    /**
     * Loads and checks the validity of the game jar.
     */
    private void initInternal() {
        LOG0.info("Loading game file...");
        try {
            ZipEntry gameJson = jar.getEntry("game.json");
            if(gameJson == null) {
                LOG0.error("Could not find game.json in root directory of game jar file.");
                ErrorCodes.INVALID_GAME_FILE.fail();
                return;
            }
            InputStream istream = jar.getInputStream(gameJson);
            StringBuilder sb = new StringBuilder();
            int b;
            while((b = istream.read()) > 0)
                sb.appendCodePoint(b);
            istream.close();
            gameData.readStringValue(new String(Minifier.minify(sb.toString())));
            
            for(String tag : REQUIRED_GAME_DATA_TAGS) {
                if(!gameData.hasTag(tag)) {
                    LOG0.error("Required fields in game.json: " + String.join(", ", REQUIRED_GAME_DATA_TAGS));
                    ErrorCodes.INVALID_GAME_FILE.fail();
                    return;
                }
            }

            LOG0.info("Loaded configuration for \"" + gameData.getString("name") + "\"");
            if(gameData.getObject("serverSettings") != null) {
                LOG0.info("Applying setting changes...");
                config = new Config(gameData.getObject("serverSettings"));
            }else
                config = new Config(null);
            
            if(jar.getEntry(gameData.getString("clientDir") + "/" + gameData.getString("mainHTML")) == null) {
                LOG0.error("Failed to find main HTML file in client folder.");
                ErrorCodes.INVALID_GAME_FILE.fail();
                return;
            }
            
            Class main = classLoader.loadClass(gameData.getString("main"));
            LOG0.info("Loaded main class. Verifying...");
            gameObject = ReflectionHelper.instantiate(main);
            if(gameObject == null) {
                LOG0.error("Invalid main class: no default constructor.");
                ErrorCodes.INVALID_GAME_FILE.fail();
            }
            
            File webCacheDir = new File(WEB_CACHE);
            if(webCacheDir.exists())
                FileSystem.delete(webCacheDir);
            webCacheDir.mkdirs();
        }catch(JsonFormatException jfe) {
            LOG0.error("Invalid game.json file. Required object with fields " + String.join(", ", REQUIRED_GAME_DATA_TAGS));
            ErrorCodes.INVALID_GAME_FILE.fail();
        }catch(ClassNotFoundException cnfe) {
            LOG0.error("Invalid main field in game.json: class not found.");
            ErrorCodes.INVALID_GAME_FILE.fail();
        }catch(IOException ex) {
            ex.printStackTrace(System.out);
            ErrorCodes.UNKNOWN_ERROR.fail();
        }
    }
    
    /**
     * Constructs a new instance of <code>GameManager</code> with a specified game
     * jar file.
     * 
     * @param file the game jar file.
     */
    public GameManager(File file) {
        this.config = null;
        this.gameData = new JsonObject();
        this.gameObject = null;
        this.isGameRunning = false;
        try {
            this.classLoader = new URLClassLoader(new URL[] {file.toURI().toURL()});
        }catch(MalformedURLException ex) {
            throw new InternalError(ex);
        }
        try {
            this.jar = new JarFile(file);
        }catch(IOException ex) {
            ErrorCodes.INVALID_GAME_FILE.fail();
            return;
        }
        
        initInternal();
    }
    
    /**
     * Returns the configuration of Ws2D with any changes the game made in its data
     * file.
     * 
     * @return the configuration of Ws2D.
     */
    public Config getConfig() {
        return config;
    }
    
    /**
     * Gets a given web file for a client, and caches that file if needed.
     * 
     * @param request the file requested.
     * @return the requested web file path, or null if that file does not exist.
     */
    public String getClientFile(String request) {
        request = request.substring(1);
        File cached = new File(WEB_CACHE + File.separator + request);
        if(!cached.exists()) {
            try {
                ZipEntry ze = jar.getEntry(gameData.getString("clientDir") + "/" + request);
                if(ze == null)
                    return null;
                FileSystem.copy(jar.getInputStream(ze), cached);
            }catch(IOException ex) {
                LOG.warn("Encountered exception while trying to cache web file.");
                ex.printStackTrace(System.out);
                return null;
            }
        }
        return cached.getAbsolutePath();
    }
    
    /**
     * Runs the pre-initialization method of the game if it exists.
     */
    public void preInit() {
        Pair<Method, Annotation> preInitMethod = ReflectionHelper.getAnnotatedMethod(gameObject.getClass(), PreInit.class);
        if(preInitMethod != null) {
            Class<?>[] params = preInitMethod.getFirst().getParameterTypes();
            if(params.length == 1 && Logger.class.equals(params[0]))
                ReflectionHelper.invoke(preInitMethod.getFirst(), gameObject, LOG);
            else
                ReflectionHelper.invoke(preInitMethod.getFirst(), gameObject);
        }
    }
    
    /**
     * Runs the initialization method of the game if it exists.
     */
    public void init() {
        tryInvoke(Init.class);
    }
    
    /**
     * Runs the post-initialization method of the game if it exists.
     */
    public void postInit() {
        tryInvoke(PostInit.class);
    }
    
    /**
     * Runs the game start listener of the game if it exists.
     */
    public void startGame() {
        if(isGameRunning)
            return;
        isGameRunning = true;
        tryInvoke(OnStart.class);
    }
    
    /**
     * Runs the game end listener of the game if it exists.
     */
    public void endGame() {
        isGameRunning = false;
        tryInvoke(OnEnd.class);
        try {
            jar.close();
            FileSystem.delete(new File(WEB_CACHE));
        }catch(IOException ex) {
            LOG.warn("Failed to close game jar file.", ex);
        }
    }
    
    /**
     * Attempts to invoke a method with the specified annotation and arguments.
     * 
     * @param annotation the annotation class.
     * @param args the arguments for the method.
     */
    private void tryInvoke(Class<? extends Annotation> annotation, Object... args) {
        Pair<Method, Annotation> method = ReflectionHelper.getAnnotatedMethod(gameObject.getClass(), annotation);
        if(method != null)
            ReflectionHelper.invoke(method.getFirst(), gameObject, args);
    }
}
