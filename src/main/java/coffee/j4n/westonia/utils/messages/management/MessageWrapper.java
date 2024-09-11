package coffee.j4n.westonia.utils.messages.management;

import coffee.j4n.westonia.interfaces.IMessageBase;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * The MessageWrapper is used to map messages to a specific language.
 *
 * @param <MessageEnum> The enum of the messages, which should be mapped.
 */
public class MessageWrapper<MessageEnum extends Enum<MessageEnum> & IMessageBase> {
    private final Map<MessageEnum, String> messages;
    private final String language;

    /**
     * Creates a new message wrapper for the given messages and language.
     *
     * @param messages The messages.
     * @param language The language of the messages.
     */
    public MessageWrapper(@NotNull Map<MessageEnum, String> messages, String language) {
        this.messages = new HashMap<>(messages);
        this.language = language;
    }

    /**
     * Returns the message for the given key.
     *
     * @param key The key of the message.
     * @return The message.
     */
    public String getMessage(MessageEnum key) {
        return messages.get(key);
    }

    /**
     * Returns the language of the message.
     *
     * @return The language.
     */
    public String getLanguage() {
        return this.language;
    }
}
