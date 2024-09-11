package coffee.j4n.westonia.utils.messages.management;

import coffee.j4n.westonia.utils.statics.enums.GradientType;
import coffee.j4n.westonia.utils.statics.enums.MessageArgumentType;

import java.util.List;

/**
 * Represents an argument of a command given in {COMMAND} placeholders.
 */
public class MessageArgument {
    /**
     * The type of the argument.
     */
    private final MessageArgumentType type;

    /**
     * The gradient type of the argument.
     */
    private final GradientType gradientType;

    /**
     * The arguments of the command.
     */
    private final List<String> arguments;

    /**
     * Creates a new message argument.
     *
     * @param type The type of the argument.
     * @param arguments The arguments of the command.
     * @param gradientType The gradient type of the argument.
     */
    public MessageArgument(MessageArgumentType type, List<String> arguments, GradientType gradientType) {
        this.type = type;
        this.arguments = arguments;
        this.gradientType = gradientType;
    }

    /**
     * Returns the type of the argument.
     *
     * @return The type of the argument.
     */
    public MessageArgumentType getType() {
        return type;
    }

    /**
     * Returns the gradient type of the argument.
     *
     * @return The gradient type of the argument.
     */
    public GradientType getGradientType() {
        return gradientType;
    }

    /**
     * Returns the arguments of the command.
     *
     * @return The arguments of the command.
     */
    public List<String> getArguments() {
        return arguments;
    }

    /**
     * Returns the string representation of the message argument.
     * The format is: "MessageArgument{type=..., arguments=...}"
     *
     * @return The string representation of the message argument.
     */
    @Override
    public String toString() {
        return "MessageArgument{" +
                "type=" + type +
                ", arguments=" + arguments +
                '}';
    }
}