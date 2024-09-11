/**
 * Copyright (c) 2024 J4N
 * This code is licensed under GNU GPLv3 license.
 * For more information, please refer to the LICENSE file.
 */

package coffee.j4n.westonia.commands;

import coffee.j4n.westonia.BasePlayer;
import coffee.j4n.westonia.Westonia;
import coffee.j4n.westonia.interfaces.commands.IPluginCommandExecutor;
import coffee.j4n.westonia.utils.messages.Messages;
import coffee.j4n.westonia.utils.statics.constants.Permissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The Fly command.
 * This command allows players to toggle their flight mode and set their flight speed.
 * <p>
 * Syntax:
 * <ul>
 *     <li>/fly - Toggle the flight mode</li>
 *     <li>/fly <player> - Toggle the flight mode for the specified player</li>
 *     <li>/fly speed <speed (1-10) | reset> - Set or reset the flight speed</li>
 *     <li>/fly speed <speed (1-10) | reset> <player> - Set or reset the flight speed for the specified player</li>
 * </ul>
 * <p>
 * Permissions:
 * <ul>
 *     <li>westonia.fly - Allows the player to toggle the flight mode for themselves</li>
 *     <li>westonia.fly.others - Allows the player to toggle the flight mode for other players</li>
 *     <li>westonia.fly.speed - Allows the player to set the flight speed for themselves</li>
 *     <li>westonia.fly.speed.others - Allows the player to set the flight speed for other players</li>
 * </ul>
 */
public class Fly implements IPluginCommandExecutor<BasePlayer, Westonia> {

    private static final String NAME = "fly";

