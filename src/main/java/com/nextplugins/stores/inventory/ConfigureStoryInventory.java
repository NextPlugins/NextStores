package com.nextplugins.stores.inventory;

import com.google.inject.Inject;
import com.henryfabio.minecraft.inventoryapi.editor.InventoryEditor;
import com.henryfabio.minecraft.inventoryapi.inventory.impl.simple.SimpleInventory;
import com.henryfabio.minecraft.inventoryapi.item.InventoryItem;
import com.henryfabio.minecraft.inventoryapi.item.enums.DefaultItem;
import com.henryfabio.minecraft.inventoryapi.viewer.Viewer;
import com.henryfabio.minecraft.inventoryapi.viewer.configuration.ViewerConfiguration;
import com.henryfabio.minecraft.inventoryapi.viewer.impl.simple.SimpleViewer;
import com.nextplugins.stores.NextStores;
import com.nextplugins.stores.api.NextStoresAPI;
import com.nextplugins.stores.api.model.store.Store;
import com.nextplugins.stores.configuration.values.InventoryValue;
import com.nextplugins.stores.inventory.button.InventoryButton;
import com.nextplugins.stores.manager.StoreManager;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */
public class ConfigureStoryInventory extends SimpleInventory {

    @Inject private StoreManager storeManager;

    public ConfigureStoryInventory() {

        super(
                "stores.configure",
                InventoryValue.get(InventoryValue::configureInventoryTitle),
                InventoryValue.get(InventoryValue::configureInventoryLines)
        );

        NextStores.getInstance().getInjector().injectMembers(this);
    }

    @Override
    protected void configureInventory(Viewer viewer, InventoryEditor editor) {

        editor.setItem(0, DefaultItem.BACK.toInventoryItem(viewer));

        Store store = NextStoresAPI.getInstance().findStoreByOwner(viewer.getPlayer().getName());
        if (store == null) {

            editor.setItem(14, InventoryItem.of(InventoryButton.getSkullItemStackName(viewer.getName()))
                    .defaultCallback(callback -> callback.getPlayer().sendMessage("TODO"))
            );

        }

    }

    @Override
    protected void configureViewer(SimpleViewer viewer) {

        ViewerConfiguration configuration = viewer.getConfiguration();
        configuration.backInventory("stores.main");

    }


}
