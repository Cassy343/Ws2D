package ws2d.init.libs;

import java.io.File;
import java.io.IOException;
import ws2d.util.FileSystem;

/**
 * This class manages the libraries for Ws2D. If it finds that any libraries are
 * missing it will unpack them from the jar.
 * 
 * @author Ian
 */
public final class Libraries {
    /**
     * The required libraries for Ws2D.
     */
    private static final String[] LIBS =
    {
        "aether-api-1.1.0.jar", "netty-buffer-4.1.8.Final.jar",
        "netty-codec-4.1.8.Final.jar", "netty-codec-dns-4.1.8.Final.jar",
        "netty-codec-http2-4.1.8.Final.jar", "netty-codec-http-4.1.8.Final.jar",
        "netty-codec-socks-4.1.8.Final.jar", "netty-common-4.1.8.Final.jar",
        "netty-handler-4.1.8.Final.jar", "netty-handler-proxy-4.1.8.Final.jar",
        "netty-resolver-4.1.8.Final.jar", "netty-resolver-dns-4.1.8.Final.jar",
        "netty-transport-4.1.8.Final.jar", "stg-utils-1.0.5-final.jar",
        "vertx-core-3.4.2.jar"
    };
    
    /**
     * Hidden as it has no use.
     */
    private Libraries() { }
    
    /**
     * Checks for any missing libraries, and unpacks them if necessary.
     * 
     * @throws IOException if an I/O error occurs.
     */
    public static void initializeLibs() throws IOException {
        File libsDir = new File("." + File.separator + "lib");
        if(!libsDir.exists())
            libsDir.mkdirs();
        for(String lib : LIBS) {
            File libFile = new File("lib" + File.separator + lib);
            if(!libFile.exists())
                FileSystem.copy(Libraries.class.getResourceAsStream(lib), libFile);
        }
    }
}
