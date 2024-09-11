package coffee.j4n.westonia.listeners;

import coffee.j4n.westonia.BasePlayer;
import coffee.j4n.westonia.Westonia;
import coffee.j4n.westonia.utils.messages.Messages;
import coffee.j4n.westonia.utils.messages.management.MessageHelpers;
import coffee.j4n.westonia.utils.statics.constants.Prefixes;
import coffee.j4n.westonia.utils.statics.enums.GradientType;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerJoinEvent implements Listener {

    private final Westonia plugin;

    public PlayerJoinEvent(Westonia plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBasePlayerJoin(org.bukkit.event.player.PlayerJoinEvent event) {

        event.joinMessage(null);
        BasePlayer basePlayer = new BasePlayer(event.getPlayer(), plugin.getNetworkPlayerDao(), plugin.getRunnableManager());
        this.plugin.getPlayerHandler().addPlayer(basePlayer);

        basePlayer.sendWestoniaMessage(Messages.PLAYER_JOIN_DATA_WILL_BE_SYNCED);

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            basePlayer.init();

            basePlayer.sendWestoniaMessage(Messages.PLAYER_JOIN_DATA_SYNCED);

            basePlayer.sendSpecialMessage(Messages.COMMON_HEADER, Prefixes.WESTONIA_RAW_STRING);
            basePlayer.sendArrowMessage(Messages.PLAYER_JOIN_WELCOME_MESSAGE, basePlayer.getPlayer().getName(), basePlayer.getLanguageHumanFriendly());

            Bukkit.broadcast(MessageHelpers.applyPrefixTemplate(MessageHelpers.getMiniMessage().deserialize("<green><b>+</b></green>")).append(MessageHelpers.getMiniMessage().deserialize(MessageHelpers.applyGradientToString(basePlayer.getPlayer().getName(), GradientType.PLAYER))).color(NamedTextColor.GOLD));

            basePlayer.sendDebugMessage("Stelle Sprache um... (aktuell: " + basePlayer.getCurrentLocale() + "[" + basePlayer.getLanguageHumanFriendly() + "])");

            if (basePlayer.getCurrentLocale().equalsIgnoreCase("de")) {
                basePlayer.setCurrentLocale("en");
            } else {
                basePlayer.setCurrentLocale("de");
            }
        });
    }
}