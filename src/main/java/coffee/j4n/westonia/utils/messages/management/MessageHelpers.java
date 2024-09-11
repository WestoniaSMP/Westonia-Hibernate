package coffee.j4n.westonia.utils.messages.management;

import coffee.j4n.westonia.utils.statics.enums.GradientType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

/**
 * Helper methods that are used to send messages or format them.
 */
public final class MessageHelpers {

    public static MiniMessage getMiniMessage() {
        return MiniMessage.miniMessage();
    }

    /**
     * Applies the prefix template to a given prefix.
     *
     * @param prefix The prefix to apply the template to
     * @return The formatted prefix
     */
    public static Component applyPrefixTemplate(String prefix) {
        return applyPrefixTemplate(getMiniMessage().deserialize(prefix));
    }

    /**
     * Applies the prefix template to a given component.
     *
     * @param component The component to apply the template to
     * @return The formatted prefix as component
     */
    public static Component applyPrefixTemplate(Component component) {
        return getMiniMessage().deserialize("<dark_gray><b>|</b></dark_gray> ").append(component).append(getMiniMessage().deserialize(" <b><gray>></gray></b><dark_gray><b>></b></dark_gray> <reset>"));
    }

    /**
     * Applies a MiniMessage gradient to a given string.
     *
     * @param stringToApplyGradientTo The string to apply the gradient to
     * @param gradientType The gradient type to apply
     * @return The string with the applied gradient
     */
    public static String applyGradientToString(String stringToApplyGradientTo, GradientType gradientType) {
        return "<gradient:" + gradientType.getGradient() + ">" + stringToApplyGradientTo + "</gradient>";
    }

    /**
     * Serializes a component to a raw message using the PlainTextComponentSerializer.
     *
     * @param componentToConvert The component to convert.
     * @return The raw message of the component.
     */
    public static String getRawMessageFromComponent(Component componentToConvert) {
        return PlainTextComponentSerializer.plainText().serialize(componentToConvert);
    }
}