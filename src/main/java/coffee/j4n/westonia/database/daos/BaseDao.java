package coffee.j4n.westonia.database.daos;

import coffee.j4n.westonia.database.DatabaseFactory;
import coffee.j4n.westonia.database.results.DbResult;
import coffee.j4n.westonia.database.results.DbReturn;
import coffee.j4n.westonia.utils.config.MdlDatabaseConfig;
import coffee.j4n.westonia.utils.statics.enums.ResultType;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import org.bukkit.plugin.java.JavaPlugin;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Base Data Access Object (DAO) that provides basic methods for interacting with the database asynchronously.
 * This class is abstract and should be extended by other DAOs.
 *
 * @param <Entity> The type of the entity that is managed by the DAO.
 * @param <Plugin> The type of the plugin that is using the DAO.
 */
public abstract class BaseDao<Entity, Plugin extends JavaPlugin> {

    private final Logger logger;
    private final Plugin pluginInstance;
    private final MdlDatabaseConfig databaseConfig;

    private Session session;

    /**
     * Creates a new instance of the BaseDao class.
     *
     * @param pluginInstance        The plugin instance that is using the DAO.
     * @param databaseConfiguration The configuration of the database.
     */
    public BaseDao(final @NotNull Plugin pluginInstance, final MdlDatabaseConfig databaseConfiguration) {
        this.pluginInstance = pluginInstance;
        this.databaseConfig = databaseConfiguration;
        this.logger = this.pluginInstance.getLogger();
    }

    /**
     * Creates a new session and returns it.
     *
     * @return A DbResult containing the session or an error message.
     */
    public DbResult<Session> getOrCreateSession() {
        final DbResult<SessionFactory> sessionFactoryResult = this.getSessionFactory();

        if (!sessionFactoryResult.isSuccessful()) {
            String msg = "Session factory result was not successful";

            this.logger.log(Level.SEVERE, msg);
            return new DbResult<>(null, msg, ResultType.ERROR);
        }

        final SessionFactory sessionFactory = sessionFactoryResult.getResult();

        if (sessionFactory == null) {
            String msg = "Failed to get session factory from result";

            this.logger.log(Level.SEVERE, msg);
            return new DbResult<>(null, msg, ResultType.ERROR);
        }

        if (this.session == null || !this.session.isOpen())
            this.session = sessionFactory.withOptions().flushMode(FlushMode.AUTO).openSession();


        return new DbResult<>(this.session, "Session created", ResultType.SUCCESS);
    }

    /**
     * Saves the given entity to the database.
     *
     * @param entity The entity to save.
     * @return A DbReturn containing a success or error message.
     */
    public DbReturn persistEntity(final @NotNull Entity entity) {
        final String entityName = entity.getClass().getTypeName();
        final Session session = this.getOrCreateSession().getResult();

        try (session) {
            if (session == null) {
                String msg = "Failed to get session";
                this.logger.log(Level.SEVERE, msg);
                return new DbReturn(msg, ResultType.ERROR);
            }

            session.beginTransaction();
            session.persist(entity);
            session.flush();
            session.getTransaction().commit();

        } catch (final Exception exception) {
            final String msg = "Failed to persist entity \"" + entityName + "\"";

            this.logger.log(Level.SEVERE, msg, exception);
            return new DbReturn(msg, ResultType.EXCEPTION);

        }

        final String msg = "Entity \"" + entityName + "\" successfully persisted";

        this.logger.log(Level.INFO, msg);
        return new DbReturn(msg, ResultType.SUCCESS);
    }

    /**
     * Removes the given entity from the database.
     *
     * @param data The entity to remove.
     * @return A DbReturn containing a success or error message.
     */
    public DbReturn removeEntity(final @NotNull Entity data) {
        try {
            session.beginTransaction();
            session.remove(data);
            session.getTransaction().commit();

        } catch (final Exception exception) {
            if (session.getTransaction() != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }

            String msg = "Failed to remove entity";
            this.logger.log(Level.SEVERE, msg, exception);
            return new DbReturn(msg, ResultType.ERROR);

        } finally {
            session.close();
        }

        return new DbReturn("Entity successfully removed", ResultType.SUCCESS);
    }

    /**
     * Returns the type of the entity that is managed by the DAO.
     *
     * @return The entity type of the DAO.
     */
    protected abstract Class<Entity> getClazzType();


    /**
     * Returns the CriteriaBuilder that is used to create queries.
     *
     * @return A DbResult containing the CriteriaBuilder or an error message.
     */
    protected abstract DbResult<CriteriaBuilder> getCriteriaBuilder();

    /**
     * Returns the CriteriaQuery that is used to create queries.
     *
     * @return A DbResult containing the CriteriaQuery or an error message.
     */
    protected abstract DbResult<CriteriaQuery<Entity>> getCriteriaQuery();

    /**
     * Returns the session factory that is used to create sessions.
     *
     * @return A DbResult containing the session factory or an error message.
     */
    private DbResult<SessionFactory> getSessionFactory() {
        final DatabaseFactory<Plugin> databaseFactory = new DatabaseFactory<>(this.pluginInstance, databaseConfig);
        return databaseFactory.buildSessionFactory();
    }
}
