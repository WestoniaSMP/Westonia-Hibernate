package coffee.j4n.westonia.utils.messages;

import coffee.j4n.westonia.utils.messages.management.*;
import coffee.j4n.westonia.utils.statics.constants.Prefixes;
import coffee.j4n.westonia.utils.statics.constants.FilePaths;
import coffee.j4n.westonia.utils.statics.enums.GradientType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * The MessageFactory is used to load messages from files and return them.
 * <p />
 * The messages are loaded from the folder FilePaths.MESSAGES_FOLDER.
 * <p />
 * The MessageFactory implements a method to get a message for a specific key and language.
 */
public class MessageFactory {
    private final Map<String, MessageWrapper<Messages>> messages = new HashMap<>();

    /**
     * Loads all messages from the files in the folder FilePaths.MESSAGES_FOLDER.
     * Messages can only be loaded if the key "languagename" is set in the file.
     *
     * @param enumClass The enum class of the messages.
     * @param languageNameKey The key of the language name in the messages file.
     */
    public MessageFactory(Class<Messages> enumClass, Messages languageNameKey) {
        File folder = new File(FilePaths.MESSAGES_FOLDER);

        MessageFileLoader.copyDefaultMessages();

        for (File file : Objects.requireNonNull(folder.listFiles())) {
            if (file.isFile()) {
                Bukkit.getServer().getConsoleSender().sendMessage(Prefixes.INFO.append(MessageHelpers.getMiniMessage().deserialize("Trying to load \"<aqua>" + file.getPath() + "</aqua>\"...")));

                String name = file.getName();
                // Get [locale] from messages_[locale].yml
                String fileLocale = name.substring(name.lastIndexOf("_") + 1, name.lastIndexOf("."));

                try {
                    MessageFileLoader<Messages> messageFileLoader = new MessageFileLoader<>(enumClass);
                    MessageWrapper<Messages> messageWrapper = messageFileLoader.loadMessagesFromFile(file);

                    if (messageWrapper == null) {
                        // Skip this file, as the language key is missing
                        Bukkit.getServer().getConsoleSender().sendMessage(Prefixes.ERROR.append(MessageHelpers.getMiniMessage().deserialize("The message file \"<aqua>" + file.getName() + "</aqua>\" for the locale \"<aqua>" + fileLocale + "</aqua)>\" could <red>not</red> be loaded: The key \"<aqua>languagename</aqua>\" was empty.")));
                        continue;
                    }

                    messages.put(fileLocale, messageWrapper);
                    String languageName = getRawMessage(Messages.LANGUAGE_NAME, fileLocale);

                    // Success message
                    Bukkit.getServer().getConsoleSender().sendMessage(Prefixes.INFO.append(MessageHelpers.getMiniMessage().deserialize("The messages from the file \"<aqua>" + file.getName() + "</aqua>\" for the language <dark_green>" + languageName + "</dark_green> (<dark_green>" + fileLocale + "</dark_green>) were <green>successfully</green> loaded.")));
                } catch (IOException e) {
                    Bukkit.getServer().getConsoleSender().sendMessage(Prefixes.ERROR.append(MessageHelpers.getMiniMessage().deserialize("The messages from the file \"<aqua>" + file.getName() + "</aqua>\" could <red>not</red> be loaded: " + e.getMessage())));
                }
            }
        }

        if (messages.isEmpty()) {
            Bukkit.getServer().getConsoleSender().sendMessage(Prefixes.ERROR.append(MessageHelpers.getMiniMessage().deserialize("No messages could be loaded, shutting down server...")));
            Bukkit.shutdown();
        }
    }

    /**
     * Returns a message for the given key in the given language.
     * <ul>
     *  <li>If the language is not found, the message is returned in German.</li>
     *  <li>If the message is not found, an error message is returned.</li>
     *  <li>If the message is found, the placeholders are replaced.</li>
     * </ul>
     *
     * @param key The key of the message.
     * @param locale The language code of the message.
     * @param placeholderValues The values to replace the placeholders in the message.
     *
     * @return The message as a Component.
     */
    public Component getMessage(Messages key, String locale, String... placeholderValues) {
        MessageWrapper<Messages> messages = this.messages.get(locale);

        if (messages == null) {
            // German fallback, if the language is not found
            messages = this.messages.get("de");
        }

        if (messages == null) {
            return MessageHelpers.getMiniMessage().deserialize("<red><b>[Found no message for key \"<aqua>" + key + "</aqua>\" in language \"<aqua>" + locale + "</aqua>\", please inform an team member.]</red></b>");
        }

        String message = messages.getMessage(key);
        return Placeholders.parseMessage(message, key, locale, placeholderValues);
    }

    /**
     * Returns the raw message for the given key in the given language, without any formatting or MiniMessage tags.
     * Placeholders are replaced, colors will be removed.
     *
     * @param key The key of the message.
     * @param languageCode The language code of the message.
     * @param placeholderValues The values to replace the placeholders in the message.
     *
     * @return The raw message as a String.
     */
    public String getRawMessage(Messages key, String languageCode, String... placeholderValues) {
        return MessageHelpers.getRawMessageFromComponent(getMessage(key, languageCode, placeholderValues));
    }

    /**
     * Returns the message for the given key in the given language as a serialized MiniMessage string.
     *
     * @param key The key of the message.
     * @param languageCode The language code of the message.
     * @param placeholderValues The values to replace the placeholders in the message.
     *
     * @return The message as a serialized MiniMessage string.
     */
    public String getMessageString(Messages key, String languageCode, String... placeholderValues) {
        return MessageHelpers.getMiniMessage().serialize(getMessage(key, languageCode, placeholderValues));
    }

    /**
     * Returns a message for the given key in the given language.
     *
     * @param languageCode The language code of the message.
     *
     * @return The language name.
     */
    public String getLanguageFromCode(String languageCode) {
        MessageWrapper<Messages> messages = this.messages.get(languageCode);

        if (messages == null) {
            // Fallback to German
            messages = this.messages.get("de");
        }

        if (messages == null) {
            return "<red><b>[Found no language name for languageCode \"<aqua>" + languageCode + "</aqua>\", please inform an team member.]</red></b>";
        }

        return messages.getLanguage();
    }

    /**
     * Returns the language codes of all loaded languages.
     *
     * @return The language codes of all loaded languages.
     */
    public List<String> getLocales() {
        return new ArrayList<>(messages.keySet());
    }
}
