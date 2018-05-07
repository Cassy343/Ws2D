package ws2d.init;

/**
 * An initialization state: see the enumerated values for details.
 * 
 * @author Ian
 */
public enum InitializationState {
    /**
     * The very first stage of initialization where the platform is just launching.
     */
    BOOT,
    /**
     * The pre-initialization stage is where loggers are finalized and where external
     * server communications can take place.
     */
    PRE_INIT,
    /**
     * The initialization stage is where all game object are registered.
     */
    INIT,
    /**
     * The post-initialization stage is where more external connections can take
     * place as well as any changes in on-disk data after initialization.
     */
    POST_INIT,
    /**
     * Marks the end of all initialization stages.
     */
    FINISHED;
    
    /**
     * The enumerated values in an array.
     */
    public static final InitializationState[] VALUES = values();
}
