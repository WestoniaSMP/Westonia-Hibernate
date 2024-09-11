package coffee.j4n.westonia.utils.guis.management;

import coffee.j4n.westonia.BasePlayer;
import coffee.j4n.westonia.Westonia;
import coffee.j4n.westonia.utils.GuiHelpers;
import coffee.j4n.westonia.utils.messages.management.MessageHelpers;
import coffee.j4n.westonia.utils.statics.constants.NamespacedKeys;
import coffee.j4n.westonia.utils.statics.enums.InventorySize;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

import static coffee.j4n.westonia.utils.GuiHelpers.createItem;

/**
 * A class that allows the creation of custom GUI systems in Minecraft with various features
 * including dynamic content, progress bars, interactive buttons, and page navigation.
 */
public class GuiBuilder implements Listener {

    private final Westonia plugin;

    private final Component guiTitle;
    private final List<GuiPage> pages;

    private final Map<Integer, Consumer<InventoryClickEvent>> clickActions;
    private final Map<Player, BukkitRunnable> playerUpdateTasks; // Managing tasks per player
    private final Map<Player, Deque<Inventory>> navigationHistory = new HashMap<>();

    private boolean generatePageOverview = true;

    public GuiBuilder(Westonia plugin, Component guiTitle) {
        this.plugin = plugin;

        this.guiTitle = guiTitle.color(NamedTextColor.GRAY);
        this.pages = new ArrayList<>();
        this.clickActions = new HashMap<>();
        this.playerUpdateTasks = new HashMap<>();

        System.out.println("Handlers before: ");
        HandlerList.getRegisteredListeners(plugin).forEach(listener -> {
            System.out.println(listener.getListener());
        });

        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        System.out.println("Handlers after: ");
        HandlerList.getRegisteredListeners(plugin).forEach(listener -> {
            System.out.println(listener.getListener());
        });
    }

    /**
     * Opens the GUI for the given player.
     *
     * @param basePlayer The player to open the GUI for
     */
    public void open(BasePlayer basePlayer) {
        addPagination();

        basePlayer.getPlayer().openInventory(pages.getFirst().getInventory());
        basePlayer.getPlayer().playSound(basePlayer.getPlayer().getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0f, 1.0f);

    }

    /**
     * Opens the GUI for the given player at the specified page.
     *
     * @param basePlayer The player to open the GUI for
     * @param page       The page to open
     */
    public void open(BasePlayer basePlayer, int page) {
        addPagination();

        // Subtract 1 from the page number to get the correct index as we start counting pages at 1
        page -= 1;

        System.out.println("Page after subtraction: " + page);
        if (page < pages.size()) {
            System.out.println("Opening page...");
            basePlayer.getPlayer().openInventory(pages.get(page).getInventory());
            basePlayer.getPlayer().playSound(basePlayer.getPlayer().getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1.0f, 1.0f);
        }
    }

    public void openPageNavigationMenu(BasePlayer basePlayer, InventorySize size) {
        GuiBuilder pageOverview = new GuiBuilder(plugin, guiTitle);

        // Calculate the amount of pages needed for the page overview
        int pagesAmount = calculatePagesNeeded(pages.size(), size.getSize());
        List<Integer> usableSlots = GuiPage.determineUsableSlots(size);

        // Create a copy of the pages list to avoid concurrent modification
        List<GuiPage> pageCopies = new ArrayList<>(this.pages);

        // Create the page overview with the correct amount of pages
        for (int overviewPageIndex = 0; overviewPageIndex < pagesAmount; overviewPageIndex++) {
            pageOverview.addPage(
                    MessageHelpers.getMiniMessage().deserialize("<gray>Page overview <dark_aqua>" + (overviewPageIndex + 1) + "</dark_aqua>"),
                    size,
                    page -> {
                        // Place the page icons on the overview page
                        for (int usableSlotIndex = 0; usableSlotIndex < usableSlots.size() && usableSlotIndex < pageCopies.size(); usableSlotIndex++) {
                            int slot = usableSlots.get(usableSlotIndex);
                            ItemStack icon = pageCopies.get(usableSlotIndex).getIcon();

                            // Add a click event to open the corresponding page
                            int finalUsableSlotIndex = usableSlotIndex;
                            page.setItem(slot, icon, clickEvent -> {
                                basePlayer.getPlayer().openInventory(pageCopies.get(finalUsableSlotIndex).getInventory());
                                basePlayer.getPlayer().playSound(basePlayer.getPlayer().getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1.0f, 1.0f);
                            });
                        }

                        // Remove the used slots from the usable slots list
                        if (usableSlots.size() <= pageCopies.size()) {
                            pageCopies.subList(0, usableSlots.size()).clear();
                        }
                    }
            ).noPageOverview(); // Disable the page overview for the page overview itself
        }

        pageOverview.open(basePlayer);
    }


