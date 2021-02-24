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
import org.bukkit.entity.Player;

import java.util.Optional;

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

        Optional<Store> store = NextStoresAPI.getInstance().findStoreByPlayer(viewer.getPlayer());
        if (!store.isPresent()) {

            editor.setItem(13, InventoryItem.of(InventoryButton.getSkullItemStackName(viewer.getName()))
                    .defaultCallback(callback -> {
                        Player player = callback.getPlayer();

                        storeManager.addStore(Store.builder()
                                .owner(player.getUniqueId())
                                .description("null")
                                .rating(0)
                                .likes(0)
                                .dislikes(0)
                                .location(player.getLocation())
                                .open(false)
                                .visits(0)
                                .build()
                        );

                        player.closeInventory();
                        player.sendMessage("§aA sua loja foi criada com sucesso!");
                    })
            );

        } else {

            Store playerStore = store.get();

            editor.setItem(14, InventoryItem.of(InventoryButton.getSkullItemStackName(viewer.getName()))
                    .defaultCallback(callback -> {
                        callback.getPlayer().sendMessage(playerStore.toString());
                        System.out.println(playerStore.toString());
                    })
            );

        }

    }

    @Override
    protected void configureViewer(SimpleViewer viewer) {

        ViewerConfiguration configuration = viewer.getConfiguration();
        configuration.backInventory("stores.main");

    }

}
