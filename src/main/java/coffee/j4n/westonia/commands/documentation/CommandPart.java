package coffee.j4n.westonia.commands.documentation;

import coffee.j4n.westonia.utils.statics.enums.GradientType;
import coffee.j4n.westonia.utils.messages.management.MessageHelpers;
import coffee.j4n.westonia.utils.statics.enums.ArgumentRequirement;
import coffee.j4n.westonia.utils.statics.enums.ArgumentType;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a part of a command that is used for documentation.
 */
public class CommandPart {

    // <editor-fold desc="Constructor and Fields">
    /**
     * The name of the command part.
     */
    private final String name;

    /**
     * The description of the command part.
     */
    private final String description;
    /**
     * The type of the command part.
     */
    private final ArgumentType type;
    /**
     * The requirement of the command part.
     */
    private final ArgumentRequirement requirement;
    /**
     * The children of this command part.
     */
    private final List<CommandPart> children = new ArrayList<>();

    /**
     * Creates a new command part.
     * @param name The name of the command part.
     * @param description The description of the command part.
     * @param type The type of the command part.
     * @param requirement The requirement of the command part.
     */
    public CommandPart(String name, String description, ArgumentType type, ArgumentRequirement requirement) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.requirement = requirement;
    }
    // </editor-fold>


    // <editor-fold desc="Methods">
    /**
     * Adds a child to this command part.
     *
     * @param child The child to add.
     * @return The same command part for chaining.
     */
    public CommandPart addChild(CommandPart child) {
        this.children.add(child);
        return this;
    }

    /**
     * Formats the current command part as a string.
     *
     * @param name The name of the command part.
     * @param type The type of the command part.
     * @param requirement The requirement of the command part.
     * @param description The description of the command part.
     * @return The formatted command part as a string with MiniMessage formatting tags.
     */
    private String formatPart(String name, ArgumentType type, ArgumentRequirement requirement, String description) {
        // First: Wrap the name in a hover text with the description
        String hoverText = "<hover:show_text:'<gray>" + description + "</gray>'><i>" + name + "</i></hover>";

        // Then: Apply the gradient to the hover text based on the type
        String formattedPart = switch (type) {
            case BOOLEAN, STRING, INTEGER_OR_RESET -> null;
            case STATIC -> MessageHelpers.applyGradientToString(hoverText, GradientType.STATIC);
            case PLAYER -> MessageHelpers.applyGradientToString(hoverText, GradientType.PLAYER);
            case NAME -> MessageHelpers.applyGradientToString(hoverText, GradientType.NAME);
            case INTEGER -> MessageHelpers.applyGradientToString(hoverText, GradientType.INTEGER);
            case FLOAT -> MessageHelpers.applyGradientToString(hoverText, GradientType.FLOAT);
        };

        // Finally: Return the formatted part based on the requirement
        return switch (requirement) {
            case STATIC -> formattedPart;
            case BASE -> "<dark_gray>/</dark_gray>" + formattedPart;
            case OPTIONAL -> "<dark_gray><</dark_gray>" + formattedPart + "<dark_gray>></dark_gray>";
            case REQUIRED -> "<dark_gray>[</dark_gray>" + formattedPart + "<dark_gray>]</dark_gray>";
        };
    }

    /**
     * Finds a command by its path and returns it as a string with MiniMessage formatting tags.
     * If the command is not found, a message is returned that informs the user about the missing command.
     *
     * @param path The path of the command where each part is separated by a slash (e.g. "fly/speed").
     * @return The formatted command as a string with MiniMessage formatting tags.
     */
    public String findCommandByPath(String path) {
        String[] parts = path.split("/");

        if (parts.length == 1) {
            return formatPart(this.name, this.type, this.requirement, this.description);
        }

        return findCommandByPath(parts, 0);
    }

    /**
     * Finds a command recursively by its path and returns it as a string with MiniMessage formatting tags.
     * @param parts The parts of the path as an array.
     * @param index The current index in the parts array.
     * @return The formatted command as a string with MiniMessage formatting tags.
     */
    private String findCommandByPath(String[] parts, int index) {
        if (index >= parts.length) {
            // If we are at the end of the parts array, return an empty string
            return "";
        }

        String currentPart = parts[index];

        // If the current part matches the name of this command part, we continue searching in the children
        if (this.name.equalsIgnoreCase(currentPart)) {
            // If we are at the end of the parts array, we return the current part
            if (index == parts.length - 1) {
                return formatPart(this.name, this.type, this.requirement, this.description);
            }

            // Otherwise, we search for the next part in the children
            for (CommandPart child : children) {
                if (child.name.equalsIgnoreCase(parts[index + 1])) {
                    return formatPart(this.name, this.type, this.requirement, this.description) + " " + child.findCommandByPath(parts, index + 1);
                }
            }
        }

        // If we did not find the current part in the children, we return an error message
        return formatPart(this.name, this.type, this.requirement, this.description) + " <red><b>!!!</b></red><gray><b>Could not find argument \"<aqua>" + parts[index + 1] + "</aqua>\".<b></gray><red><b>!!!</b></red>";
    }
    // </editor-fold>


    // <editor-fold desc="Debug">
    /**
     * Returns the command part as a string for the console.
     * @return The command part as a string for the console.
     */
    public String toConsoleString() {
        StringBuilder sb = new StringBuilder();
        toConsoleString(sb, 0);
        return sb.toString();
    }

    /**
     * Recursively appends the command part to the string builder with the given level of indentation.
     *
     * @param sb The string builder to append the command part to.
     * @param level The current level of indentation.
     */
    private void toConsoleString(StringBuilder sb, int level) {
        String indent = "  ".repeat(level);
        sb.append(indent).append(name).append(": ").append(description).append("\n");
        for (CommandPart child : children) {
            child.toConsoleString(sb, level + 1);
        }
    }
    // </editor-fold>
}

