package coffee.j4n.westonia.commands;

import coffee.j4n.westonia.BasePlayer;
import coffee.j4n.westonia.Westonia;
import coffee.j4n.westonia.interfaces.commands.IPluginCommandExecutor;
import coffee.j4n.westonia.utils.messages.TimeParser;
import coffee.j4n.westonia.utils.statics.constants.TempDataKeys;
import com.google.common.base.Optional;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Gamemode  implements IPluginCommandExecutor<BasePlayer, Westonia> {

    private BasePlayer basePlayer;

    /**
     * Returns the name of the command, e.g. "help", "ban", "kick", ...
     *
     * @return The name of the command.
     */
    @Override
    public String getName() {
        return "";
    }

    /**
     * Executes the command
     *
     * @param basePlayer The player who executed the command
     * @param command    The command that was executed
     * @param arguments  The arguments that were passed with the command
     * @param plugin     The main class of the plugin
     * @return true if the command was executed successfully, false otherwise
     */
    @Override
    public Boolean onCommand(BasePlayer basePlayer, Command command, String[] arguments, Westonia plugin) {
        this.basePlayer = basePlayer;
        String cmd = command.getName();

        if (cmd.equalsIgnoreCase("gamemeode") || cmd.equalsIgnoreCase("gm")) {

            if (arguments.length == 0) {
                //basePlayer.sendInfoMessage("Bitte gib einen Spielmodus an.");
                return false;
            }

        } else if (cmd.equalsIgnoreCase("gmc")) {

        } else if (cmd.equalsIgnoreCase("gms")) {

        } else if (cmd.equalsIgnoreCase("gmsp")) {

        } else if (cmd.equalsIgnoreCase("gma")) {

        }


        return null;
    }

    /**
     * Requests a list of possible completions for a command argument.
     *
     * @param sender  Source of the command.  For players tab-completing a
     *                command inside of a command block, this will be the player, not
     *                the command block.
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    The arguments passed to the command, including final
     *                partial argument to be completed
     * @return A List of possible completions for the final argument, or null
     * to default to the command executor
     */
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return List.of();
    }


    // <editor-fold defaultstate="collapsed" desc="Gamemode specific functions">
    /**
     * Changes the gamemode of the player to the specified gamemode.
     *
     * @param gameMode The new gamemode.
     */
    public void changeGameMode(GameMode gameMode) {
        changeGamemode(gameMode, "", null);
    }

    /**
     * Changes the gamemode of the player to the specified gamemode for the specified time.
     *
     * @param gameMode The new gamemode.
     * @param timeString The time as a string in the format "2d1h30m20s", empty string for no time limit.
     */
    public void changeGameMode(GameMode gameMode, String timeString) {
        changeGamemode(gameMode, timeString, null);
    }

    /**
     * Changes the gamemode of the player to the specified gamemode, with the specified command sender.
     *
     * @param gameMode The new gamemode.
     * @param commandSender The player who executed the command in case of a player changes another player's gamemode.
     */
    public void changeGameMode(GameMode gameMode, BasePlayer commandSender) {
        changeGamemode(gameMode, "", commandSender);
    }

    /**
     * Changes the gamemode of the player.
     *
     * @param gameMode The new gamemode.
     * @param timeString The time as a string in the format "2d1h30m20s" - empty string for no time limit.
     * @param commandSender The player who executed the command in case of a player changes another player's gamemode.
     *
     * @return true if the gamemode was changed successfully, false otherwise.
     */
    public boolean changeGamemode(GameMode gameMode, String timeString, BasePlayer commandSender) {
        GameMode previousGameMode = this.basePlayer.getPlayer().getGameMode();
        Optional<Long> timeInSeconds = TimeParser.parseTimeToSeconds(timeString);

        if (!timeInSeconds.isPresent()) {
            //this.basePlayer.sendErrorMessage("Ungültige Zeitangabe.");
            return false;
        }

        if (commandSender == null) {
            this.basePlayer.getPlayer().setGameMode(gameMode);
        }

        return true;
    }
    // </editor-fold>

}
