package coffee.j4n.westonia;

import coffee.j4n.westonia.database.daos.PlayerDao;
import coffee.j4n.westonia.database.entities.WstPlayer;
import coffee.j4n.westonia.database.results.DbResult;
import coffee.j4n.westonia.database.results.DbReturn;
import coffee.j4n.westonia.utils.Helpers;
import coffee.j4n.westonia.utils.messages.Messages;
import coffee.j4n.westonia.utils.messages.management.MessageHelpers;
import coffee.j4n.westonia.utils.runnables.RunnableManager;
import coffee.j4n.westonia.utils.statics.constants.Permissions;
import coffee.j4n.westonia.utils.statics.constants.Prefixes;
import coffee.j4n.westonia.utils.statics.enums.ResultType;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The BasePlayer class is a wrapper for the org.bukkit.entity.Player class.
 * It provides additional functionality and simplifies database operations for player data.
 * <br/><br/>
 * The BasePlayer class is used to handle player-specific operations within the Westonia plugin and should be used for all player-related tasks.
 */
@SuppressWarnings("unused")
public class BasePlayer {

    private final Player player;
    private final PlayerDao playerDao;
    private final RunnableManager runnableManager;
    private final Map<String, UUID> timers = new HashMap<>();
    private final Map<String, Object> tempData = new HashMap<>();


    // <editor-fold defaultstate="collapsed" desc="Constructors, Initialization and Fields">
    private String currentLocale;
    private boolean isInitialized = false;

    /**
     * Creates a new BasePlayer object with the specified player and PlayerDao.
     * The player must be online for this constructor to work and you should use the init() method to initialize the player synchronously.
     *
     * @param player    The player object.
     * @param playerDao The PlayerDao to use for database operations.
     */
    public BasePlayer(Player player, PlayerDao playerDao, RunnableManager runnableManager) {
        this.player = player;
        this.playerDao = playerDao;
        this.runnableManager = runnableManager;
        ;

        this.currentLocale = player.locale().toString().substring(0, 2);
    }


    /**
     * Creates a new BasePlayer object with the specified player UUID and PlayerDao.
     * The player must be online for this constructor to work and you should use the init() method to initialize the player synchronously.
     *
     * @param playerUUID The UUID of the player.
     * @param playerDao  The PlayerDao to use for database operations.
     */
    public BasePlayer(UUID playerUUID, PlayerDao playerDao, RunnableManager runnableManager) {
        this.player = Bukkit.getServer().getPlayer(playerUUID);

        if (this.player == null) {
            throw new IllegalArgumentException("Player with UUID " + playerUUID + " is not online.");
        }

        this.playerDao = playerDao;
        this.runnableManager = runnableManager;

        this.currentLocale = player.locale().toString().substring(0, 2);
    }

    /**
     * Initializes the BasePlayer synchronously.
     */
    public void init() {
        isInitialized = false;
        DbReturn playerRegisteredResult = this.playerDao.isPlayerRegistered(this.player.getUniqueId().toString());
        if (playerRegisteredResult.getResultType() == ResultType.ERROR) {
            Helpers.runOnMainThread(() -> {
                Component kickMessage = getMessageFromKey(Messages.COMMON_HEADER, Prefixes.WESTONIA_RAW_STRING)
                        .append(MessageHelpers.getMiniMessage().deserialize(
                                "<gray><newline><newline>Ein <red>Fehler</red> ist aufgetreten, während deine Spieler-Daten geladen wurden.<newline>Die <dark_purple>Datenbank</dark_purple> scheint <red>nicht</red> erreichbar zu sein.<newline><newline>Versuche es bitte <aqua>später</aqua> erneut.<newline><newline><aqua>▶</aqua> Sollte der <red>Fehler</red> weiterhin auftreten, gebe bitte <gold>J4N</gold> / <gold>Jan</gold> bescheid :) <aqua>◀</aqua>"));
                this.player.kick(kickMessage);
            });
            return;
        }

        if (playerRegisteredResult.getResultType() == ResultType.NOT_FOUND) {
            DbReturn registerPlayerResult = this.playerDao.registerPlayer(new WstPlayer(this));

            if (!registerPlayerResult.isSuccessful()) {
                Helpers.runOnMainThread(() -> {
                    Component kickMessage = getMessageFromKey(Messages.COMMON_HEADER, Prefixes.WESTONIA_RAW_STRING)
                            .append(MessageHelpers.getMiniMessage().deserialize(
                                    "<gray><newline><newline>Ein <red>Fehler</red> ist aufgetreten, während deine Spieler-Daten angelegt wurden.<newline><newline>Versuche es bitte erneut.<newline><newline><aqua>▶</aqua> Sollte der <red>Fehler</red> weiterhin auftreten, gebe bitte <gold>J4N</gold> / <gold>Jan</gold> bescheid :) <aqua>◀</aqua>"));
                    this.player.kick(kickMessage);
                });
                return;
            }

            // Proceed to load the player data after registration
            loadPlayerData();
            isInitialized = true;
        } else {
            // Player is already registered, proceed to load player data
            loadPlayerData();
            isInitialized = true;
        }
    }

