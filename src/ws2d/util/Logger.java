package ws2d.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The logger class for Ws2D.
 * 
 * @author Ian
 */
public final class Logger {
    /**
     * Used to format the time.
     */
    private static final SimpleDateFormat DATETIME = new SimpleDateFormat("HH:mm:ss");
    /**
     * The name of the logger.
     */
    private final String name;
    
    /**
     * Constructs a new instance of <code>Logger</code> with a specified name.
     * 
     * @param name the name of the logger.
     */
    public Logger(String name) {
        this.name = name;
    }
    
    /**
     * Formats a raw message for logging.
     * 
     * @param msg the message.
     * @param lvl the priority level.
     * @param prompt whether or not to prompt the user for a command.
     * @return the formatted message.
     */
    private String formatMessage(String msg, String lvl) {
        return '\r' + DATETIME.format(new Date()) + " [" + name + '/' + lvl + "] " + msg + "\n> ";
    }
    
    /**
     * Prints an object.
     * 
     * @param x the object to print.
     */
    public static void print(Object x) {
        System.out.print('\r' + x.toString());
    }
    
    /**
     * Prints an object and starts a new line.
     * 
     * @param x the object to print.
     */
    public static void println(Object x) {
        System.out.println('\r' + x.toString());
    }
    
    /**
     * Prints the object without modifying the message.
     * 
     * @param x the object to print.
     */
    public static void printDirect(Object x) {
        System.out.print(x.toString());
    }
    
    /**
     * Prints the object without modifying the message and starts a new line.
     * 
     * @param x the object to print.
     */
    public static void printlnDirect(Object x) {
        System.out.println(x.toString());
    }
    
    /**
     * Prints the message with the <code>INFO</code> tag.
     * 
     * @param msg the message to print.
     */
    public void info(String msg) {
        System.out.print(formatMessage(msg, "INFO"));
    }
    
    /**
     * Prints the message with the <code>WARN</code> tag.
     * 
     * @param msg the message to print.
     */
    public void warn(String msg) {
        System.out.print(formatMessage(msg, "WARN"));
    }
    
    /**
     * Prints the throwable and with the notification <code>Encountered Exception:</code>
     * with the <code>WARN</code> tag.
     * 
     * @param t the throwable to print.
     */
    public void warn(Throwable t) {
        System.out.print(formatMessage("Encountered Exception:", "WARN"));
        printThrowable(t);
    }
    
    /**
     * Prints the message to the console with the <code>WARN</code> tag, and then
     * prints the throwable.
     * 
     * @param msg the message to print.
     * @param t the throwable to print.
     */
    public void warn(String msg, Throwable t) {
        System.out.print(formatMessage(msg, "WARN"));
        printThrowable(t);
    }
    
    /**
     * Prints the message with the <code>ERROR</code> tag.
     * 
     * @param msg the message to print.
     */
    public void error(String msg) {
        System.out.print(formatMessage(msg, "ERROR"));
    }
    
    /**
     * Prints the throwable and with the notification <code>Encountered Exception:</code>
     * with the <code>ERROR</code> tag.
     * 
     * @param t the throwable to print.
     */
    public void error(Throwable t) {
        System.out.print(formatMessage("Encountered Exception:", "ERROR"));
        printThrowable(t);
    }
    
    /**
     * Prints the message to the console with the <code>ERROR</code> tag, and then
     * prints the throwable.
     * 
     * @param msg the message to print.
     * @param t the throwable to print.
     */
    public void error(String msg, Throwable t) {
        System.out.print(formatMessage(msg, "ERROR"));
        printThrowable(t);
    }
    
    /**
     * Prints a throwable to the console.
     * 
     * @param t the throwable to print.
     */
    public static void printThrowable(Throwable t) {
        println(t.getClass().getName() + ": " + t.getMessage());
        for(StackTraceElement ste : t.getStackTrace())
            System.out.println('\t' + ste.toString());
    }
}
