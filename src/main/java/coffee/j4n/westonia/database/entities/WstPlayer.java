package coffee.j4n.westonia.database.entities;

import jakarta.persistence.*;
import coffee.j4n.westonia.database.converter.UUIDConverter;
import coffee.j4n.westonia.BasePlayer;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Entity class for the player table.
 */
@Entity
@Table(name = "westonia_player")
public class WstPlayer extends BaseEntity implements Serializable {

    /**
     * The Minecraft UUID of the player.
     * This field is unique and not nullable.
     * Default value is a random UUID.
     */
    @Column(name = "minecraft_uuid", unique = true, nullable = false)
    @Convert(converter = UUIDConverter.class)
    @NotNull
    public UUID minecraftUUID = UUID.randomUUID();

    /**
     * The date, on which the player first joined the server.
     * This field is not nullable.
     * Default value is the current date.
     */
    @Column(name = "first_join_date", nullable = false)
    @NotNull
    private Date firstJoinDate = new Date();

    /**
     * The currently selected language of the player.
     * The language is stored as a two-letter language code and is used in all systems associated with Westonia.
     * This field is not nullable.
     * Default value is "de", because the server is mainly German.
     */
    @Column(name = "global_language", nullable = false)
    @NotNull
    private String language = "de";

    /**
     * Default constructor, required by Hibernate.
     * Initializes the minecraftUUID with an empty string.
     */
    public WstPlayer() {}

    public WstPlayer(@NotNull BasePlayer basePlayer) {
        this.minecraftUUID = basePlayer.getPlayer().getUniqueId();
        this.language = basePlayer.getPlayer().locale().getLanguage();
    }

    // region Getter und Setter
    /**
     * Returns the UUID of the player.
     */
    public @NotNull UUID getMinecraftUUID() {
        return minecraftUUID;
    }

    /**
     * Returns the date, on which the player first joined the server.
     *
     * @return The date, on which the player first joined the server.
     */
    public @NotNull Date getFirstJoinDate() {
        return firstJoinDate;
    }

    /**
     * Returns the currently selected language of the player.
     *
     * @return The currently selected language of the player.
     */
    public @NotNull String getLanguage() {
        return language;
    }

    /**
     * Sets the currently selected language of the player.
     *
     * @param language The language to be set.
     */
    public void setLanguage(@NotNull String language) {
        this.language = language;
    }
    // endregion
}
