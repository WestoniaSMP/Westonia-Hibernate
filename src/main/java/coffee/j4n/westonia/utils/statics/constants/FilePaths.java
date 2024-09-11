package coffee.j4n.westonia.utils.statics.constants;

/**
 * Contains all file paths used in the Westonia plugin
 */
public final class FilePaths {

    //<editor-fold desc="Folder paths">
    /**
     * The folder where the plugin stores its files
     */
    public static final String PLUGIN_FOLDER = "plugins/Westonia";

    /**
     * The folder where the plugin stores its messages
     */
    public static final String MESSAGES_FOLDER = PLUGIN_FOLDER + "/messages";
    //</editor-fold>


    //<editor-fold desc="File paths">
    /**
     * The path to the database config file
     */
    public static final String DATABASE_CONFIG = PLUGIN_FOLDER + "/database-config.yml";

    /**
     * The path to the default messages config file
     */
    public static final String DEFAULT_MESSAGES_CONFIG = PLUGIN_FOLDER + "/messages_de.yml";
    //</editor-fold>
}

