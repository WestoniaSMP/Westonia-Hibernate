package coffee.j4n.westonia.utils.players;

import coffee.j4n.westonia.BasePlayer;
import io.papermc.paper.annotation.DoNotUse;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The PlayerHandler is used to manage the BasePlayers of the server and to provide easy access to them.
 */
public class PlayerHandler {

    /**
     * Stores all players of the server, wrapped in the BasePlayer class.
     */
    private final Map<UUID, BasePlayer> players = new HashMap<>();

    /**
     * Adds a BasePlayer to the the PLayerHandler.
     *
     * @param playerToBeAdded The player to be added.
     */
    public void addPlayer(@NotNull BasePlayer playerToBeAdded) {
        players.put(playerToBeAdded.getUniqueId(), playerToBeAdded);
    }
    // region Get

    /**
     * Returns the BasePlayer with the given UUID.
     * The player must be online to be returned.
     *
     * @param uuid The UUID of the BasePlayer.
     * @return The BasePlayer with the given UUID.
     */
    @Nullable
    public BasePlayer getPlayer(@NotNull UUID uuid) {
        return players.get(uuid);
    }

    /**
     * Returns the BasePlayer based on the player name.
     *
     * @param name The name of the player that is searched for.
     * @return The BasePlayer with the given name.
     */
    @Nullable
    public BasePlayer getPlayer(String name) {
        for (BasePlayer player : players.values()) {
            if (player.getPlayer().getName().equalsIgnoreCase(name)) {
                return player;
            }
        }
        return null;
    }

    /**
     * Removes a player from the PlayerHandler.
     *
     * @param player The player to be removed.
     */
    public void removePlayer(BasePlayer player) {
        players.remove(player.getUniqueId());
    }

    /**
     * Entfernt einen Spieler aus der Spielerliste anhand der UUID.
     */
    public void removePlayer(UUID uuid) {
        players.remove(uuid);
    }

    /**
     * Entfernt einen Spieler aus der Spielerliste anhand des Spielernamen.
     */
    public void removePlayer(String name) {
        for (BasePlayer player : players.values()) {
            if (player.getPlayer().getName().equalsIgnoreCase(name)) {
                players.remove(player.getUniqueId());
            }
        }
    }
    // endregion
}