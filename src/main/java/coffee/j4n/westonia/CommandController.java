package coffee.j4n.westonia;

import coffee.j4n.westonia.commands.Fly;
import coffee.j4n.westonia.interfaces.commands.IWstCommand;
import coffee.j4n.westonia.utils.messages.management.MessageHelpers;
import coffee.j4n.westonia.utils.statics.constants.Prefixes;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * The CommandController class is responsible for handling all commands of the Westonia plugin.
 * It registers all commands and forwards the command execution to the corresponding command class.
 */
public class CommandController implements CommandExecutor {

    private final Westonia plugin;
    private Fly cmdFly;

    /**
     * Creates a new instance of the CommandController
     * That class should only be instantiated once.
     *
     * @param plugin The main class of the plugin
     */
    public CommandController(Westonia plugin) {
        this.plugin = plugin;
        this.registerCommands();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        MiniMessage miniMessage = MiniMessage.miniMessage();
        String commandName = command.getName();

        // Register console commands here

        if (!(sender instanceof Player player)) {
            Bukkit.getServer().getConsoleSender().sendMessage(Prefixes.ERROR.append(MessageHelpers.getMiniMessage().deserialize("This command can <u>only</u> be executed by a <gold>player</gold>!")));
            return false;
        }

        BasePlayer basePlayer = plugin.getPlayerHandler().getPlayer(player.getUniqueId());

        // Register player commands here
        if (commandName.equalsIgnoreCase(this.cmdFly.getName())) {
            return this.cmdFly.onCommand(basePlayer, command, args, this.plugin);
        }
        return false;
    }

    /**
     * Registers all commands of the Westonia plugin
     */
    private void registerCommands() {
        // /Fly
        this.cmdFly = new Fly();
        this.registerCommand(this.cmdFly);
    }

    /**
     * Registers a command
     *
     * @param commandToRegister Command that should be registered
     * @param <T> Type of the command
     */
    private <T extends IWstCommand> void registerCommand(T commandToRegister) {
        if (commandToRegister.getName() == null || commandToRegister.getName().isBlank()) {
            Bukkit.getServer().getConsoleSender().sendMessage(Prefixes.ERROR.append(MessageHelpers.getMiniMessage().deserialize("\"/<aqua>" + commandToRegister.getName() + "</aqza>\" could <red>not</red> be registered!").appendNewline().append(Prefixes.ARROWS_POINTING_RIGHT).append(
                    MessageHelpers.getMiniMessage().deserialize("The command name is <red>empty</red> in the command class \"<aqua>" + commandToRegister.getClass().getSimpleName() + "<gray>\"."))));
            return;
        }

        Bukkit.getServer().getConsoleSender().sendMessage(Prefixes.INFO.append(MiniMessage.miniMessage().deserialize("Registering command \"/<aqua>" + commandToRegister.getName() + "</aqua>\"...")));

        PluginCommand pluginCommand = this.plugin.getCommand(commandToRegister.getName());

        if (pluginCommand == null) {
            Bukkit.getServer().getConsoleSender().sendMessage(Prefixes.ERROR.append(
                    MessageHelpers.getMiniMessage().deserialize("The command \"/<aqua>" + commandToRegister.getName() + "</aqua>\" could not be registered.")).appendNewline().append(Prefixes.ARROWS_POINTING_RIGHT).append(
                    MessageHelpers.getMiniMessage().deserialize("The PluginCommand object is <red>null</red>!")).appendNewline().appendNewline().append(Prefixes.ARROWS_POINTING_RIGHT).append(
                    MessageHelpers.getMiniMessage().deserialize("Please check the following:")).appendNewline().append(Prefixes.ARROWS_POINTING_RIGHT).append(
                    MessageHelpers.getMiniMessage().deserialize("1. The command name in the command class \"<aqua>" + commandToRegister.getClass().getSimpleName() + "</aqua>\" is <u>correct</u>.")).appendNewline().append(Prefixes.ARROWS_POINTING_RIGHT).append(
                    MessageHelpers.getMiniMessage().deserialize("2. The command name in the \"<aqua>plugin.yml</aqua>\" file is <u>correct</u>.")));
            return;
        }

        pluginCommand.setExecutor(this);
        pluginCommand.setTabCompleter(commandToRegister);

        Bukkit.getServer().getConsoleSender().sendMessage(Prefixes.INFO.append(MiniMessage.miniMessage().deserialize("The command \"/<aqua>" + commandToRegister.getName() + "</aqua>\" <gray>has been <green>successfully</green> registered.")));
    }
}
