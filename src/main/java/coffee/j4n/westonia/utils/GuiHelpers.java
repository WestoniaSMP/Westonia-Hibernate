package coffee.j4n.westonia.utils;

import coffee.j4n.westonia.utils.guis.management.GuiPage;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

public class GuiHelpers {
    /**
     * Utility method to create an ItemStack with a display name
     *
     * @param material The material of the item
     * @param name     The display name of the item
     * @return A customized ItemStack
     */
    public static @NotNull ItemStack createItem(Material material, Component name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(name);
            item.setItemMeta(meta);
        }
        return item;
    }

    /**
     * Utility method to add a (custom) NBTTag to an ItemStack
     *
     * @param item The ItemStack to add the NBTTag to
     * @param key The key of the NBTTag
     * @param value The value of the NBTTag
     */
    public static void addNBTTag(@NotNull ItemStack item, NamespacedKey key, String value) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, value);
            item.setItemMeta(meta);
        }
    }

    /**
     * Utility method to get a (custom) NBTTag from an ItemStack
     *
     * @param item The ItemStack to get the NBTTag from
     * @param key  The key of the NBTTag
     * @return The value of the NBTTag or null if the tag does not exist
     */
    public static String getNBTTag(@NotNull ItemStack item, NamespacedKey key) {
        return item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING);
    }

    /**
     * Utility method to set the display name of an ItemStack
     *
     * @param item The ItemStack to set the display name of
     * @param name The display name to set
     */
    public static void setDisplayName(@NotNull ItemStack item, Component name) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(name);
            item.setItemMeta(meta);
        }
    }

    /**
     * Utility method to set the lore of an ItemStack
     *
     * @param item The ItemStack to set the lore of
     * @param lore The lore to set
     */
    public static void setLore(@NotNull ItemStack item, Component... lore) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.lore(Arrays.asList(lore));
            item.setItemMeta(meta);
        }
    }

    /**
     * Utility method to update a line to the lore of an ItemStack
     *
     * @param item The ItemStack to update the lore of
     * @param index The index of the lore line to update
     * @param lore The new lore to set
     */
    public static void updateLoreByIndex(@NotNull ItemStack item, int index, Component lore) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            Objects.requireNonNull(meta.lore()).set(index, lore);
            item.setItemMeta(meta);
        }
    }

    public static void addLineToLore(@NotNull ItemStack item, Component lore) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            Objects.requireNonNull(meta.lore()).add(lore);
            item.setItemMeta(meta);
        }
    }

    /**
     * Utility method to set the amount of an ItemStack
     *
     * @param item The ItemStack to set the amount of
     * @param amount The amount to set
     */
    public static void setAmount(@NotNull ItemStack item, int amount) {
        item.setAmount(amount);
    }
}
