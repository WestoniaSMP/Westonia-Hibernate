package coffee.j4n.westonia.interfaces.commands;

import org.bukkit.command.Command;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Interface for plugin command executors, including the BasePlayer and the PluginInstance.
 * @param <BasePlayer> The BasePlayer class of the plugin
 * @param <MainClass> The main class of the plugin
 */
public interface IPluginCommandExecutor<BasePlayer, MainClass extends JavaPlugin> extends IWstCommand {

    /**
     * Executes the command
     * @param basePlayer The player who executed the command
     * @param command The command that was executed
     * @param arguments The arguments that were passed with the command
     * @param plugin The main class of the plugin
     * @return true if the command was executed successfully, false otherwise
     */
    Boolean onCommand(BasePlayer basePlayer, Command command, String[] arguments, MainClass plugin);
}
