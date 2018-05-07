package ws2d.core.command;

/**
 * This class defines an exception that is to be thrown if an error occurs while
 * processing a command.
 * 
 * @author Ian
 */
public class CommandException extends Exception {
    /**
     * Constructs a new instance of <code>CommandException</code> with a detail
     * message.
     * 
     * @param msg the detail message.
     */
    public CommandException(String msg) {
        super(msg);
    }
    
    /**
     * Constructs a new instance of <code>CommandException</code> without a detail
     * message.
     */
    public CommandException() {
        super();
    }
}
