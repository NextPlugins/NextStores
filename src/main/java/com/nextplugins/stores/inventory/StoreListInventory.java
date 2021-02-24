package com.nextplugins.stores.inventory;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.henryfabio.minecraft.inventoryapi.editor.InventoryEditor;
import com.henryfabio.minecraft.inventoryapi.inventory.impl.paged.PagedInventory;
import com.henryfabio.minecraft.inventoryapi.item.InventoryItem;
import com.henryfabio.minecraft.inventoryapi.item.supplier.InventoryItemSupplier;
import com.henryfabio.minecraft.inventoryapi.viewer.Viewer;
import com.henryfabio.minecraft.inventoryapi.viewer.impl.paged.PagedViewer;
import com.nextplugins.stores.api.model.store.Store;
import com.nextplugins.stores.inventory.button.InventoryButton;
import com.nextplugins.stores.manager.StoreManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.LinkedList;
import java.util.List;

public final class StoreListInventory extends PagedInventory {

    @Inject private StoreManager storeManager;

    public StoreListInventory() {
        super(
                "stores.storeList",
                "Lojas disponíveis",
                6 * 9
        );
    }

    @Override
    protected void configureInventory(Viewer viewer, InventoryEditor editor) {
        super.configureInventory(viewer, editor);
    }

    @Override
    protected List<InventoryItemSupplier> createPageItems(PagedViewer viewer) {

        LinkedList<InventoryItemSupplier> items = Lists.newLinkedList();

        for (Store store : storeManager.getStores().values()) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(store.getOwner());

            items.add(() -> InventoryItem.of(InventoryButton.getSkullItemStackName(player.getName()))
                    .defaultCallback(callback -> {
                        callback.getPlayer().teleport(store.getLocation());
                        callback.getPlayer().closeInventory();
                        callback.getPlayer().sendMessage(
                                String.format("§aVocê foi teleportado à loja de %s", player.getName())
                        );
                    })
            );
        }

        return items;

    }
}
