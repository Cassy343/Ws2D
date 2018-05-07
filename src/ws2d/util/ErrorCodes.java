package ws2d.util;

import ws2d.init.Ws2D;

/**
 * Defines all errors that can occur in Ws2D.
 * 
 * @author Ian
 */
public enum ErrorCodes {
    /**
     * Invalid command line arguments.
     */
    INVALID_ARGS,
    /**
     * Invalid game file specified.
     */
    INVALID_GAME_FILE,
    /**
     * An unknown error occurred.
     */
    UNKNOWN_ERROR;
    
    /**
     * Terminated the current running instance of Ws2D.
     */
    public void fail() {
        Ws2D.getInstance().shutdown();
        System.err.println("\rRUN FAILED. Error Code: " + (ordinal() + 1) + " (" + name().toUpperCase() + ")");
        System.exit(ordinal() + 1);
    }
}
