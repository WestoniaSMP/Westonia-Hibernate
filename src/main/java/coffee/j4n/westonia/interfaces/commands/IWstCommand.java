package coffee.j4n.westonia.interfaces.commands;

import org.bukkit.command.TabCompleter;

/**
 * Interface for commands every command of the Westonia plugin should implement
 */
public interface IWstCommand extends TabCompleter {

    /**
     * Returns the name of the command, e.g. "help", "ban", "kick", ...
     *
     * @return The name of the command.
     */
    String getName();
}
