package com.nextplugins.stores.inventory;

import com.google.inject.Inject;
import com.henryfabio.minecraft.inventoryapi.editor.InventoryEditor;
import com.henryfabio.minecraft.inventoryapi.inventory.impl.global.GlobalInventory;
import com.henryfabio.minecraft.inventoryapi.item.InventoryItem;
import com.nextplugins.stores.NextStores;
import com.nextplugins.stores.configuration.values.inventories.MainInventoryValue;
import com.nextplugins.stores.inventory.button.InventoryButton;
import com.nextplugins.stores.registry.InventoryButtonRegistry;
import com.nextplugins.stores.registry.InventoryRegistry;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */
public class StoreInventory extends GlobalInventory {

    @Inject private InventoryButtonRegistry inventoryButtonRegistry;
    @Inject private InventoryRegistry inventoryRegistry;

    public StoreInventory() {

        super(
                "stores.main",
                MainInventoryValue.get(MainInventoryValue::title),
                MainInventoryValue.get(MainInventoryValue::lines) * 9
        );

        NextStores.getInstance().getInjector().injectMembers(this);
    }

    @Override
    protected void configureInventory(InventoryEditor editor) {

        InventoryButton yourStoreButton = inventoryButtonRegistry.get("main.yourStore");
        editor.setItem(
                yourStoreButton.getInventorySlot(),
                InventoryItem.of(yourStoreButton.getItemStack()).defaultCallback(
                        callback -> inventoryRegistry.getConfigureStoryInventory().openInventory(callback.getPlayer())
                )
        );

        InventoryButton allStoresButton = inventoryButtonRegistry.get("main.allStores");
        editor.setItem(
                allStoresButton.getInventorySlot(),
                InventoryItem.of(allStoresButton.getItemStack()).defaultCallback(
                        callback -> inventoryRegistry.getStoreListInventory().openInventory(callback.getPlayer())
                )
        );

    }

}
