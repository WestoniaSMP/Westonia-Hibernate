package coffee.j4n.westonia.interfaces;

/**
 * Interface for messages that are loaded from a yaml configuration file.
 */
public interface IMessageBase {

    /**
     * Returns the path to the yaml configuration file.
     *
     * @return The path to the yaml configuration file.
     */
    String getYamlConfigurationPath();
}
