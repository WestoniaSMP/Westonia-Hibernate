package coffee.j4n.westonia.utils.config;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This class represents the configuration of the database.
 */
public class MdlDatabaseConfig {

    /**
     * The host of the database.
     */
    private String host;

    /**
     * The port of the database.
     */
    private Integer port;

    /**
     * The name of the database.
     */
    private String databaseName;

    /**
     * The name of the database user.
     */
    private String username;

    /**
     * The password of the database user.
     */
    private String password;

    /**
     * Whether the SQL and debug messages should be shown in the console.
     */
    private Boolean showSqlInConsole;

    /**
     * The package where the database entities are located in the project.
     */
    private String databaseEntitiesPackage;

    /**
     * Default constructor for Jackson.
     */
    public MdlDatabaseConfig() {}

    /**
     * Creates a new database configuration.
     *
     * @param host The host of the database.
     * @param port The port of the database.
     * @param databaseName The name of the database.
     * @param username The name of the database user.
     * @param password The password of the database user.
     * @param showSqlInConsole Whether the SQL and debug messages should be shown in the console.
     * @param databaseEntitiesPackage The package where the database entities are located in the project.
     */
    public MdlDatabaseConfig(String host, Integer port, String databaseName, String username, String password, Boolean showSqlInConsole, String databaseEntitiesPackage) {
        this.host = host;
        this.port = port;
        this.databaseName = databaseName;
        this.username = username;
        this.password = password;
        this.showSqlInConsole = showSqlInConsole;
        this.databaseEntitiesPackage = databaseEntitiesPackage;
    }

    /**
     * Returns the host of the database.
     *
     * @return The host of the database.
     */
    public String getHost() {
        return this.host;
    }

    /**
     * Returns the port of the database.
     *
     * @return The port of the database.
     */
    public Integer getPort() {
        return this.port;
    }

    /**
     * Returns the name of the database.
     *
     * @return The name of the database.
     */
    public String getDatabaseName() {
        return this.databaseName;
    }

    /**
     * Returns the name of the database user.
     *
     * @return The name of the database user.
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Returns the password of the database user.
     *
     * @return The password of the database user.
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Returns whether the SQL and debug messages should be shown in the console.
     *
     * @return Whether the SQL and debug messages should be shown in the console.
     */
    public Boolean getShowSqlInConsole() {
        return this.showSqlInConsole;
    }

    /**
     * Returns the package where the database entities are located in the project.
     *
     * @return The package where the database entities are located in the project.
     */
    public String getDatabaseEntitiesPackage() {
        return this.databaseEntitiesPackage;
    }

    /**
     * Returns the connection URL for the database, which is used to connect to the database.
     * The URL is built from the host, port and database name.
     * This method is annotated with @JsonIgnore, so that Jackson does not serialize it.
     *
     * @return The connection URL for the database.
     */
    @JsonIgnore
    public String getConnectionURL() {
        return "jdbc:mysql://" + this.getHost() + ":" + this.getPort() + "/" + this.getDatabaseName() + "?useUnicode=true";
    }
}
