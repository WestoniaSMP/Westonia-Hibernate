package coffee.j4n.westonia.utils.statics.constants;

/**
 * Contains all permissions used in the Westonia plugin
 */
public final class Permissions {

    //<editor-fold desc="Prefixes">
    /**
     * The prefix for all permissions of the Westonia plugin
     */
    public static final String WESTONIA_PREFIX = "westonia";

    //</editor-fold>

    /**
     * The permission to use all features and commands of the Westonia plugin
     */
    public static final String WESTONIA_ALL = WESTONIA_PREFIX + ".*";

    //<editor-fold desc="Fly permissions">
    /**
     * The permission to toggle the own fly mode
     */
    public static final String FLY = WESTONIA_PREFIX + ".fly";
    /**
     * The permission to toggle fly mode for other players
     */
    public static final String FLY_OTHERS = FLY + ".others";

    /**
     * The permission to change the fly speed of the own fly mode
     */
    public static final String FLY_SPEED = FLY + ".speed";
    /**
     * The permission to change the fly speed of the fly mode for other players
     */
    public static final String FLY_SPEED_OTHERS = FLY_SPEED + ".others";
    //</editor-fold>

}