package coffee.j4n.westonia.database.daos;

import coffee.j4n.westonia.Westonia;
import coffee.j4n.westonia.database.entities.WstPlayer;
import coffee.j4n.westonia.database.results.DbResult;
import coffee.j4n.westonia.database.results.DbReturn;
import coffee.j4n.westonia.utils.config.MdlDatabaseConfig;
import coffee.j4n.westonia.utils.statics.enums.ResultType;
import jakarta.persistence.Table;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("OptionalOfNullableMisuse")
@Table(name = "network_player")
public class PlayerDao extends BaseDao<WstPlayer, Westonia> {

    private final Logger logger;

    /**
     * Creates a new instance of the PlayerDao class.
     */
    public PlayerDao(@NotNull Westonia plugin, final @NotNull MdlDatabaseConfig databaseConfiguration) {
        super(plugin, databaseConfiguration);
        this.logger = plugin.getLogger();
    }

    /**
     * Returns the player based on the UUID.
     *
     * @param uuid The UUID of the player.
     * @return A CompletableFuture that completes with the DbResult containing the WstPlayer or an error.
     */
    public DbResult<WstPlayer> getPlayer(final @NotNull String uuid) {
        DbResult<Session> sessionResult = this.getOrCreateSession();
        DbResult<CriteriaBuilder> criteriaBuilderResult = this.getCriteriaBuilder();
        DbResult<CriteriaQuery<WstPlayer>> criteriaQueryResult = this.getCriteriaQuery();

        if (!sessionResult.isSuccessful()) {
            String msg = "Session result type is " + sessionResult.getResultType().toString();
            this.logger.log(Level.SEVERE, msg);
            return new DbResult<>(null, msg, ResultType.ERROR);
        }

        if (!criteriaBuilderResult.isSuccessful()) {
            String msg = "CriteriaBuilder result type is " + criteriaBuilderResult.getResultType().toString();
            this.logger.log(Level.SEVERE, msg);
            return new DbResult<>(null, msg, ResultType.ERROR);
        }

        if (!criteriaQueryResult.isSuccessful()) {
            String msg = "CriteriaQuery result type is " + criteriaQueryResult.getResultType().toString();
            this.logger.log(Level.SEVERE, msg);
            return new DbResult<>(null, msg, ResultType.ERROR);
        }

        Session session = sessionResult.getResult();
        CriteriaBuilder criteriaBuilder = criteriaBuilderResult.getResult();
        CriteriaQuery<WstPlayer> criteriaQuery = criteriaQueryResult.getResult();

        if (session == null || criteriaBuilder == null || criteriaQuery == null) {
            String msg = "Session, CriteriaBuilder or CriteriaQuery is null";
            this.logger.log(Level.SEVERE, msg);
            return new DbResult<>(null, msg, ResultType.ERROR);
        }

        Root<WstPlayer> root = criteriaQuery.from(WstPlayer.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("minecraftUUID"), uuid));

        WstPlayer foundPlayer = session.createQuery(criteriaQuery).uniqueResult();
        if (foundPlayer == null) {
            return new DbResult<>(null, "Player not found", ResultType.NOT_FOUND);
        }

