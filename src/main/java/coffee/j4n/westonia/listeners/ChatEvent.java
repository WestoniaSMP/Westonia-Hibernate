package coffee.j4n.westonia.listeners;

import coffee.j4n.westonia.BasePlayer;
import coffee.j4n.westonia.Westonia;
import coffee.j4n.westonia.utils.Helpers;
import coffee.j4n.westonia.utils.guis.ConfirmationGui;
import coffee.j4n.westonia.utils.guis.management.GuiBuilder;
import coffee.j4n.westonia.utils.messages.Messages;
import coffee.j4n.westonia.utils.messages.management.MessageHelpers;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Listener for chat events.
 */
public class ChatEvent implements Listener {

    private final Westonia plugin;
    private GuiBuilder guiBuilder;

    public ChatEvent(Westonia plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onChat(AsyncChatEvent event) {
        BasePlayer basePlayer = plugin.getPlayerHandler().getPlayer(event.getPlayer().getUniqueId());

        assert basePlayer != null;

        // We don't want the player to chat while we're still loading the player data
        if (!basePlayer.isInitialized()) {
            event.setCancelled(true);
            basePlayer.sendWestoniaMessage(Messages.PLAYER_JOIN_WAIT_WHILE_LOADING);
            return;
        }

        basePlayer.getPlayer().sendMessage("event.isAsynchronous() --> " + event.isAsynchronous());

        if (MessageHelpers.getRawMessageFromComponent(event.message()).equalsIgnoreCase("debug gui")) {

            ConfirmationGui confirmationGui = new ConfirmationGui(plugin, "debug gui");

            Helpers.runOnMainThread(() -> {
                confirmationGui.openConfirmationGui(basePlayer);
            });


//            GuiBuilder playerMenuMain = new GuiBuilder(plugin, Component.text("<purple>Spielermenü</purple>"), InventorySize.SMALL);
//
//            playerMenuMain.addPage(page -> {}).addSubMenuButton(10, createItem(Material.DIAMOND, Component.text("<green>Survival</green>")), survivalMenuMain -> {
//                survivalMenuMain.addPage(page -> {
//                    page.setItem(10);
//
//                })
//            });
//
//
//            Helpers.runOnMainThread(() -> {
//                guiBuilder.open(basePlayer.getPlayer(), 0);
//                guiBuilder.startUpdating(basePlayer.getPlayer(), 0, 20); // Update every second (20 ticks)
//            });
        }
    }
}