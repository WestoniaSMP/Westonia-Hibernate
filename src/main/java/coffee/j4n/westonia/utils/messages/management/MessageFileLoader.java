package coffee.j4n.westonia.utils.messages.management;

import coffee.j4n.westonia.Westonia;
import coffee.j4n.westonia.interfaces.IMessageBase;
import coffee.j4n.westonia.utils.FileUtils;
import coffee.j4n.westonia.utils.statics.constants.FilePaths;
import coffee.j4n.westonia.utils.statics.constants.Prefixes;
import coffee.j4n.westonia.utils.statics.enums.GradientType;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.*;

/**
 * This class is used to load messages from a file.
 *
 * @param <MessageEnum> The enum of the messages.
 */
public class MessageFileLoader<MessageEnum extends Enum<MessageEnum> & IMessageBase> {

    Class<MessageEnum> enumClass;

    /**
     * Creates a new message file loader for the given enum class.
     *
     * @param enumClass The enum class of the messages.
     */
    public MessageFileLoader(Class<MessageEnum> enumClass) {
        this.enumClass = enumClass;
    }

    /**
     * Copies the default messages file to the messages folder if it does not exist.
     */
    public static void copyDefaultMessages() {
        List<File> languageFiles = new ArrayList<>();

        // Define the default messages files for each language
        File defaultGermanLanguageFile = new File(FilePaths.MESSAGES_FOLDER, "messages_de.yml");
        File defaultEnglishLanguageFile = new File(FilePaths.MESSAGES_FOLDER, "messages_en.yml");

        languageFiles.add(defaultGermanLanguageFile);
        languageFiles.add(defaultEnglishLanguageFile);

        for (File languageFile : languageFiles) {
            copyDefaultMessages(languageFile);
        }
    }

    /**
     * Does the actual copying of the default messages file.
     *
     * @param destinationFile The file to copy the default messages file to.
     */
    private static void copyDefaultMessages(File destinationFile) {
        if (!destinationFile.exists()) {
            FileUtils.createParentDirectories(destinationFile);

            InputStream inputStream = MessageFileLoader.class.getResourceAsStream("/" + destinationFile.getName());

            if (inputStream == null) {
                throw new MissingResourceException("Could not find default messages file in resources (inputStream is null)", "MessageFileLoader", "messages_de.yml");
            }

            try (OutputStream outputStream = new FileOutputStream(destinationFile)) {

                byte[] buffer = new byte[1024];
                int length;

                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }

            } catch (IOException e) {
                throw new MissingResourceException("Could not copy default messages file to destination ", "MessageFileLoader", "messages_de.yml");
            }
        }
    }

    /**
     * Loads messages from a "message_[language].yml" file and returns them as a MessageWrapper.
     *
     * @param file The file to load the messages from.
     */
    public MessageWrapper<MessageEnum> loadMessagesFromFile(File file) throws IOException {
        Map<MessageEnum, String> messages = new HashMap<>();
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        String language = "";

        if (config.get("languagename") == null) {
            return null;
        }

        language = config.getString("languagename");

        if (language == null || language.isEmpty()) {
            Westonia.getInstance().getServer().getConsoleSender().sendMessage(Prefixes.INFO.append(MessageHelpers.getMiniMessage().deserialize("The message file \"<aqua>" + file.getName() + "</aqua>\" could not be loaded: The key \"<aqua>languagename</aqua>\" was empty.")));
            throw new MissingResourceException("The key 'languagename' was empty.", "MessageFileLoader", "languagename");
        }

        // Loop through all message keys and load try to load the messages based on the yaml path in the enum
        for (MessageEnum messageKey : enumClass.getEnumConstants()) {
            String path = messageKey.getYamlConfigurationPath();
            StringBuilder message = new StringBuilder();

            if (config.contains(path)) {
                // Allow messages to be a list of strings
                if (config.isList(path)) {

                    List<String> messageList = config.getStringList(path);
                    for (String line : messageList) {
                        if (messageList.indexOf(line) == 0) {
                            message.append(MessageHelpers.applyGradientToString(line, GradientType.DEFAULT));
                            continue;
                        }

                        // Empty lines should be replaced with a newline and have no prefix
                        // Lines with content should be prefixed with a newline and the ARROWS_POINTING_RIGHT prefix
                        if (line.isEmpty()) {
                            message.append("<newline>");
                        } else {
                            message.append("<newline>").append("<gray><b>></b><dark_gray><b>></b></dark_gray></gray> ");
                        }

                        // Apply default gradient to the line
                        message.append(MessageHelpers.applyGradientToString(line, GradientType.DEFAULT));
                    }

                    // Put the message into the map
                    messages.put(messageKey, message.toString());
                } else {
                    // Normal "one-line" messages can just be put into the map
                    message.append(MessageHelpers.applyGradientToString(config.getString(path), GradientType.DEFAULT));
                    messages.put(messageKey, message.toString());
                }
            } else {
                Bukkit.getServer().getConsoleSender().sendMessage(Prefixes.WARN.append(MessageHelpers.getMiniMessage().deserialize("Found no message for \"<aqua>" + messageKey + "</aqua>\" (YAML Path: \"<aqua>" + messageKey.getYamlConfigurationPath() + "</aqua>\"). This message will be replaced with a placeholder, please fix this ASAP.")));
                messages.put(messageKey, "<red><b>[Found no message for \"<aqua>" + messageKey + "</aqua>\" (YAML Path: \"<aqua>" + messageKey.getYamlConfigurationPath() + "</aqua>), please inform an team member.]</b></red>");
            }
        }
        // Wrap the loaded messages in a MessageWrapper and return it
        return new MessageWrapper<>(messages, language);
    }
}