        return new DbResult<>(foundPlayer, "Player found", ResultType.FOUND);
    }

    /**
     * Returns the language of the player based on the UUID.
     *
     * @param uuid The UUID of the player whose language is to be returned.
     * @return A DbResult containing the language of the player or an error.
     */
    public DbResult<String> getLanguage(final @NotNull String uuid) {
        DbResult<WstPlayer> playerResult = getPlayer(uuid);

        if (!playerResult.isSuccessful()) {
            return new DbResult<>(null, playerResult.getMessage(), playerResult.getResultType());
        }

        return new DbResult<>(playerResult.getResult().getLanguage(), "Language found", ResultType.FOUND);
    }

    /**
     * Updates the language of the player based on the UUID synchronously
     *
     * @param uuid     The UUID of the player whose language is to be updated.
     * @param language The language to be updated.
     * @return A DbReturn containing the result of the operation.
     */
    public DbReturn updateLanguage(final @NotNull String uuid, final @NotNull String language) {
        DbResult<WstPlayer> playerResult = getPlayer(uuid);
        if (!playerResult.isSuccessful()) {
            return new DbReturn(playerResult.getMessage(), ResultType.ERROR);
        }

        WstPlayer player = playerResult.getResult();

        if (player == null) {
            return new DbReturn("Player is null", ResultType.ERROR);
        }

        player.setLanguage(language);

        return this.persistEntity(player);
    }

    /**
     * Checks if the player is registered based on the UUID.
     *
     * @param uuid The UUID of the player.
     * @return A DbReturn containing the result of the operation.
     */
    public DbReturn isPlayerRegistered(final @NotNull String uuid) {
        DbResult<WstPlayer> playerResult = getPlayer(uuid);
        if (!playerResult.isSuccessful()) {
            return new DbReturn(playerResult.getMessage(), playerResult.getResultType());
        }

        if (playerResult.getResult() == null) {
            return new DbReturn("Player not found", ResultType.NOT_FOUND);
        }

        return new DbReturn("Player found", ResultType.FOUND);
    }

    /**
     * Registers a player.
     *
     * @param player The player to be registered.
     * @return A DbrReturn containing the result of the operation.
     */
    public DbReturn registerPlayer(final @NotNull WstPlayer player) {
        return this.persistEntity(player);
    }

    @Override
    protected DbResult<CriteriaBuilder> getCriteriaBuilder() {
        DbResult<Session> sessionResult = getOrCreateSession();
        if (!sessionResult.isSuccessful()) {
            this.logger.log(Level.SEVERE, "Failed to get session");
            return new DbResult<>(null, "Failed to get session", ResultType.ERROR);
        }

        Session session = sessionResult.getResult();

        if (session == null) {
            this.logger.log(Level.SEVERE, "Session is null");
            return new DbResult<>(null, "Session is null", ResultType.ERROR);
        }

        CriteriaBuilder builder = session.getCriteriaBuilder();

        if (builder == null) {
            this.logger.log(Level.SEVERE, "CriteriaBuilder is null");
            return new DbResult<>(null, "CriteriaBuilder is null", ResultType.ERROR);
        }

        return new DbResult<>(builder, "CriteriaBuilder created", ResultType.SUCCESS);
    }

    @Override
    protected DbResult<CriteriaQuery<WstPlayer>> getCriteriaQuery() {
        DbResult<CriteriaBuilder> criteriaBuilderResult = getCriteriaBuilder();

        if (!criteriaBuilderResult.isSuccessful()) {
            this.logger.log(Level.SEVERE, "CriteriaBuilder is null");
            return new DbResult<>(null, "CriteriaBuilder is null", ResultType.ERROR);
        }

        CriteriaBuilder criteriaBuilder = criteriaBuilderResult.getResult();

        if (criteriaBuilder == null) {
            this.logger.log(Level.SEVERE, "CriteriaBuilder is null");
            return new DbResult<>(null, "CriteriaBuilder is null", ResultType.ERROR);
        }

        CriteriaQuery<WstPlayer> criteriaQuery = criteriaBuilder.createQuery(WstPlayer.class);

        if (criteriaQuery == null) {
            this.logger.log(Level.SEVERE, "CriteriaQuery is null");
            return new DbResult<>(null, "CriteriaQuery is null", ResultType.ERROR);
        }

        return new DbResult<>(criteriaQuery, "CriteriaQuery created", ResultType.SUCCESS);
    }

    /**
     * Gibt die Klasse des Typs zurück, der vom DAO (Data Access Object) verwaltet wird.
     */
    @Override
    protected Class<WstPlayer> getClazzType() {
        return WstPlayer.class;
    }
}
