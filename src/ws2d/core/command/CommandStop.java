package ws2d.core.command;

import ws2d.init.Ws2D;
import ws2d.core.server.Server;

/**
 * Stops the current running instance of Ws2D.
 * 
 * @author Ian
 */
public class CommandStop extends Command {
    /**
     * Constructs a new instance of <code>CommandStop</code>.
     */
    public CommandStop() {
        super("stop", "shutdown");
    }
    
    /**
     * Stops the current running instance of Ws2D. Command format: <code>stop</code>.
     * 
     * @param server the current running instance of the server.
     * @param args the arguments for the command.
     * 
     * @throws CommandException if an invalid state is reached or any arguments
     * are invalid.
     */
    @Override
    public void execute(Server server, String[] args) throws CommandException {
        Ws2D.getInstance().shutdown();
    }
}
