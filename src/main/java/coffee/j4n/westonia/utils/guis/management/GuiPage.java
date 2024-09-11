package coffee.j4n.westonia.utils.guis.management;

import coffee.j4n.westonia.utils.GuiHelpers;
import coffee.j4n.westonia.utils.statics.constants.NamespacedKeys;
import coffee.j4n.westonia.utils.statics.enums.InventorySize;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

public class GuiPage {

    private final Inventory minecraftInventory;
    private final InventorySize inventorySize;
    private final Component title;
    private final int pageIndex;
    private final ItemStack icon;

    private final Map<Integer, Consumer<InventoryClickEvent>> clickActions;

    private final List<Integer> usableSlots = new ArrayList<>();

    public GuiPage(@Nullable Component title, int pageIndex, InventorySize inventorySize) {
        this.title = title;
        this.pageIndex = pageIndex;

        if (title == null) {
            title = Component.text("");
        }

        this.icon = GuiHelpers.createItem(Material.PAPER, Component.text("<gray>Page: <dark_aqua>" + pageIndex + "</dark_aqua></gray>"));

        switch(inventorySize) {
            case HOPPER -> this.minecraftInventory = Bukkit.createInventory(null, InventoryType.HOPPER, title);
            case DROPPER -> this.minecraftInventory = Bukkit.createInventory(null, InventoryType.DROPPER, title);
            default -> this.minecraftInventory = Bukkit.createInventory(null, inventorySize.getSize(), title);
        }

        applyOutlines();
        this.usableSlots.addAll(determineUsableSlots(inventorySize));

        this.inventorySize = inventorySize;
        this.clickActions = new HashMap<>();
    }

    public GuiPage(@Nullable Component title, int pageIndex, @NotNull ItemStack icon, InventorySize inventorySize) {
        this.title = title;
        this.pageIndex = pageIndex;

        if (title == null) {
            title = Component.text("");
        }

        this.icon = icon;

        switch(inventorySize) {
            case HOPPER -> this.minecraftInventory = Bukkit.createInventory(null, InventoryType.HOPPER, title);
            case DROPPER -> this.minecraftInventory = Bukkit.createInventory(null, InventoryType.DROPPER, title);
            default -> this.minecraftInventory = Bukkit.createInventory(null, inventorySize.getSize(), title);
        }

        applyOutlines();
        this.usableSlots.addAll(determineUsableSlots(inventorySize));

        this.inventorySize = inventorySize;
        this.clickActions = new HashMap<>();
    }

    public Inventory getInventory() {
        return minecraftInventory;
    }

    public int getSize() {
        return inventorySize.getSize();
    }

    public int getPageIndex() {
        return this.pageIndex;
    }

    public ItemStack getIcon() {
        return this.icon;
    }

    @Nullable
    public Component getTitle() {
        return title;
    }

    public List<Integer> getUsableSlots() {
        return this.usableSlots;
    }

    /**
     * Returns whether the inventory has a back button.
     *
     * @return True if the inventory has a back button, false otherwise
     */
    public boolean hasBackButton() {
        String result = GuiHelpers.getNBTTag(Objects.requireNonNull(minecraftInventory.getItem(minecraftInventory.getSize() - 8)), NamespacedKeys.GUI_BACK_BUTTON);

        Bukkit.getServer().getLogger().info("Result: " + result);

        return result != null && result.equals("true");
    }

    /**
     * Returns whether the inventory has pagination.
     *
     * @return True if the inventory has pagination, false otherwise
     */
    public boolean hasPagination() {
        String resultBackButton = GuiHelpers.getNBTTag(Objects.requireNonNull(minecraftInventory.getItem(minecraftInventory.getSize() - 8)), NamespacedKeys.GUI_PAGINATION_BACK_BUTTON);
        String resultPageInfoButton = GuiHelpers.getNBTTag(Objects.requireNonNull(minecraftInventory.getItem(minecraftInventory.getSize() - 5)), NamespacedKeys.GUI_PAGINATION_PAGE_INFO_BUTTON);
        String resultNextButton = GuiHelpers.getNBTTag(Objects.requireNonNull(minecraftInventory.getItem(minecraftInventory.getSize() - 2)), NamespacedKeys.GUI_PAGINATION_NEXT_BUTTON);

        return resultBackButton != null && resultBackButton.equals("true") || resultPageInfoButton != null && resultPageInfoButton.equals("true") || resultNextButton != null && resultNextButton.equals("true");
    }

    /**
     * Removes the click action from the inventory at the specified slot.
     *
     * @param slot The slot to remove the click action from
     */
    private void removeClickAction(int slot) {
        clickActions.remove(slot);
    }

    public Map<Integer, Consumer<InventoryClickEvent>> getClickActions() {
        return this.clickActions;
    }

    /**
     * Sets the item in the inventory at the specified slot.
     *
     * @param slot The slot to set the item in
     * @param item The item to set
     */
    public void setItem(int slot, ItemStack item) {
        minecraftInventory.setItem(slot, item);
    }

    /**
     * Sets the item in the inventory at the specified slot and adds a click action to it.
     *
     * @param slot The slot to set the item in
     * @param item The item to set
     * @param action The action to perform when the slot is clicked
     */
    public void setItem(int slot, ItemStack item, Consumer<InventoryClickEvent> action) {
        minecraftInventory.setItem(slot, item);
        addClickAction(slot, action);
    }

    /**
     * Fills the remaining slots in the inventory with gray stained glass panes.
     */
    public void fillRemainingSlots() {
        for (int i = 0; i < minecraftInventory.getSize(); i++) {
            if (minecraftInventory.getItem(i) == null) {
                minecraftInventory.setItem(i, GuiHelpers.createItem(Material.GRAY_STAINED_GLASS_PANE, Component.text("")));
            }
        }
    }

    /**
     * Applies the outlines (gray stained glass panes) to the inventory.
     */
    public void applyOutlines() {
        //Outline the inventory with GRAY_STAINED_GLASS_PANE, based on the inventory size
        for (int i = 0; i < minecraftInventory.getSize(); i++) {
            // Reads like this: if the slot is in the first row, last row, first column, or last column, set the item to a gray pane
            if (i < 9 || i >= minecraftInventory.getSize() - 9 || i % 9 == 0 || i % 9 == 8) {
                minecraftInventory.setItem(i, GuiHelpers.createItem(Material.GRAY_STAINED_GLASS_PANE, Component.text("")));
            }
        }
    }


    /**
     * Adds a click action to the inventory at the specified slot.
     * If a click action already exists for the slot, it will be replaced with the new action.
     *
     * @param slot The slot to add the click action to
     * @param action The action to perform when the slot is clicked
     */
    private void addClickAction(int slot, Consumer<InventoryClickEvent> action) {
        if (!clickActions.containsKey(slot)) {
            clickActions.put(slot, action);
        } else {
            clickActions.replace(slot, action);
        }
    }


    /**
     * Utility method to get the usable slots (those that are not occupied from the outline) of a page based on the inventory size.
     *
     * @return A list of usable slots
     */
    public static List<Integer> determineUsableSlots(InventorySize size) {
        List<Integer> usableSlots = new ArrayList<>();

        for (int i = 0; i < size.getSize(); i++) {
            // Checks if the slot is not in the first or last row or the first or last column
            // Reads like this: if the slot is in the first row, last row, first column, or last column, save the index of the usable slot
            if (!(i < 9 || i >= size.getSize() - 9 || i % 9 == 0 || i % 9 == 8)) {
                usableSlots.add(i);
            }
        }

        return usableSlots;
    }
}