    /**
     * Adds a new page to the GUI with the given setup.
     * The setup is a consumer that takes an page as input and sets up the content of the page.
     * Every GuiBuilder must have at least one page that the developer must add manually.
     *
     * @param pageSetup The setup for the page
     * @return The GuiBuilder instance for chaining
     */
    public GuiBuilder addPage(@Nullable Component pageTitle, InventorySize size, Consumer<GuiPage> pageSetup) {
        GuiPage page;

        // Add 1 to the current page index to set the page index as we start counting pages at 1
        int pageIndex = pages.size() + 1;

        // Create a new inventory with the specified size and title (if the title is null, there will be no title)
        if (pageTitle == null) {
            page = new GuiPage(guiTitle, pageIndex, size);
        } else {
            page = new GuiPage(guiTitle.append(MessageHelpers.getMiniMessage().deserialize(" <dark_gray>[</dark_gray><dark_purple>-</dark_purple><dark_gray>]</dark_gray> ").append(pageTitle)), pageIndex, size);
        }

        // Apply the setup to the page
        pageSetup.accept(page);
        page.applyOutlines();

        pages.add(page);
        return this;
    }

    /**
     * Adds pagination to the GUI.
     */
    private void addPagination() {
        int pageCount = pages.size();

        if (pageCount <= 1) return;

        // loop through all pages and add pagination
        pages.forEach(page -> {
            if (page.hasPagination()) return;

            int currentPageIndex = page.getPageIndex();

            if (generatePageOverview) {
                // Page info button for every page
                ItemStack pageInfoButton = createItem(Material.PAPER, MessageHelpers.getMiniMessage().deserialize("<gray>Page <dark_aqua>" + currentPageIndex + "</dark_aqua><dark_gray>/</dark_gray><dark_aqua>" + pageCount + "<dark_aqua>"));
                GuiHelpers.addNBTTag(pageInfoButton, NamespacedKeys.GUI_PAGINATION_PAGE_INFO_BUTTON, "page_info_" + currentPageIndex);
                GuiHelpers.setLore(pageInfoButton, MessageHelpers.getMiniMessage().deserialize("<gray>Use your <green>middle mouse button</green> to open the page overview."));

                page.setItem(page.getSize() - 5, pageInfoButton, clickEvent -> {
                    String nbtTag = GuiHelpers.getNBTTag(Objects.requireNonNull(clickEvent.getCurrentItem()), NamespacedKeys.GUI_PAGINATION_PAGE_INFO_BUTTON);

                    if (!clickEvent.getClick().equals(ClickType.MIDDLE) ||
                            clickEvent.getSlot() != page.getSize() - 5 ||
                            nbtTag == null ||
                            !nbtTag.equals("page_info_" + currentPageIndex)) {
                        return;
                    }

                    clickEvent.setCancelled(true);
                    BasePlayer basePlayer = plugin.getPlayerHandler().getPlayer(clickEvent.getWhoClicked().getUniqueId());

                    assert basePlayer != null;
                    openPageNavigationMenu(basePlayer, InventorySize.CHEST_3ROW);
                });
            }

            // Previous button for the second to the last page
            if (currentPageIndex != 1) {
                int previousPageIndex = currentPageIndex - 1;

                ItemStack previousPageButton = createItem(Material.ARROW, MessageHelpers.getMiniMessage().deserialize("<green>Previous Page</green>"));
                GuiHelpers.addNBTTag(previousPageButton, NamespacedKeys.GUI_PAGINATION_BACK_BUTTON, "back_button_" + previousPageIndex);

                if (page.getTitle() == null) {
                    GuiHelpers.setLore(previousPageButton, MessageHelpers.getMiniMessage().deserialize("<gray>Navigates you back to page <dark_aqua>" + previousPageIndex + "</dark_aqua>"));
                } else {
                    GuiHelpers.setLore(previousPageButton, MessageHelpers.getMiniMessage().deserialize("<gray>Navigates you back to the page <dark_aqua>" + previousPageIndex + "</dark_aqua> (<dark_aqua>").append(page.getTitle()).append(MessageHelpers.getMiniMessage().deserialize("</dark_aqua>)</gray>")));
                }

                page.setItem(page.getSize() - 6, previousPageButton, clickEvent -> {

                    String nbtTag = GuiHelpers.getNBTTag(Objects.requireNonNull(clickEvent.getCurrentItem()), NamespacedKeys.GUI_PAGINATION_BACK_BUTTON);

                    if (clickEvent.getSlot() != page.getSize() - 6 || nbtTag == null || !nbtTag.equals("back_button_" + previousPageIndex)) {
                        return;
                    }

                    clickEvent.setCancelled(true);
                    BasePlayer basePlayer = plugin.getPlayerHandler().getPlayer(clickEvent.getWhoClicked().getUniqueId());

                    assert basePlayer != null;
                    open(basePlayer, currentPageIndex - 1);
                });
            }

            // Next button for the first to the next to last page
            if (currentPageIndex - 1 != pageCount ) {
                int nextPageIndex = currentPageIndex + 1;

                ItemStack nextPageButton = createItem(Material.ARROW, MessageHelpers.getMiniMessage().deserialize("<green>Next Page</green>"));
                GuiHelpers.addNBTTag(nextPageButton, NamespacedKeys.GUI_PAGINATION_NEXT_BUTTON, "next_button_" + nextPageIndex);

                System.out.println("Meta set with NBT for next button: " + GuiHelpers.getNBTTag(nextPageButton, NamespacedKeys.GUI_PAGINATION_NEXT_BUTTON));

                if (page.getTitle() == null) {
                    GuiHelpers.setLore(nextPageButton, MessageHelpers.getMiniMessage().deserialize("<gray>Navigates you to page <dark_aqua>" + nextPageIndex + "</dark_aqua>"));
                } else {
                    GuiHelpers.setLore(nextPageButton, MessageHelpers.getMiniMessage().deserialize("<gray>Navigates you to the page <dark_aqua>" + nextPageIndex + "</dark_aqua> (<dark_aqua>").append(page.getTitle()).append(MessageHelpers.getMiniMessage().deserialize("</dark_aqua>)</gray>")));
                }

                page.setItem(page.getSize() - 4, nextPageButton, clickEvent -> {
                    String nbtTag = GuiHelpers.getNBTTag(Objects.requireNonNull(clickEvent.getCurrentItem()), NamespacedKeys.GUI_PAGINATION_NEXT_BUTTON);

                    System.out.println("Next button clicked, NBT: " + nbtTag);

                    if (clickEvent.getSlot() != page.getSize() - 4 || nbtTag == null || !nbtTag.equals("next_button_" + nextPageIndex)) {
                        System.out.println("Invalid click.");
                        return;
                    }

                    clickEvent.setCancelled(true);
                    BasePlayer basePlayer = plugin.getPlayerHandler().getPlayer(clickEvent.getWhoClicked().getUniqueId());

                    assert basePlayer != null;
                    System.out.println("Opening page " + nextPageIndex);
                    open(basePlayer, nextPageIndex);
                });
            }
        });
    }