    /**
     * Executes the command /fly
     */
    @Override
    public Boolean onCommand(BasePlayer basePlayer, Command command, String[] arguments, Westonia plugin) {
        // /fly
        if (arguments.length == 0) {
            if (!basePlayer.hasPermission(Permissions.FLY)) return false;

            if (basePlayer.getPlayer().isFlying()) {
                basePlayer.getPlayer().setAllowFlight(false);
                basePlayer.getPlayer().setFlying(false);

                basePlayer.sendInfoMessage(Messages.COMMANDS_FLY_ACTIVE_SELF);

                return true;
            } else {
                basePlayer.getPlayer().setAllowFlight(true);
                basePlayer.getPlayer().setFlying(true);

                basePlayer.sendInfoMessage(Messages.COMMANDS_FLY_INACTIVE_SELF);

                return true;
            }
        }

        // /fly speed <speed (0-10) | reset> <player>
        if (arguments[0].equalsIgnoreCase("speed")) {
            if (!basePlayer.hasPermission(Permissions.FLY_SPEED)) return false;

            if (arguments.length == 1) {
                basePlayer.sendErrorMessage(Messages.COMMANDS_FLY_NOTIFICATIONS_SPEED_MISSING_ARGUMENTS);
                return false;
            }

            boolean setForTargetPlayer = arguments.length == 3;
            int speedToBeSet = 1;

            if (arguments[1].equalsIgnoreCase("reset") || arguments[1].equalsIgnoreCase("default")) {
                if (setForTargetPlayer) {
                    if (!basePlayer.hasPermission(Permissions.FLY_SPEED_OTHERS)) return false;

                    BasePlayer targetPlayer = plugin.getPlayerHandler().getPlayer(arguments[2]);

                    if (targetPlayer == null) {
                        basePlayer.sendErrorMessage(Messages.COMMON_PLAYERNOTONLINE, arguments[2]);
                        return false;
                    }

                    targetPlayer.getPlayer().setFlySpeed(0.1f);
                    basePlayer.sendInfoMessage(Messages.COMMANDS_FLY_SPEED_OTHERS_DEFAULT, targetPlayer.getPlayer().getName());
                    targetPlayer.sendInfoMessage(Messages.COMMANDS_FLY_SPEED_TARGET_DEFAULT, basePlayer.getPlayer().getName());
                    return true;
                }

                basePlayer.getPlayer().setFlySpeed(0.1f);
                basePlayer.sendInfoMessage(Messages.COMMANDS_FLY_SPEED_SELF_DEFAULT);
                return true;
            }

            try {
                speedToBeSet = Integer.parseInt(arguments[1]);
            } catch (NumberFormatException e) {
                basePlayer.sendErrorMessage(Messages.COMMANDS_FLY_NOTIFICATIONS_SPEED_NOT_A_NUMBER, arguments[1]);
                return false;
            }

            if (speedToBeSet < 1 || speedToBeSet > 10) {
                basePlayer.sendErrorMessage(Messages.COMMANDS_FLY_NOTIFICATIONS_SPEED_MUST_BE_NUMBER_BETWEEN);
                return false;
            }

            if (setForTargetPlayer) {
                if (!basePlayer.hasPermission(Permissions.FLY_SPEED_OTHERS)) return false;

                BasePlayer targetPlayer = plugin.getPlayerHandler().getPlayer(arguments[2]);

                if (targetPlayer == null) {
                    basePlayer.sendErrorMessage(Messages.COMMON_PLAYERNOTONLINE, arguments[2]);
                    return false;
                }

                targetPlayer.getPlayer().setFlySpeed((float) speedToBeSet / 10);
                basePlayer.sendInfoMessage(Messages.COMMANDS_FLY_SPEED_OTHERS, targetPlayer.getPlayer().getName(), String.valueOf(speedToBeSet));
                targetPlayer.sendInfoMessage(Messages.COMMANDS_FLY_SPEED_TARGET, basePlayer.getPlayer().getName(), basePlayer.getPlayer().getName(), String.valueOf(speedToBeSet));
                return true;
            } else {
                basePlayer.getPlayer().setFlySpeed((float) speedToBeSet / 10);
                basePlayer.sendInfoMessage(Messages.COMMANDS_FLY_SPEED_SELF, String.valueOf(speedToBeSet));
                return true;
            }
        }

        // /fly <player>
        if (arguments.length == 1 && !arguments[0].equalsIgnoreCase("speed")) {
            if (!basePlayer.hasPermission(Permissions.FLY_OTHERS)) return false;

            BasePlayer targetPlayer = plugin.getPlayerHandler().getPlayer(arguments[0]);

            if (targetPlayer == null) {
                basePlayer.sendErrorMessage(Messages.COMMON_PLAYERNOTONLINE, arguments[0]);
                return false;
            }

            if (targetPlayer.getPlayer().isFlying()) {
                // Disable flight mode for the target player
                basePlayer.getPlayer().setAllowFlight(false);
                targetPlayer.getPlayer().setFlying(false);

                basePlayer.sendInfoMessage(Messages.COMMANDS_FLY_INACTIVE_OTHERS, targetPlayer.getPlayer().getName());
                targetPlayer.sendInfoMessage(Messages.COMMANDS_FLY_INACTIVE_TARGET, basePlayer.getPlayer().getName());

                return true;
            } else {
                // Enable flight mode for the target player
                basePlayer.getPlayer().setAllowFlight(true);
                targetPlayer.getPlayer().setFlying(true);

                basePlayer.sendInfoMessage(Messages.COMMANDS_FLY_ACTIVE_OTHERS, targetPlayer.getPlayer().getName());
                targetPlayer.sendInfoMessage(Messages.COMMANDS_FLY_ACTIVE_TARGET, basePlayer.getPlayer().getName());

                return true;
            }
        }

        return false;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(commandSender instanceof Player)) {
            return Collections.emptyList();
        }

        Player player = (Player) commandSender;
        if (args.length == 1) {
            // Vorschläge für das erste Argument
            List<String> suggestions = new ArrayList<>();
            suggestions.add("speed");
            suggestions.add("deine MUM!!!!!!");
            if (player.hasPermission("yourplugin.fly.others")) {
                suggestions.addAll(player.getServer().getOnlinePlayers().stream()
                        .map(Player::getName)
                        .collect(Collectors.toList()));
            }
            return suggestions.stream().filter(sug -> sug.toLowerCase().startsWith(args[0].toLowerCase())).collect(Collectors.toList());
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("speed")) {
            // Vorschläge für das zweite Argument bei "/fly speed"
            return Stream.of("reset", "default", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10")
                    .filter(speedSug -> speedSug.toLowerCase().startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("speed") && player.hasPermission("yourplugin.fly.speed.others")) {
            // Vorschläge für das dritte Argument bei "/fly speed <speed> <player>"
            return player.getServer().getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(playerSug -> playerSug.toLowerCase().startsWith(args[2].toLowerCase()))
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }
}