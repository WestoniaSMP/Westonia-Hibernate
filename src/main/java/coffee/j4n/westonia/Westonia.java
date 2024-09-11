package coffee.j4n.westonia;

import coffee.j4n.westonia.database.daos.PlayerDao;
import coffee.j4n.westonia.utils.ConfigHandler;
import coffee.j4n.westonia.utils.runnables.RunnableManager;
import coffee.j4n.westonia.utils.config.MdlDatabaseConfig;
import coffee.j4n.westonia.utils.messages.MessageFactory;
import coffee.j4n.westonia.utils.messages.Messages;
import coffee.j4n.westonia.utils.messages.management.MessageHelpers;
import coffee.j4n.westonia.utils.players.PlayerHandler;
import coffee.j4n.westonia.utils.statics.constants.FilePaths;
import coffee.j4n.westonia.utils.statics.constants.Prefixes;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

/**
 * The main class of the Westonia plugin.
 */
public class Westonia extends JavaPlugin {

    private static Westonia instance;
    private PlayerHandler playerHandler;
    private PlayerDao playerDao;

    private MessageFactory messageFactory;
    private RunnableManager runnableManager;

    /**
     * Returns the instance of the Westonia plugin.
     */
    public static Westonia getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        this.runnableManager = new RunnableManager(this);
        Bukkit.getServer().getConsoleSender().sendMessage(MessageHelpers.getMiniMessage().deserialize("[=-=-=-=-=-=-=-=] </reset>").append(Prefixes.WESTONIA_RAW).append(MessageHelpers.getMiniMessage().deserialize("</reset> <gray>[=-=-=-=-=-=-=-=]")));

        // Get plugin.yml information
        String version = this.getDescription().getVersion();
        String apiVersion = this.getDescription().getAPIVersion();
        String name = this.getDescription().getName();
        String author = this.getDescription().getAuthors().getFirst();

        // send loading message
        Bukkit.getServer().getConsoleSender().sendMessage(Prefixes.WESTONIA_PREFIX.append(MessageHelpers.getMiniMessage().deserialize("Loading <aqua>" + name + "</aqua> <aqua>" + version + "</aqua><dark_gray>(</dark_gray>API-Version:<aqua>" + apiVersion + "</aqua> by <aqua>" + author + "</aqua>...")));

        Bukkit.getServer().getConsoleSender().sendMessage(Prefixes.INFO.append(MessageHelpers.getMiniMessage().deserialize("Initializing <aqua>PlayerHandler</aqua>...")));
        playerHandler = new PlayerHandler();

        Bukkit.getServer().getConsoleSender().sendMessage(Prefixes.INFO.append(MessageHelpers.getMiniMessage().deserialize("Initializing <aqua>MessageFactory</aqua>...")));
        this.messageFactory = new MessageFactory(Messages.class, Messages.LANGUAGE_NAME);

        ConfigHandler<MdlDatabaseConfig> databaseConfigConfigHandler = new ConfigHandler<>(new File(FilePaths.DATABASE_CONFIG), MdlDatabaseConfig.class);
        MdlDatabaseConfig databaseConfig = databaseConfigConfigHandler.getConfigModel();

        if (!databaseConfigConfigHandler.getFile().exists()) {
            try {
                Bukkit.getServer().getConsoleSender().sendMessage(Prefixes.INFO.append(MessageHelpers.getMiniMessage().deserialize("Creating \"<aqua>" + FilePaths.DATABASE_CONFIG + "</aqua>\"...")));

                //noinspection ResultOfMethodCallIgnored
                databaseConfigConfigHandler.getFile().createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            String filePath = databaseConfigConfigHandler.getFile().getAbsolutePath();
            databaseConfigConfigHandler.saveConfig();

            Bukkit.getServer().getConsoleSender().sendMessage(Prefixes.INFO.append(MessageHelpers.getMiniMessage().deserialize("Successfully created \"<aqua>" + FilePaths.DATABASE_CONFIG + "</aqua>\"! Please fill out the configuration and restart the server - it is located in \"<aqua>" + filePath + "</aqua>\".")));
            return;
        }

        this.playerDao = new PlayerDao(this, databaseConfig);

        CommandController commandController = new CommandController(this);
        this.getServer().getPluginManager().registerEvents(new coffee.j4n.westonia.listeners.PlayerJoinEvent(this), this);
        this.getServer().getPluginManager().registerEvents(new coffee.j4n.westonia.listeners.ChatEvent(this), this);

        Bukkit.getServer().getConsoleSender().sendMessage(Prefixes.WESTONIA_PREFIX.append(MessageHelpers.getMiniMessage().deserialize("Westonia and all of its components have been loaded <green>successfully</green>!")));

        if ((long) Bukkit.getOnlinePlayers().size() > 0) {
            Bukkit.getServer().getConsoleSender().sendMessage(Prefixes.WESTONIA_PREFIX.append(MessageHelpers.getMiniMessage().deserialize("Loading all online players...")));
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                playerHandler.addPlayer(new BasePlayer(onlinePlayer, getNetworkPlayerDao(), getRunnableManager()));
            }
            Bukkit.getServer().getConsoleSender().sendMessage(Prefixes.WESTONIA_PREFIX.append(MessageHelpers.getMiniMessage().deserialize("All online players have been loaded <green>successfully</green>!")));
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    /**
     * Returns the instance of the PlayerHandler.
     */
    public PlayerHandler getPlayerHandler() {
        return playerHandler;
    }

    /**
     * Returns the instance of the PlayerDao.
     */
    public PlayerDao getNetworkPlayerDao() {
        return playerDao;
    }

    /**
     * Returns the instance of the MessageFactory.
     */
    public MessageFactory getMessageFactory() {
        return messageFactory;
    }

    /**
     * Returns the instance of the RunnableManager.
     */
    public RunnableManager getRunnableManager() {
        return runnableManager;
    }

}