    public int getCurrentPage(Player player) {
        Inventory inventory = player.getOpenInventory().getTopInventory();
        System.out.println("Pages: " + pages);
        return pages.stream().filter(page -> page.getInventory().equals(inventory)).findFirst().map(GuiPage::getPageIndex).orElse(-1);
    }

    /**
     * Utility method to calculate the amount of pages needed for a given amount of items and usable slots per inventory.
     *
     * @param totalItems  The total amount of items
     * @param usableSlots The amount of usable slots per inventory
     * @return The amount of pages needed
     */
    public int calculatePagesNeeded(int totalItems, int usableSlots) {
        return (int) Math.ceil((double) totalItems / usableSlots);
    }

    public void noPageOverview() {
        this.generatePageOverview = false;
    }

    @EventHandler
    public void handleClick(InventoryClickEvent event) {
        System.out.println("Hello!");
        if (event.getWhoClicked() instanceof Player player) {
            int currentPage = getCurrentPage(player) - 1;

            if (currentPage == -1) return;

            event.setCancelled(true);
            Consumer<InventoryClickEvent> action = pages.get(currentPage).getClickActions().get(event.getRawSlot());

            if (action != null) {
                System.out.println("Executing action...");
                action.accept(event);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;

        System.out.println("Player closed inventory");
        int currentPage = getCurrentPage(player) - 1;

        if (currentPage == -1) return;

        // TODO: Implement a way to stop listening for updates when the player closes the inventory
        //HandlerList.unregisterAll(this);
    }

    // public GuiBuilder addSubMenuButton(int slot, int page, ItemStack item, InventorySize size, Consumer<GuiBuilder> subMenuSetup) {
    //     GuiBuilder subMenu = new GuiBuilder(plugin, Component.text("Sub Menu"), size);
    //     subMenuSetup.accept(subMenu);
//
    //     clickActions.put(slot, event -> {
    //         Player player = (Player) event.getWhoClicked();
    //         navigationHistory.computeIfAbsent(player, k -> new ArrayDeque<>()).push(player.getOpenInventory().getTopInventory());
    //         subMenu.open(player, 0);
    //     });
//
    //     pages.get(page).setItem(slot, item);
    //     return this;
    // }
//
    // public void addBackButton(GuiBuilder subMenu) {
    //     subMenu.pages.forEach(page -> {
    //         ItemStack backButton = createItem(Material.BARRIER, Component.text("Back to Previous Menu"));
    //         page.setItem(size.getSize() - 1, backButton); // Setze den Button an die letzte Position
//
    //         subMenu.clickActions.put(size.getSize() - 1, event -> {
    //             Player player = (Player) event.getWhoClicked();
    //             Deque<Inventory> history = navigationHistory.get(player);
//
    //             if (history != null && !history.isEmpty()) {
    //                 Inventory previousMenu = history.pop();
    //                 player.openInventory(previousMenu); // Gehe zum vorherigen Menü zurück
    //             }
    //         });
    //     });
    // }
//
//
//
    // public void handleClick(InventoryClickEvent event) {
    //     if (event.getWhoClicked() instanceof Player player) {
    //         if (event.getClick() == ClickType.MIDDLE) {
    //             openPageNavigationMenu(player);
    //             event.setCancelled(true);
    //             return;
    //         }
//
    //         Consumer<InventoryClickEvent> action = clickActions.get(event.getRawSlot());
    //         if (action != null) {
    //             event.setCancelled(true);
    //             action.accept(event);
    //         }
    //     }
    // }
//
    // public void startUpdating(Player player, int page, int interval) {
    //     stopUpdating(player); // Stop any existing update task for the player
//
    //     BukkitRunnable updateTask = new BukkitRunnable() {
    //         @Override
    //         public void run() {
    //             Inventory currentInventory = player.getOpenInventory().getTopInventory();
    //             if (currentInventory != null && currentInventory.getViewers().contains(player)) {
    //                 updatePageContent(currentInventory);
    //             } else {
    //                 stopUpdating(player);
    //             }
    //         }
    //     };
//
    //     updateTask.runTaskTimer(plugin, 0L, interval);
    //     playerUpdateTasks.put(player, updateTask);
    // }
//
    // public void stopUpdating(Player player) {
    //     BukkitRunnable task = playerUpdateTasks.remove(player);
    //     if (task != null) {
    //         task.cancel();
    //     }
    // }
//
    // public void stopAllUpdating() {
    //     playerUpdateTasks.values().forEach(BukkitRunnable::cancel);
    //     playerUpdateTasks.clear();
    // }
//
    // private void updatePageContent(Inventory inventory) {
    //     // Example: Animate items by cycling through colors or types
    //     ItemStack animatedItem = new ItemStack(Material.values()[new Random().nextInt(Material.values().length)]);
    //     inventory.setItem(0, animatedItem);
    // }
//
    // public void open(Player player, int page) {
    //     if (page < pages.size()) {
    //         player.openInventory(pages.get(page));
    //         player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0f, 1.0f); // Add sound effect on opening
    //     }
    // }
//
    // public void openPageNavigationMenu(Player player) {
    //     Inventory navMenu = Bukkit.createInventory(null, 9, "Select Page");
    //     for (int i = 0; i < pages.size(); i++) {
    //         ItemStack pageButton = createItem(Material.PAPER, "Page " + (i + 1));
    //         navMenu.setItem(i, pageButton);
    //         final int pageIndex = i;
    //         clickActions.put(i, event -> open(player, pageIndex));
    //     }
    //     player.openInventory(navMenu);
    // }
//
    // public GuiBuilder addToggleButton(int slot, ItemStack onItem, ItemStack offItem, boolean initialState, Consumer<Boolean> toggleAction) {
    //     boolean[] state = {initialState};
    //     ItemStack initialItem = initialState ? onItem : offItem;
    //     pages.get(0).setItem(slot, initialItem);
    //     clickActions.put(slot, event -> {
    //         state[0] = !state[0];
    //         ItemStack newItem = state[0] ? onItem : offItem;
    //         pages.get(0).setItem(slot, newItem);
    //         toggleAction.accept(state[0]);
    //     });
    //     return this;
    // }
//
    // public GuiBuilder addRadioButtonGroup(int startSlot, List<ItemStack> options, int selectedIndex, Consumer<Integer> onSelect) {
    //     for (int i = 0; i < options.size(); i++) {
    //         ItemStack item = options.get(i);
    //         if (i == selectedIndex) {
    //             ItemMeta meta = item.getItemMeta();
    //             meta.setDisplayName("§a" + meta.getDisplayName());
    //             item.setItemMeta(meta);
    //         }
    //         int slot = startSlot + i;
    //         pages.get(0).setItem(slot, item);
    //         int finalI = i;
    //         clickActions.put(slot, event -> {
    //             addRadioButtonGroup(startSlot, options, finalI, onSelect);
    //             onSelect.accept(finalI);
    //         });
    //     }
    //     return this;
    // }
//
    // public void setProgressBar(Inventory inventory, int slot, double progress) {
    //     ItemStack progressBar = createItem(Material.GOLD_INGOT, "Progress: " + (int) (progress * 100) + "%");
    //     progressBar.setAmount((int) (progress * 64));
    //     inventory.setItem(slot, progressBar);
    // }
}