package coffee.j4n.westonia.utils.guis;

import coffee.j4n.westonia.BasePlayer;
import coffee.j4n.westonia.Westonia;
import coffee.j4n.westonia.utils.GuiHelpers;
import coffee.j4n.westonia.utils.guis.management.GuiBuilder;
import coffee.j4n.westonia.utils.messages.management.MessageHelpers;
import coffee.j4n.westonia.utils.statics.enums.InventorySize;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

public class ConfirmationGui {

    private final Westonia plugin;
    private final String actionName;

    private final GuiBuilder confirmationGui;

    public ConfirmationGui(Westonia plugin, String actionName) {
        this.plugin = plugin;
        this.actionName = actionName;

        this.confirmationGui = createConfirmationGui();
    }

    public void openConfirmationGui(BasePlayer basePlayer) {
        confirmationGui.open(basePlayer);

    }

    private GuiBuilder createConfirmationGui() {
        GuiBuilder confirmationGui = new GuiBuilder(plugin, MessageHelpers.getMiniMessage().deserialize("<gray>Please confirm: <dark_aqua>" + actionName + "</dark_aqua></gray>"));

        confirmationGui.addPage(null, InventorySize.CHEST_3ROW, page -> {
            page.setItem(11, GuiHelpers.createItem(Material.GREEN_CONCRETE, MessageHelpers.getMiniMessage().deserialize("<green><b>CONFIRM</b></green>")), clickEvent -> {
                BasePlayer basePlayer = plugin.getPlayerHandler().getPlayer(clickEvent.getWhoClicked().getUniqueId());

                assert basePlayer != null;
                basePlayer.sendDebugMessage("Confirmed action: " + actionName);
            });

            page.setItem(15, GuiHelpers.createItem(Material.RED_CONCRETE, MessageHelpers.getMiniMessage().deserialize("<red><b>CANCEL</b></red>")), clickEvent -> {
                BasePlayer basePlayer = plugin.getPlayerHandler().getPlayer(clickEvent.getWhoClicked().getUniqueId());

                assert basePlayer != null;
                basePlayer.sendDebugMessage("Cancelled action: " + actionName);
            });
        });

        confirmationGui.addPage(Component.text("TEST"), InventorySize.CHEST_6ROW, page -> {
            page.setItem(11, GuiHelpers.createItem(Material.GREEN_CONCRETE, MessageHelpers.getMiniMessage().deserialize("<green><b>CONFIRM</b></green>")), clickEvent -> {
                BasePlayer basePlayer = plugin.getPlayerHandler().getPlayer(clickEvent.getWhoClicked().getUniqueId());

                assert basePlayer != null;
                basePlayer.sendDebugMessage("Confirmed action: " + actionName);
            });

            page.setItem(15, GuiHelpers.createItem(Material.RED_CONCRETE, MessageHelpers.getMiniMessage().deserialize("<red><b>CANCEL</b></red>")), clickEvent -> {
                BasePlayer basePlayer = plugin.getPlayerHandler().getPlayer(clickEvent.getWhoClicked().getUniqueId());

                assert basePlayer != null;
                basePlayer.sendDebugMessage("Cancelled action: " + actionName);
            });
        });
        return confirmationGui;
    }
}
