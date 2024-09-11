package coffee.j4n.westonia.interfaces;

import coffee.j4n.westonia.database.results.DbResult;
import org.hibernate.SessionFactory;

import javax.annotation.Nullable;

/**
 * Interface for database factories.
 */
public interface IDatabaseFactory {

    /**
     * Connects to the database.
     *
     * @throws Exception If the connection fails.
     */
    void connect() throws Exception;

    /**
     * Includes all annotated classes.
     *
     * @throws Exception If the classes cannot be included.
     */
    void includeAnnotatedClasses() throws Exception;

    /**
     * Builds the session factory.
     *
     * @return The session factory or null if the session factory could not be built.
     */
    @Nullable
    DbResult<SessionFactory> buildSessionFactory();
}