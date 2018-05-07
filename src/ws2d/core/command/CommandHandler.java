package ws2d.core.command;

import ws2d.init.Ws2D;
import ws2d.core.server.Server;
import ws2d.util.Logger;
import ws2d.util.Utils;
import java.util.Scanner;

/**
 * Handles the console input and the processing of commands. This class does manage
 * <code>System.in</code> and is reliant on its input.
 * 
 * @author Ian
 */
public class CommandHandler extends Thread {
    /**
     * The console input.
     */
    private final Scanner in;
    /**
     * The current running server instance.
     */
    private final Server server;
    /**
     * Whether or not the handler is running.
     */
    private volatile boolean run;
    
    /**
     * Constructs a new instance of <code>CommandHandler</code> with the current
     * running instance of the server.
     * 
     * @param server the current running instance of the server.
     */
    public CommandHandler(Server server) {
        this.in = new Scanner(System.in);
        this.server = server;
        this.run = true;
    }
    
    /**
     * Executes the command handler. This reads input from <code>System.in</code>
     * and processes it by matching it to a command (if possible), and executing
     * that command.
     */
    @Override
    public void run() {
        question();
        while(run) {
            String line = in.nextLine();
            String[] args = Utils.safeSplit(line.toLowerCase());
            if(args.length == 0) {
                question();
                continue;
            }
            Command cmd = Ws2D.getRegistry().getCommand(args[0]);
            if(cmd == null) {
                System.out.println("Invalid command.");
                question();
                continue;
            }
            try {
                cmd.execute(server, args);
            }catch(CommandException ex) {
                System.out.println(ex.getMessage());
            }catch(Throwable t) {
                System.out.println("There was an error while processing this command.");
            }
            question();
        }
    }
    
    /**
     * Stops the command handler.
     */
    @Override
    public void interrupt() {
        run = false;
        in.close();
        super.interrupt();
    }
    
    /**
     * Prompts the user of the console for a command by printing <code>"&gt; "</code>
     * to the console.
     */
    public void question() {
        Logger.print("> ");
    }
}
