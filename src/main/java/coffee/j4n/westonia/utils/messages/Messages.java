package coffee.j4n.westonia.utils.messages;

import coffee.j4n.westonia.interfaces.IMessageBase;

/**
 * This enum contains all yaml paths for the messages that are used in the plugin.
 */
public enum Messages implements IMessageBase {

    // Language
    LANGUAGE_NAME("languagename"),

    // Player Join
    PLAYER_JOIN_WELCOME_MESSAGE("player_join.welcome_message"),
    PLAYER_JOIN_DATA_WILL_BE_SYNCED("player_join.data_will_be_synced"),
    PLAYER_JOIN_DATA_SYNCED("player_join.data_synced"),
    PLAYER_JOIN_WAIT_WHILE_LOADING("player_join.wait_while_loading"),
    // Common,
    COMMON_HEADER("common.header"),
    COMMON_NOPERMISSION("common.nopermission"),
    COMMON_PLAYERNOTONLINE("common.playernotonline"),
    COMMON_INVALID_TIME_FORMAT("common.invalid_time_format"),
    // Player
    // Language
    PLAYER_LANGUAGE_ERROR_ON_SAVE("player.language.error_on_save"),
    PLAYER_LANGUAGE_SAVED("player.language.saved"),

    // Commands
    // <editor-fold defaultstate="collapsed" desc="Fly">
    // Fly
    COMMANDS_FLY_ACTIVE_SELF("commands.fly.active_self"),
    COMMANDS_FLY_INACTIVE_SELF("commands.fly.inactive_self"),

    COMMANDS_FLY_ACTIVE_OTHERS("commands.fly.active_others"),
    COMMANDS_FLY_INACTIVE_OTHERS("commands.fly.inactive_others"),

    COMMANDS_FLY_ACTIVE_TARGET("commands.fly.active_target"),
    COMMANDS_FLY_INACTIVE_TARGET("commands.fly.inactive_target"),

    // Fly Speed
    COMMANDS_FLY_SPEED_SELF("commands.fly.speed_self"),
    COMMANDS_FLY_SPEED_SELF_DEFAULT("commands.fly.speed_self_default"),

    COMMANDS_FLY_SPEED_OTHERS("commands.fly.speed_others"),
    COMMANDS_FLY_SPEED_OTHERS_DEFAULT("commands.fly.speed_others_default"),

    COMMANDS_FLY_SPEED_TARGET("commands.fly.speed_target"),
    COMMANDS_FLY_SPEED_TARGET_DEFAULT("commands.fly.speed_target_default"),

    // Notifications
    COMMANDS_FLY_NOTIFICATIONS_SPEED_MISSING_ARGUMENTS("commands.fly.notifications.speed_missing_arguments"),
    COMMANDS_FLY_NOTIFICATIONS_SPEED_NOT_A_NUMBER("commands.fly.notifications.speed_not_a_number"),
    COMMANDS_FLY_NOTIFICATIONS_SPEED_MUST_BE_NUMBER_BETWEEN("commands.fly.notifications.speed_must_be_number_between"),
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Gamemode">
    // Modes
    GAMEMODE_CREATIVE_MODE("commands.gamemode.creative_mode"),
    GAMEMODE_SURVIVAL_MODE("commands.gamemode.survival_mode"),
    GAMEMODE_ADVENTURE_MODE("commands.gamemode.adventure_mode"),
    GAMEMODE_SPECTATOR_MODE("commands.gamemode.spectator_mode"),

    // Actions
    GAMEMODE_CHANGED_SELF("commands.gamemode.mode_changed_self"),
    GAMEMODE_CHANGED_OTHERS("commands.gamemode.mode_changed_others"),
    GAMEMODE_CHANGED_TARGET("commands.gamemode.mode_changed_target"),

    // Notifications
    GAMEMODE_NOTIFICATIONS_MISSING_GAMEMODE("commands.gamemode.notifications.missing_gamemode"),
    GAMEMODE_NOTIFICATIONS_UNKNOWN_GAMEMODE("commands.gamemode.notifications.unknown_gamemode"),
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CDR">
    // Fly
    COMMAND_DOCUMENTATION_REGISTRY_FLY_BASE("command_documentation_registry.fly_base"),
    COMMAND_DOCUMENTATION_REGISTRY_FLY_SPEED("command_documentation_registry.fly_speed"),
    COMMAND_DOCUMENTATION_REGISTRY_FLY_SPEED_SPEED("command_documentation_registry.fly_speed"),
    COMMAND_DOCUMENTATION_REGISTRY_FLY_SPEED_SPEED_PLAYER("command_documentation_registry.fly_player"),
    COMMAND_DOCUMENTATION_REGISTRY_FLY_PLAYER("command_documentation_registry.fly_player"),

    // Gamemode
    COMMAND_DOCUMENTATION_REGISTRY_GAMEMODE_BASE("command_documentation_registry.gamemode.gamemode_base"),
    COMMAND_DOCUMENTATION_REGISTRY_GAMEMODE_GAMEMODE("command_documentation_registry.gamemode.gamemode_gamemode"),
    COMMAND_DOCUMENTATION_REGISTRY_GAMEMODE_GAMEMODE_PLAYER("command_documentation_registry.gamemode.gamemode_gamemode_player");
    // </editor-fold>


    private final String path;

    Messages(String path) {
        this.path = path;
    }

    /**
     * Returns the path of the yaml configuration.
     *
     * @return The path of the yaml configuration.
     */
    public String getYamlConfigurationPath() {
        return this.path;
    }
    }
