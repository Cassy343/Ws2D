package ws2d.main;

import ws2d.util.FileSystem;
import ws2d.init.Ws2D;
import ws2d.util.ErrorCodes;
import ws2d.util.Logger;
import java.io.File;
import ws2d.init.libs.Libraries;
import ws2d.init.setup.ProjectSetup;

/**
 * The launcher for Ws2D.
 * 
 * @author Ian
 */
public class Main {
    /**
     * The launcher's logger.
     */
    private static final Logger LOG = new Logger("LAUNCHER");
    /**
     * The vert.x cache directory.
     */
    private static final String VERTX_DIR = "./.vertx";
    
    /**
     * The entry point of Ws2D.
     * 
     * @param args the command line arguments.
     */
    public static void main(String[] args) {
        if(args.length == 0)
            ErrorCodes.INVALID_ARGS.fail();
        
        try {
            String operation = args[0];
            if("launch".equalsIgnoreCase(operation)) {
                if(args.length == 0) {
                    LOG.error("Usage: ws2d launch <gameJarFile>");
                    ErrorCodes.INVALID_ARGS.fail();
                }
                
                File file = new File(args[1]);
                if(!file.exists())
                    ErrorCodes.INVALID_GAME_FILE.fail();

                LOG.info("Initializing libraries...");
                Libraries.initializeLibs();
                
                LOG.info("Starting server.");
                File vertxDir = new File(VERTX_DIR);
                if(vertxDir.exists()) {
                    LOG.info("Cleaning Vertx cache.");
                    FileSystem.delete(vertxDir);
                }

                Ws2D.getInstance().boot(file);
            }else if("setup".equalsIgnoreCase(operation)) {
                if(args.length < 6) {
                    LOG.error("Usage: ws2d setup <rootDir> <mainClass> <projectName> <ws2dJar> <bash|batch>");
                    ErrorCodes.INVALID_ARGS.fail();
                }
                boolean bashOrBatch;
                if("bash".equalsIgnoreCase(args[5]))
                    bashOrBatch = true;
                else if("batch".equalsIgnoreCase(args[5]) || "bat".equalsIgnoreCase(args[5]))
                    bashOrBatch = false;
                else{
                    LOG.error("Invalid argument: " + args[5] + ", expected BASH or BATCH.");
                    return;
                }
                ProjectSetup ps = new ProjectSetup(new File(args[1]), args[2], args[3], args[4], bashOrBatch);
                ps.setup();
            }else{
                LOG.error("Invalid operation: " + operation + ". Valid operations include: launch, setup");
                ErrorCodes.INVALID_ARGS.fail();
            }
        }catch(Throwable t) {
            LOG.error("\rError Encountered: " + t.getClass().getName());
            t.printStackTrace(System.out);
            ErrorCodes.UNKNOWN_ERROR.fail();
        }
    }
}
