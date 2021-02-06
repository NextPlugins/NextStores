package com.nextplugins.stores.inventory;

import com.henryfabio.minecraft.inventoryapi.editor.InventoryEditor;
import com.henryfabio.minecraft.inventoryapi.inventory.impl.global.GlobalInventory;
import com.henryfabio.minecraft.inventoryapi.item.InventoryItem;
import com.nextplugins.stores.utils.ColorUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */
public class StoreInventory extends GlobalInventory {

    public StoreInventory() {

        super(
                "stores.main",
                "Sistema de Loja",
                3 * 9
        );

    }

    @Override
    protected void configureInventory(InventoryEditor editor) {

        editor.setItem(12, InventoryItem.of(new ItemStack(Material.OBSIDIAN)));
        editor.setItem(14, InventoryItem.of(new ItemStack(Material.PRISMARINE_SHARD)));

    }

}
