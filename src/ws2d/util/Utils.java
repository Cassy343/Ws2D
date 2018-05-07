package ws2d.util;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.buffer.impl.BufferFactoryImpl;
import io.vertx.core.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * A general utilities class for Ws2D.
 * 
 * @author Ian
 */
public final class Utils {
    /**
     * The vert.x instance.
     */
    public static final Vertx VERTX = Vertx.vertx();
    
    /**
     * Private as it has no use.
     */
    private Utils() { }
    
    /**
     * Returns whether or not two socket addresses are exactly equal, but not necessarily
     * the same object.
     * 
     * @param a the first socket address.
     * @param b the second socket address.
     * @return <code>true</code>, if the socket addresses are equal, <code>false</code>
     * otherwise.
     */
    public static boolean socketAddressEquals(SocketAddress a, SocketAddress b) {
        return a.host().equals(b.host()) && a.port() == b.port();
    }
    
    /**
     * Turns a byte array to a vert.x buffer.
     * 
     * @param bytes the bytes to buffer.
     * @return a vert.x buffer of the given bytes.
     */
    public static Buffer buffer(byte[] bytes) {
        return (new BufferFactoryImpl()).buffer(bytes);
    }
    
    /**
     * Splits a string by spaces while keeping quotes in mind. I.E. if a string
     * is in quotes, it is treated as a single element.
     * 
     * @param string the string to split.
     * @return an array of the given string split by spaces.
     */
    public static String[] safeSplit(String string) {
        int flags = 0x0;
        List<String> args = new ArrayList<>();
        StringBuilder carg = new StringBuilder();
        char[] c = string.toCharArray();
        for(int i = 0;i < string.length();++ i) {
            if(c[i] == '\\') {
                flags |= 0x1;
                continue;
            }
            if(flags != 0) {
                if((flags & 0x1) != 0)
                    flags &= ~0x1;
                carg.append(c[i]);
                continue;
            }
            if(c[i] == '\"') {
                flags ^= 0x2;
                continue;
            }else if(c[i] == '\'') {
                flags ^= 0x4;
                continue;
            }
            if(c[i] <= 32) {
                String arg = carg.toString().trim();
                if(!arg.isEmpty()) {
                    args.add(arg);
                    carg.setLength(0);
                }
                continue;
            }
            carg.append(c[i]);
        }
        String arg = carg.toString().trim();
        if(!arg.isEmpty())
            args.add(arg);
        return args.toArray(new String[args.size()]);
    }
}
