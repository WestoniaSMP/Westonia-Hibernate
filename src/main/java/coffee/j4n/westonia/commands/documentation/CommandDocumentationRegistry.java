package coffee.j4n.westonia.commands.documentation;

import coffee.j4n.westonia.Westonia;
import coffee.j4n.westonia.utils.messages.Messages;
import coffee.j4n.westonia.utils.statics.enums.ArgumentRequirement;
import coffee.j4n.westonia.utils.statics.enums.ArgumentType;

/**
 * The CommandDocumentationRegistry is a registry for all commands in the plugin. It is used to document all commands and their arguments.
 */
public final class CommandDocumentationRegistry {

    static String getMessage(Messages messageKey, String locale, String... placeholderValues) {
        return Westonia.getInstance().getMessageFactory().getMessageString(messageKey, locale, placeholderValues);
    }

    // <editor-fold defaultstate="collapsed" desc="Command Documentation">
    private static CommandPart FLY(String locale) {
        CommandPart flyBase = new CommandPart("Fly", getMessage(Messages.COMMAND_DOCUMENTATION_REGISTRY_FLY_BASE, locale), ArgumentType.STATIC, ArgumentRequirement.BASE);


        // /Fly Speed [Speed] <Player>
        flyBase.addChild(new CommandPart("Speed", getMessage(Messages.COMMAND_DOCUMENTATION_REGISTRY_FLY_SPEED, locale), ArgumentType.STATIC, ArgumentRequirement.STATIC)
                .addChild(new CommandPart("Speed", getMessage(Messages.COMMAND_DOCUMENTATION_REGISTRY_FLY_SPEED_SPEED, locale), ArgumentType.INTEGER, ArgumentRequirement.REQUIRED)
                        .addChild(new CommandPart("Player", getMessage(Messages.COMMAND_DOCUMENTATION_REGISTRY_FLY_SPEED_SPEED_PLAYER, locale), ArgumentType.PLAYER, ArgumentRequirement.OPTIONAL))));

        // /Fly <Player>
        flyBase.addChild(new CommandPart("Player", getMessage(Messages.COMMAND_DOCUMENTATION_REGISTRY_FLY_PLAYER, locale), ArgumentType.PLAYER, ArgumentRequirement.OPTIONAL));


        return flyBase;
    }

    private static CommandPart GAMEMODE(String locale) {

        // /Gamemode [Gamemode] <Player> <Time>
        CommandPart gamemodeBase = new CommandPart("Gamemode", getMessage(Messages.COMMAND_DOCUMENTATION_REGISTRY_GAMEMODE_BASE, locale), ArgumentType.STATIC, ArgumentRequirement.BASE);

        gamemodeBase.addChild(new CommandPart("Gamemode", getMessage(Messages.COMMAND_DOCUMENTATION_REGISTRY_GAMEMODE_GAMEMODE, locale), ArgumentType.NAME, ArgumentRequirement.REQUIRED)
                .addChild(new CommandPart("Player", getMessage(Messages.COMMAND_DOCUMENTATION_REGISTRY_GAMEMODE_GAMEMODE_PLAYER, locale), ArgumentType.PLAYER, ArgumentRequirement.OPTIONAL)));

        return gamemodeBase;
    }

    private static CommandPart GMC(String locale) {
        CommandPart gmcBase = new CommandPart("Gmc", "Ändert deinen Spielmodus zu Kreativ", ArgumentType.STATIC, ArgumentRequirement.BASE);

        return gmcBase;
    }
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="Helper Methods">

    private static CommandPart LANGUAGE(String locale) {
        CommandPart langBase = new CommandPart("Language", "!LOC!Öffnet die Sprachauswahl", ArgumentType.STATIC, ArgumentRequirement.BASE);

        return langBase;
    }

    /**
     * Finds a command by its path and return it as a formatted string.
     *
     * @param path The path of the command.
     * @return The formatted command.
     */
    public static String findCommandRaw(String path, String locale) {
        if (path.split("/").length > 0) {
            String commandName = path.split("/")[0];

            if (commandName.equalsIgnoreCase("fly")) {
                return CommandDocumentationRegistry.FLY(locale).findCommandByPath(path);
            } else if (commandName.equalsIgnoreCase(("gamemode")) || commandName.equalsIgnoreCase("gm")) {
                return CommandDocumentationRegistry.GAMEMODE(locale).findCommandByPath(path);
            } else if (commandName.equalsIgnoreCase(("language"))) {
                return CommandDocumentationRegistry.LANGUAGE(locale).findCommandByPath(path);
            }


            return "<red><b>!!!</b></red><gray><b>Found no command with name \"<aqua>" + commandName + "</aqua>\" in the <dark_purple>Command Documentation Registry</dark_purple>, please inform an team member.</b></gray><red><b>!!!</b></red>";
        } else {
            return "";
        }
    }
    // </editor-fold>
}
