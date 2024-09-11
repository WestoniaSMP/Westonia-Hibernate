package coffee.j4n.westonia.utils;

import coffee.j4n.westonia.utils.messages.management.MessageHelpers;
import coffee.j4n.westonia.utils.statics.constants.Prefixes;
import coffee.j4n.westonia.utils.statics.enums.GradientType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import coffee.j4n.westonia.interfaces.IConfigHandler;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;


/**+
 * A generic implementation of the IConfigHandler interface that handles the loading and saving of a config file as well as the updating of the config model.
 *
 * @param <ConfigModel> The model of the config that should be handled
 */
public class ConfigHandler<ConfigModel> implements IConfigHandler<ConfigModel> {

    // <editor-fold defaultstate="collapsed" desc="Constructor and fields">
    /**
     * The type of the config model that is handled by this config handler
     */
    private final Class<ConfigModel> type;

    /**
     * The file that the config is saved to
     */
    private final File file;

    /**
     * The config model that is currently handled by this config handler
     */
    private ConfigModel configModel;

    /**
     * Creates a new config handler with the given file and type
     *
     * @param file The file that the config is saved to
     * @param type The type of the config model that is handled by this config handler
     */
    public ConfigHandler(File file, Class<ConfigModel> type) {
        this.file = file;
        this.type = type;

        // Make sure the file and its parent folders exist
        createFoldersIfNotExists(file.getParentFile());
    }
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="Interface implementation">
    /**
     * Loads the config from the file system.
     * If the file does not exist, a new config model with default values is created and saved to the file.
     */
    public void loadConfig() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            if (!file.exists()) {
                if (file.createNewFile()) {
                    this.configModel = Helpers.createWithDefaultValues(type);
                    saveConfig();

                    Bukkit.getServer().getConsoleSender().sendMessage(Prefixes.INFO.append(MessageHelpers.getMiniMessage().deserialize("The configuration file \"<aqua>" + file.getName() + "</aqua>\" has been " + MessageHelpers.applyGradientToString("successfully", GradientType.SUCCESS) + " created.")));
                }
            }
            this.configModel = mapper.readValue(file, type);

        } catch (IOException e) {
            Bukkit.getServer().getConsoleSender().sendMessage(Prefixes.ERROR.append(MessageHelpers.getMiniMessage().deserialize("An error occurred while loading the configuration file \"<aqua>" + file.getName() + "</aqua>\".<newline>Exception: <red>" + e.getMessage() + "</red><newline>Stacktrace: <red>" + Arrays.toString(e.getStackTrace()) + "</red>")));
        }
    }

    /**
     * Saves the handled config and reloads it afterwards.
     * This ensures that even if a error occurs while saving the config, the config model is still up to date with the file in the file system.
     */
    public void saveConfig()  {
        if (this.configModel == null)
            return;

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            mapper.writeValue(file, this.configModel);
        } catch (IOException e) {
            Bukkit.getServer().getConsoleSender().sendMessage(Prefixes.ERROR.append(MessageHelpers.getMiniMessage().deserialize("An error occurred while saving the configuration file \"<aqua>" + file.getName() + "</aqua>\".<newline>Exception: <red>" + e.getMessage() + "</red><newline>Stacktrace: <red>" + Arrays.toString(e.getStackTrace()) + "</red>")));
        }

        this.configModel = null;
        this.loadConfig();
    }

    public void updateConfig(ConfigModel configModel)  {
        if (this.configModel == null)
            return;

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            mapper.writeValue(file, configModel);
        } catch (IOException e) {
            Bukkit.getServer().getConsoleSender().sendMessage(Prefixes.ERROR.append(MessageHelpers.getMiniMessage().deserialize("An error occurred while updating the configuration file \"<aqua>" + file.getName() + "</aqua>\".<newline>Exception: <red>" + e.getMessage() + "</red><newline>Stacktrace: <red>" + Arrays.toString(e.getStackTrace()) + "</red>")));
        }
    }

    public ConfigModel getConfigModel() {
        if (configModel == null) {
            loadConfig();
        }

        return configModel;
    }

    public File getFile() {
        return file;
    }
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="Private methods">
    /**
     * Creates the folders for the given file if they do not exist
     *
     * @param folder The folder to create
     */
    private void createFoldersIfNotExists(File folder) {
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                Bukkit.getServer().getConsoleSender().sendMessage(Prefixes.ERROR.append(MessageHelpers.getMiniMessage().deserialize("Could not create folder: \"<aqua>" + folder.getAbsolutePath() + "</aqua>\".")
                        .appendNewline()
                        .append(Prefixes.ARROWS_POINTING_RIGHT).append(MessageHelpers.getMiniMessage().deserialize("An exception occurred while creating the folder:<newline><red>" + folder.getAbsolutePath() + "</red>."))));
                throw new IllegalStateException("Could not create folder: " + folder.getAbsolutePath());
            }
        }
    }
    // </editor-fold>
}