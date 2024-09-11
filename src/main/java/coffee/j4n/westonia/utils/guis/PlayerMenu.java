package coffee.j4n.westonia.utils.guis;

import coffee.j4n.westonia.BasePlayer;
import coffee.j4n.westonia.Westonia;
import coffee.j4n.westonia.utils.GuiHelpers;
import coffee.j4n.westonia.utils.guis.management.GuiBuilder;
import coffee.j4n.westonia.utils.messages.management.MessageHelpers;
import coffee.j4n.westonia.utils.statics.enums.InventorySize;
import org.bukkit.Material;

import java.util.List;
import java.util.stream.Collectors;

public class PlayerMenu {

    private final Westonia plugin;
    private final BasePlayer basePlayer;

    private final GuiBuilder playerMenuMain;

    private final GuiBuilder languageSubMenu;
    //private final GuiBuilder statisticsSubMenu;
    //private final GuiBuilder clanSettingsSubMenu;
    //private final GuiBuilder allieManagementSubMenu;
    //private final GuiBuilder playerRecordsSubMenu;
    //private final GuiBuilder moderationSubMenu;
    //private final GuiBuilder administrationSubMenu;

    public PlayerMenu(Westonia plugin, BasePlayer basePlayer) {
        this.plugin = plugin;
        this.basePlayer = basePlayer;

        this.playerMenuMain = createPlayerMenuMain();
        this.languageSubMenu = createLanguageSubMenu();
        //this.statisticsSubMenu = createStatisticsSubMenu();
        //this.clanSettingsSubMenu = createClanSettingsSubMenu();
        //this.allieManagementSubMenu = createAllieManagementSubMenu();
        //this.playerRecordsSubMenu = createPlayerRecordsSubMenu();
        //this.moderationSubMenu = createModerationSubMenu();
        //this.administrationSubMenu = createAdministrationSubMenu();
    }

    private GuiBuilder createPlayerMenuMain() {
        GuiBuilder playerMenuGui = new GuiBuilder(plugin, MessageHelpers.getMiniMessage().deserialize("<gray><purple>Playermenu</purple>"));


        playerMenuGui.addPage(MessageHelpers.getMiniMessage().deserialize("Startseite"), InventorySize.CHEST_4ROW, page -> {
            page.setItem(10, GuiHelpers.createItem(Material.BOOK, MessageHelpers.getMiniMessage().deserialize("<green><b>Language</b></green>")), clickEvent -> {
                languageSubMenu.open(basePlayer);
            });

            page.setItem(12, GuiHelpers.createItem(Material.EXPERIENCE_BOTTLE, MessageHelpers.getMiniMessage().deserialize("<green><b>Statistics</b></green>")), clickEvent -> {
                //statisticsSubMenu.open(basePlayer);
            });


        });


        return playerMenuMain;
    }

    private GuiBuilder createLanguageSubMenu() {
        GuiBuilder languageGui = new GuiBuilder(plugin, MessageHelpers.getMiniMessage().deserialize("<gray><blue>Language</blue>"));

        List<String> locales = plugin.getMessageFactory().getLocales();
        List<String> languages = locales.stream().map(locale -> plugin.getMessageFactory().getLanguageFromCode(locale)).toList();

        return null;
    }
}
