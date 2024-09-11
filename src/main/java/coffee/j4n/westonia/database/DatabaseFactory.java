package coffee.j4n.westonia.database;

import com.google.common.reflect.ClassPath;
import coffee.j4n.westonia.database.results.DbResult;
import coffee.j4n.westonia.utils.statics.enums.ResultType;
import coffee.j4n.westonia.interfaces.IDatabaseFactory;
import coffee.j4n.westonia.utils.config.MdlDatabaseConfig;
import org.bukkit.plugin.java.JavaPlugin;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.jetbrains.annotations.NotNull;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Connects to the database and provides a SessionFactory synchronously.
 */
public class DatabaseFactory<T extends JavaPlugin> implements IDatabaseFactory {


    private final Logger logger;
    private final MdlDatabaseConfig databaseConfiguration;
    private Configuration configuration;


    public DatabaseFactory(final @NotNull T pluginInstance, final @NotNull MdlDatabaseConfig databaseConfiguration) {
        this.databaseConfiguration = databaseConfiguration;
        this.logger = pluginInstance.getLogger();
        this.connect();
    }


    /**
     * Synchronously connects to the database.
     */
    @Override
    public void connect() {
        Properties properties = new Properties();

        properties.setProperty(Environment.JAKARTA_JDBC_DRIVER, "com.mysql.cj.jdbc.Driver");
        properties.setProperty(Environment.AUTOCOMMIT, "true");
        properties.setProperty(Environment.AUTO_CLOSE_SESSION, "true");
        properties.setProperty(Environment.HBM2DDL_AUTO, "update");
        properties.setProperty(Environment.JAKARTA_JDBC_USER, this.databaseConfiguration.getUsername());
        properties.setProperty(Environment.JAKARTA_JDBC_PASSWORD, this.databaseConfiguration.getPassword());

        properties.setProperty(Environment.JAKARTA_JDBC_URL, this.databaseConfiguration.getConnectionURL());
        properties.setProperty(Environment.SHOW_SQL, String.valueOf(this.databaseConfiguration.getShowSqlInConsole()));

        this.configuration = new Configuration().addProperties(properties);
        this.includeAnnotatedClasses();
    }


    /**
     * Synchronously includes all annotated classes in the configuration.
     */
    @Override
    public void includeAnnotatedClasses() {
        try {
            ClassPath.from(this.getClass().getClassLoader()).getAllClasses().stream().filter(classInfo -> {
                return classInfo.getPackageName().contains(this.databaseConfiguration.getDatabaseEntitiesPackage());
            }).toList().stream().map(ClassPath.ClassInfo::load).forEach(clazz -> {
                try {
                    this.getClass().getClassLoader().loadClass(clazz.getName());
                    this.configuration.addAnnotatedClass(clazz);
                } catch (final Exception exception) {
                    this.logger.log(Level.SEVERE, "An exception occurred while loading the class: " + clazz.getName(), exception);
                }
            });
        } catch (final Exception exception) {
            this.logger.log(Level.SEVERE, "An exception occurred while including annotated classes", exception);
        }
    }

    /**
     * Creates a SessionFactory synchronously.
     *
     * @return A DbResult containing the SessionFactory or an error message.
     */
    @Override
    public DbResult<SessionFactory> buildSessionFactory() {
        try {
            if (this.configuration == null) {
                String msg = "The configuration is null";
                this.logger.log(Level.SEVERE, msg);
                return new DbResult<>(null, msg, ResultType.CONFIGURATION_ERROR);
            }

            return new DbResult<>(this.configuration.buildSessionFactory(),"The session factory was successfully built", ResultType.SUCCESS);
        } catch (HibernateException exception) {
            String msg = "An exception occurred while building the session factory";

            this.logger.log(Level.SEVERE, msg, exception);
            return new DbResult<>(null, msg, ResultType.EXCEPTION);
        }
    }
}