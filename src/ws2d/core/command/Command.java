package ws2d.core.command;

import ws2d.core.server.Server;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The base class for all server commands in Ws2D.
 * 
 * @author Ian
 */
public abstract class Command {
    /**
     * All string representations of this command that are valid on the command
     * line.
     */
    protected final List<String> aliases;
    
    /**
     * Constructs a new instance of <code>Command</code> with the specified aliases.
     * There must be at least one alias or an <code>IllegalArgumentException</code>
     * will be thrown.
     * 
     * @param aliases the command aliases.
     */
    protected Command(String... aliases) {
        if(aliases.length == 0)
            throw new IllegalArgumentException("A command must have at least one alias.");
        this.aliases = new ArrayList<>(aliases.length);
        for(String alias : aliases)
            this.aliases.add(alias.toLowerCase());
    }
    
    /**
     * Returns the aliases for this command.
     * 
     * @return the aliases for this command.
     */
    public final List<String> getAliases() {
        return Collections.unmodifiableList(aliases);
    }
    
    /**
     * Executes this command. The current running server instance and any given
     * arguments are supplied. A <code>CommandException</code> should be thrown
     * if any given arguments are invalid or if an invalid state is reached.
     * 
     * @param server the current running server instance.
     * @param args the arguments for the command.
     * 
     * @throws CommandException if an invalid state is reached or any arguments
     * are invalid.
     */
    public abstract void execute(Server server, String[] args) throws CommandException;
}