    /**
     * Loads the player data Synchronously.
     */
    private void loadPlayerData() {
        DbResult<WstPlayer> networkPlayerResult = this.playerDao.getPlayer(this.player.getUniqueId().toString());
        if (!networkPlayerResult.isSuccessful()) {
            Helpers.runOnMainThread(() -> {
                Component kickMessage = getMessageFromKey(Messages.COMMON_HEADER, Prefixes.WESTONIA_RAW_STRING)
                        .append(MessageHelpers.getMiniMessage().deserialize(
                                "<gray><newline><newline>Ein <red>Fehler</red> ist aufgetreten, während deine Spieler-Daten geladen wurden.<newline><newline>Versuche es bitte erneut.<newline><newline><aqua>▶</aqua> Sollte der <red>Fehler</red> weiterhin auftreten, gebe bitte <gold>J4N</gold> / <gold>Jan</gold> bescheid :) <aqua>◀</aqua>"));
                this.player.kick(kickMessage);
            });
            return;
        }

        WstPlayer WstPlayer = networkPlayerResult.getResult();
        if (WstPlayer != null) {
            this.currentLocale = WstPlayer.getLanguage();
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Temporary data">

    /**
     * Sets temporary data that can be accessed later.
     *
     * @param dataName The name of the data to set.
     * @param value The value to set.
     */
    public void setTempData(String dataName, Object value) {
        this.tempData.put(dataName, value);
    }

    /**
     * Gets temporary data that was set using setTempData.
     *
     * @param dataName The name of the data to get.
     * @return The data that was set using setTempData.
     */
    public Object getTempData(String dataName) {
        return this.tempData.get(dataName);
    }

    /**
     * Removes temporary data that was set using setTempData.
     *
     * @param dataName The name of the data to remove.
     */
    public void removeTempData(String dataName) {
        this.tempData.remove(dataName);
    }


    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Timers, runnables, ...">

    /**
     * Starts a new countdown for the player.
     *
     * @param duration the duration of the countdown in seconds.
     * @param onTick   the task to execute every second during the countdown.
     * @param onFinish the task to execute when the countdown finishes.
     * @return the unique ID of the created countdown timer.
     */
    public UUID startCountdown(int duration, Runnable onTick, Runnable onFinish) {
        return runnableManager.startCountdown(getUniqueId(), duration, onTick, onFinish);
    }

    /**
     * Stops a specific countdown timer for the player using its unique timer ID.
     *
     * @param timerName The unique name of the countdown timer to stop.
     */
    public void stopCountdown(String timerName) {
        UUID timerId = this.timers.get(timerName);
        runnableManager.stopTimer(getUniqueId(), timerId);
        this.timers.remove(timerName);
    }

    /**
     * Gets the remaining time of a specific countdown timer for the player.
     *
     * @param timerName The unique name of the timer
     * @return the remaining time in seconds, or -1 if no countdown is found.
     */
    public int getRemainingCountdownTime(String timerName) {
        return runnableManager.getRemainingTime(getUniqueId(), this.timers.get(timerName));
    }

    /**
     * Stops all countdowns and timers associated with the player.
     */
    public void stopAllCountdowns() {
        runnableManager.stopAllTimers(getUniqueId());
    }

    /**
     * Starts a repeating timer for the player.
     *
     * @param delay  the initial delay before the timer starts, in ticks.
     * @param period the period between consecutive executions, in ticks.
     * @param task   the task to execute periodically.
     */
    public void startTimer(String name, long delay, long period, Runnable task) {
        this.timers.put(name, runnableManager.startTimer(getUniqueId(), delay, period, task));
    }

    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="Player specific functions">

    /**
     * Returns whether the player has been initialized.
     *
     * @return True if the player has been initialized, otherwise false.
     */
    public boolean isInitialized() {
        return isInitialized;
    }

    /**
     * Checks if the player has the specified permission.
     * Returns true if the player has the permission, otherwise false.
     * <br/> <br/>
     * <b>IMPORTANT:</b>
     * If the player has the permission "westonia.*" or "*", true is always returned.
     *
     * @param permissionToCheck The permission to check.
     * @return True if the player has the permission, otherwise false.
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean hasPermission(String permissionToCheck) {
        if (player.hasPermission("*") || player.hasPermission(Permissions.WESTONIA_ALL) || player.hasPermission(permissionToCheck)) {
            return true;
        }

        Component message = Prefixes.ERROR.append(getMessageFromKey(Messages.COMMON_NOPERMISSION, permissionToCheck));


        player.sendMessage(message);
        return false;
    }

    /**
     * Returns the org.bukkit.entity.Player object within the BasePlayer.
     *
     * @return The org.bukkit.entity.Player object within the BasePlayer.
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Returns the unique ID of the BasePlayer.
     *
     * @return The unique ID of the BasePlayer.
     */
    public UUID getUniqueId() {
        return this.player.getUniqueId();
    }
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="Language specific functions">

    /**
     * Returns the language code of the player.
     *
     * @return The language code of the player (e.g. "de").
     */
    public String getCurrentLocale() {
        return currentLocale;
    }

    /**
     * Sets the language of the player synchronously.
     *
     * @param currentLocale The language code to set (e.g. "de").
     */
    public void setCurrentLocale(String currentLocale) {
        this.currentLocale = currentLocale;

        if (!saveLanguage().isSuccessful()) {
            sendErrorMessage(Messages.PLAYER_LANGUAGE_ERROR_ON_SAVE);
            return;
        }

        sendInfoMessage(Messages.PLAYER_LANGUAGE_SAVED, getLanguageHumanFriendly());
    }

    /**
     * Gets the language of the player in a human-friendly format.
     *
     * @return The human-friendly language of the player (e.g. "Deutsch").
     */
    public String getLanguageHumanFriendly() {
        return Westonia.getInstance().getMessageFactory().getLanguageFromCode(this.currentLocale);
    }
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="Message functions">

    /**
     * Sends a message to the player.
     *
     * @param key        The key of the message to send.
     * @param parameters The parameters to replace within the message.
     */
    public void sendSpecialMessage(Messages key, String... parameters) {
        player.sendMessage(getMessageFromKey(key, parameters));
    }

    /**
     * Sends a message to the player with the Westonia prefix.
     *
     * @param key        The key of the message to send.
     * @param parameters The parameters to replace within the message.
     */
    public void sendWestoniaMessage(Messages key, String... parameters) {
        player.sendMessage(Prefixes.WESTONIA_PREFIX.append(getMessageFromKey(key, parameters)));
    }

    /**
     * Sends a message to the player with the specified gradient.
     *
     * @param key        The key of the message to send.
     * @param parameters The parameters to replace within the message.
     */
    public void sendArrowMessage(Messages key, String... parameters) {
        player.sendMessage(Prefixes.ARROWS_POINTING_RIGHT.append(getMessageFromKey(key, parameters)));
    }

    /**
     * Sends an info message to the player.
     *
     * @param key        The key of the message to send.
     * @param parameters The parameters to replace within the message.
     */
    public void sendInfoMessage(Messages key, String... parameters) {
        player.sendMessage(Prefixes.INFO.append(getMessageFromKey(key, parameters)));
    }

    /**
     * Sends a warning message to the player.
     *
     * @param key        The key of the message to send.
     * @param parameters The parameters to replace within the message.
     */
    public void sendWarnMessage(Messages key, String... parameters) {
        player.sendMessage(Prefixes.WARN.append(getMessageFromKey(key, parameters)));
    }

    /**
     * Sends an error message to the player.
     *
     * @param key        The key of the message to send.
     * @param parameters The parameters to replace within the message.
     */
    public void sendErrorMessage(Messages key, String... parameters) {
        player.sendMessage(Prefixes.ERROR.append(getMessageFromKey(key, parameters)));
    }

    /**
     * Sends a debug message to the player.
     *
     * @param message The message to send.
     */
    public void sendDebugMessage(String message) {
        player.sendMessage(Prefixes.DEBUG.append(MessageHelpers.getMiniMessage().deserialize(message)));
    }

    /**
     * Sends a message to the player with the specified gradient.
     *
     * @param key        The key of the message to send.
     * @param parameters The parameters to replace within the message.
     * @return The final processed message as a Component.
     */
    private Component getMessageFromKey(Messages key, String... parameters) {
        return Westonia.getInstance().getMessageFactory().getMessage(key, this.currentLocale, parameters);
    }
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="Database functions">

    /**
     * Synchronously retrieves the WstPlayer data from the database.
     *
     * @return A CompletableFuture that completes with the DbResult containing the WstPlayer or an error.
     */
    private DbResult<WstPlayer> getNetworkPlayer() {

        DbResult<WstPlayer> playerResult = this.playerDao.getPlayer(this.player.getUniqueId().toString());

        if (!playerResult.isSuccessful()) {
            return new DbResult<>(null, playerResult.getMessage(), playerResult.getResultType());
        }

        return new DbResult<>(playerResult.getResult(), "Player data loaded", ResultType.SUCCESS);
    }

    /**
     * Synchronously saves the language of the player to the database.
     *
     * @return A DbReturn containing the result of the operation.
     */
    private DbReturn saveLanguage() {
        return this.playerDao.updateLanguage(this.getUniqueId().toString(), this.currentLocale);
    }
    // </editor-fold>
}