package ws2d.init;

import ws2d.core.command.Command;
import ws2d.core.network.Packet;
import ws2d.core.network.PacketData;
import ws2d.core.network.PacketHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * The object registry for Ws2D. All variable sets of objects, including packets,
 * commands and more, are stored here.
 * 
 * @author Ian
 */
public class Registry {
    /**
     * The registered packets.
     */
    private final List<PacketData<Packet>> packets;
    /**
     * The registered commands.
     */
    private final List<Command> commands;
    
    /**
     * Constructs a new instance of <code>Registry</code>.
     */
    Registry() {
        this.packets = new ArrayList<>();
        this.commands = new ArrayList<>();
    }
    
    /**
     * Registers a new packet.
     * 
     * @param packetClass the packet's class.
     * @param handler the packet's handler.
     * @param <T> the packet type.
     */
    public <T extends Packet> void registerPacket(Class<T> packetClass, PacketHandler<T> handler) {
        Objects.requireNonNull(packetClass, "The specified packet class cannot be null.");
        packets.stream().map((pd) -> {
            if(packetClass.equals(pd.packetClass))
                throw new IllegalArgumentException("This packet class has already been registered.");
            return pd;
        }).filter((pd) -> (pd.id == packets.size())).forEachOrdered((_item) -> {
            throw new InternalError("A duplicate packet ID was found. This error should never happen, please report this to the maintainers of this build.");
        });
        if(packets.size() == 0x100)
            throw new RuntimeException("The number of available packet slots has been exceeded: 256.");
        packets.add(new PacketData<>((Class<Packet>)packetClass, (PacketHandler<Packet>)handler, packets.size()));
    }
    
    /**
     * Returns an immutable list of the registered packets.
     * 
     * @return an immutable list of the registered packets.
     */
    public List<PacketData<Packet>> getRegisteredPackets() {
        return Collections.unmodifiableList(packets);
    }
    
    /**
     * Registers a new command.
     * 
     * @param command the command to register.
     */
    public void registerCommand(Command command) {
        Objects.requireNonNull(command, "The specified command cannot be null.");
        commands.stream().map((c) -> {
            if(command.getClass().equals(c.getClass()))
                throw new IllegalArgumentException("This command has already been registered.");
            return c;
        }).forEachOrdered((c) -> {
            command.getAliases().stream().filter((alias) -> (c.getAliases().contains(alias))).forEachOrdered((alias) -> {
                throw new IllegalArgumentException("Duplicate alias in " + command.getClass().getName() + ": \"" + alias + "\"");
            });
        });
        commands.add(command);
    }
    
    /**
     * Returns an immutable list of the registered commands.
     * 
     * @return an immutable list of the registered commands.
     */
    public List<Command> getRegisteredCommands() {
        return Collections.unmodifiableList(commands);
    }
    
    /**
     * Returns a command that matches the given string.
     * 
     * @param cmd the string to match to.
     * @return a command that matches the given string, or null if a match does
     * not exist.
     */
    public Command getCommand(String cmd) {
        for(Command c : commands) {
            if(c.getAliases().contains(cmd.toLowerCase()))
                return c;
        }
        return null;
    }
}
