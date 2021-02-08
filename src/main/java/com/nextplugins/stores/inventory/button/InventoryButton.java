package com.nextplugins.stores.inventory.button;

import lombok.Builder;
import lombok.Data;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;

import java.util.List;

@Builder
@Data
public final class InventoryButton {

    private final MaterialData materialData;

    private final String displayName;
    private final String username;

    private final List<String> lore;

    private final int inventorySlot;

    private ItemStack itemStack;

    public ItemStack getItemStack() {

        if (this.itemStack == null) {

            this.itemStack = materialData.toItemStack(1);

            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(this.displayName);
            itemMeta.setLore(this.lore);
            itemMeta.addItemFlags(ItemFlag.values());

            this.itemStack.setItemMeta(itemMeta);

        }

        return getSkullItemStack();

    }

    private ItemStack getSkullItemStack() {

        if (username == null) return itemStack;

        ItemStack itemStack = this.getItemStack().clone();

        if (itemStack.getType().name().contains("SKULL_ITEM")) {

            SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();

            skullMeta.setOwner(this.username);
            itemStack.setItemMeta(skullMeta);

        }

        return itemStack;

    }

}
