package com.nextplugins.stores.inventory;

import com.henryfabio.minecraft.inventoryapi.editor.InventoryEditor;
import com.henryfabio.minecraft.inventoryapi.inventory.impl.global.GlobalInventory;
import com.henryfabio.minecraft.inventoryapi.item.InventoryItem;
import com.nextplugins.stores.NextStores;
import com.nextplugins.stores.configuration.values.inventories.MainInventoryValue;
import com.nextplugins.stores.registry.InventoryButtonRegistry;
import com.nextplugins.stores.registry.InventoryRegistry;
import com.nextplugins.stores.util.ItemBuilder;
import lombok.val;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */

public class StoreInventory extends GlobalInventory {

    private final InventoryButtonRegistry inventoryButtonRegistry = NextStores.getInstance().getInventoryButtonRegistry();
    private final InventoryRegistry inventoryRegistry = NextStores.getInstance().getInventoryRegistry();

    public StoreInventory() {
        super(
                "stores.main",
                MainInventoryValue.get(MainInventoryValue::title),
                MainInventoryValue.get(MainInventoryValue::lines) * 9
        );
    }

    @Override
    protected void configureInventory(InventoryEditor editor) {

        val yourStoreButton = inventoryButtonRegistry.get("main.yourStore");
        editor.setItem(
                yourStoreButton.getInventorySlot(),
                InventoryItem.of(
                        new ItemBuilder(yourStoreButton.getItemStack())
                                .changeItemMeta(itemMeta -> {

                                    val skullMeta = (SkullMeta) itemMeta;
                                    skullMeta.setOwner(yourStoreButton.getUsername());

                                })
                                .result()
                ).defaultCallback(
                        callback -> inventoryRegistry.getConfigureStoryInventory().openInventory(callback.getPlayer())
                )
        );

        val allStoresButton = inventoryButtonRegistry.get("main.allStores");
        editor.setItem(
                allStoresButton.getInventorySlot(),
                InventoryItem.of(
                        new ItemBuilder(allStoresButton.getItemStack())
                                .changeItemMeta(itemMeta -> {
                                    SkullMeta skullMeta = (SkullMeta) itemMeta;

                                    skullMeta.setOwner(allStoresButton.getUsername());
                                })
                                .result()
                ).defaultCallback(
                        callback -> {
                            try {
                                inventoryRegistry.getStoreListInventory().openInventory(callback.getPlayer());
                            }catch (Throwable ignored) {
                                val player = callback.getPlayer();
                                player.sendMessage("§cNão existe nenhuma loja criada no servidor.");
                                player.closeInventory();
                            }
                        }

                )
        );

    }

}
