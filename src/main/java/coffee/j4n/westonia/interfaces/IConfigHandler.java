package coffee.j4n.westonia.interfaces;

import java.io.File;

/**
 * Interface for the config handler
 * @param <ConfigModel> The model of the config that should be handled
 */
public interface IConfigHandler<ConfigModel> {

    /**
     * Saves the handled config.
     */
    void saveConfig();

    /**
     * Updates the config with the given config model
     * @param configModel The config model to update the config with
     */
    void updateConfig(ConfigModel configModel);

    /**
     * Loads the config from the file system
     */
    void loadConfig();

    /**
     * Returns the config model that is currently handled by this config handler
     *
     * @return The config model that is currently handled by this config handler
     */
    ConfigModel getConfigModel();

    /**
     * Returns the file that the config is saved to
     *
     * @return The file that the config is saved to
     */
    File getFile();
}
