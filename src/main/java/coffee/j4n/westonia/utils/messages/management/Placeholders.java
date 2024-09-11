package coffee.j4n.westonia.utils.messages.management;

import coffee.j4n.westonia.utils.messages.Messages;
import coffee.j4n.westonia.utils.statics.enums.GradientType;
import coffee.j4n.westonia.utils.statics.enums.MessageArgumentType;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static coffee.j4n.westonia.commands.documentation.CommandDocumentationRegistry.findCommandRaw;

/**
 * Utility class for parsing and replacing placeholders in messages.
 */
public final class Placeholders {

    /**
     * Parses a message and replaces placeholders with the provided parameters.
     *
     * @param rawMessage  The raw message string containing placeholders.
     * @param messageKey  The key associated with the message, used for additional context.
     * @param parameters  The parameters to replace within the message.
     * @return The final processed message as a Component.
     */
    public static Component parseMessage(String rawMessage, Messages messageKey, String locale, String... parameters) {
        String parsedMessage = parsePlaceholders(rawMessage, locale, parameters);
        return MessageHelpers.getMiniMessage().deserialize(parsedMessage);
    }

    /**
     * Replaces placeholders in the message with corresponding values.
     *
     * @param input      The message string containing placeholders.
     * @param parameters The parameters to replace within the message.
     * @return The message with placeholders replaced by actual values.
     */
    private static String parsePlaceholders(String input, String locale, String... parameters) {
        String formattedMessage = input;

        // Replace indexed placeholders {0}, {1}, etc. with corresponding parameters
        for (int i = 0; i < parameters.length; i++) {
            formattedMessage = formattedMessage.replace("{" + i + "}", parameters[i]);
        }

        // Identify and replace special placeholders like {COMMAND}, {GRADIENT}, etc.
        List<MessageArgument> specialPlaceholders = parseSpecialPlaceholders(formattedMessage);

        for (MessageArgument specialPlaceholder : specialPlaceholders) {
            if (specialPlaceholder.getType() == MessageArgumentType.COMMAND) {
                String commandRaw = findCommandRaw(String.join("/", specialPlaceholder.getArguments()), locale);
                formattedMessage = replaceCommandPlaceholder(formattedMessage, commandRaw, specialPlaceholder.getType().name(), String.join(" ", specialPlaceholder.getArguments()));
            } else if (specialPlaceholder.getType() == MessageArgumentType.GRADIENT) {
                String gradientMessage = handleGradientPlaceholder(specialPlaceholder);
                System.out.println( specialPlaceholder.getGradientType().name());
                formattedMessage = replaceGradientPlaceholder(formattedMessage, gradientMessage, specialPlaceholder.getGradientType().name(), String.join(" ", specialPlaceholder.getArguments()));
            }
        }

        return formattedMessage;
    }

    /**
     * Parses the message to identify special placeholders like {COMMAND}, {GRADIENT}, etc.
     *
     * @param rawMessage The raw message string containing placeholders.
     * @return A list of MessageArgument objects representing the identified placeholders.
     */
    private static List<MessageArgument> parseSpecialPlaceholders(String rawMessage) {
        List<MessageArgument> messageArguments = new ArrayList<>();

        // Pattern to identify placeholders of the format {type='value'} or {type:subtype='value'}
        Pattern pattern = Pattern.compile("\\{([\\w:]+)=\\s*'(.*?)'\\s*\\}", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(rawMessage);

        while (matcher.find()) {
            String typeString = matcher.group(1).toUpperCase();
            String argumentsString = matcher.group(2);

            System.out.println("typeString: " + typeString);
            System.out.println("argumentsString: " + argumentsString);

            String[] typeParts = typeString.split(":");

            System.out.println("typeParts: " + Arrays.toString(typeParts));

            MessageArgumentType type = null;

            // Identify the MessageArgumentType from the type string
            for (String part : typeParts) {
                try {
                    type = MessageArgumentType.valueOf(part);
                    break;
                } catch (IllegalArgumentException e) {
                    // Continue searching for a valid type
                }
            }

            if (type == null) {
                continue; // Skip if no valid MessageArgumentType is found
            }

            List<String> arguments = Arrays.asList(argumentsString.split("\\s+"));
            GradientType gradientType = GradientType.NONE;

            // Identify the GradientType if specified
            if (typeParts.length == 2) {
                try {
                    gradientType = GradientType.valueOf(typeParts[1].toUpperCase());
                } catch (IllegalArgumentException ignored) {}
            }

            // Create and add the MessageArgument to the list
            messageArguments.add(new MessageArgument(type, arguments, gradientType));
        }

        return messageArguments;
    }

    /**
     * Replaces a {COMMAND} placeholder in the message with the actual command.
     *
     * @param rawMessage      The raw message string.
     * @param replacement     The replacement string for the command.
     * @param commandType     The type of the command.
     * @param commandArgument The command argument.
     * @return The message with the command placeholder replaced.
     */
    private static String replaceCommandPlaceholder(String rawMessage, String replacement, String commandType, String commandArgument) {
        String regex = "\\{" + Pattern.quote(commandType) + "=\\s*'" + Pattern.quote(commandArgument) + "'\\s*\\}";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(rawMessage);

        return matcher.replaceAll(replacement);
    }

    /**
     * Replaces a {GRADIENT} placeholder in the message with the actual gradient value.
     *
     * @param rawMessage      The raw message string.
     * @param replacement     The replacement string for the gradient.
     * @param gradientType    The type of the gradient.
     * @param gradientArgument The gradient argument.
     * @return The message with the gradient placeholder replaced.
     */
    private static String replaceGradientPlaceholder(String rawMessage, String replacement, String gradientType, String gradientArgument) {
        String regex = "\\{" + Pattern.quote("gradient:" + gradientType) + "=\\s*'" + Pattern.quote(gradientArgument) + "'\\s*\\}";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(rawMessage);

        System.out.println("regex: "+ regex);
        System.out.println("replacement: "+ replacement);
        System.out.println("matcher.replaceAll(replacement);: " + matcher.replaceAll(replacement));

        return matcher.replaceAll(replacement);
    }

    /**
     * Handles the application of a gradient to a placeholder's value.
     *
     * @param specialPlaceholder The MessageArgument containing the gradient information.
     * @return The gradient-applied string.
     */
    private static String handleGradientPlaceholder(MessageArgument specialPlaceholder) {
        return MessageHelpers.applyGradientToString(String.join(" ", specialPlaceholder.getArguments()), specialPlaceholder.getGradientType());
    }

}
