package coffee.j4n.westonia.interfaces.commands;

import coffee.j4n.westonia.BasePlayer;
import org.bukkit.command.Command;

/**
 * Interface for basic command executors, including the BasePlayer.
 */
public interface IBasicCommandExecutor extends IWstCommand {

    /**
     * Executes the command
     *
     * @param basePlayer The player who executed the command
     * @param command    The command that was executed
     * @param arguments  The arguments that were passed with the command
     * @return true if the command was executed successfully, false otherwise
     */
    Boolean onCommand(BasePlayer basePlayer, Command command, String[] arguments);
}
