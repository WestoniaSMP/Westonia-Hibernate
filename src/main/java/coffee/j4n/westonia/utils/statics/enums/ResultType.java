package coffee.j4n.westonia.utils.statics.enums;

/**
 * Represents the type of a DbResult or DbReturn.
 * Used to determine how the result should be handled by the plugin.
 */
public enum ResultType {
    CONFIGURATION_ERROR,
    SUCCESS,
    ERROR,
    EXCEPTION,
    FOUND,
    NOT_FOUND,
    ALREADY_EXISTS
}
