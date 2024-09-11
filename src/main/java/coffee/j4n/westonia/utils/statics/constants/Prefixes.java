package coffee.j4n.westonia.utils.statics.constants;

import coffee.j4n.westonia.utils.messages.management.MessageHelpers;
import coffee.j4n.westonia.utils.statics.enums.GradientType;
import net.kyori.adventure.text.Component;

import static coffee.j4n.westonia.utils.messages.management.MessageHelpers.applyGradientToString;
import static coffee.j4n.westonia.utils.messages.management.MessageHelpers.applyPrefixTemplate;

/**
 * Static class that contains all chat prefixes used in the plugin.
 */
public final class Prefixes {

    public static final String WESTONIA_RAW_STRING = applyGradientToString("Westonia", GradientType.PREFIX);

    /*+
     * Raw prefix of the plugin.
     */
    public static final Component WESTONIA_RAW = MessageHelpers.getMiniMessage().deserialize(WESTONIA_RAW_STRING);

    /**
     * Same as {@link Prefixes#WESTONIA_RAW WESTONIA_RAW} but FAT.
     */
    public static final Component WESTONIA_RAW_FAT = MessageHelpers.getMiniMessage().deserialize("<b>" + WESTONIA_RAW_STRING + "</b>");

    /**
     * Prefix of the plugin.
     */
    public static final Component WESTONIA_PREFIX = applyPrefixTemplate(WESTONIA_RAW);

    /**
     * Double arrow pointing to the right, used for sub prefixes.
     */
    public static final Component ARROWS_POINTING_RIGHT = MessageHelpers.getMiniMessage().deserialize("<gray><b>></b></gray><dark_gray><b>></b></dark_gray> <reset>");

    /**
     * Prefix for debug messages.
     */
    public static final Component DEBUG = applyPrefixTemplate( "<dark_purple><b><i>DEBUG</i></b></dark_purple>");

    /**
     * Prefix for information.
     */
    public static final Component INFO =  applyPrefixTemplate("<green><b>I</b></green>");

    /**
     * Prefix for warnings.
     */
    public static final Component WARN = applyPrefixTemplate("<gold><b>!</b></gold>");

    /**
     * Prefix for errors.
     */
    public static final Component ERROR = applyPrefixTemplate("<red><b>!</b></red>");

}
