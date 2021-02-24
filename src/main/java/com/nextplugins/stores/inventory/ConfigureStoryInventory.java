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
import com.nextplugins.stores.configuration.values.inventories.StoreInventoryValue;
import com.nextplugins.stores.inventory.button.InventoryButton;
import com.nextplugins.stores.manager.StoreManager;
import com.nextplugins.stores.registry.InventoryButtonRegistry;
import com.nextplugins.stores.util.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

import java.util.Optional;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */
public class ConfigureStoryInventory extends SimpleInventory {

    @Inject
    private StoreManager storeManager;
    @Inject
    private InventoryButtonRegistry inventoryButtonRegistry;

    public ConfigureStoryInventory() {

        super(
                "stores.configure",
                StoreInventoryValue.get(StoreInventoryValue::title),
                StoreInventoryValue.get(StoreInventoryValue::lines) * 9
        );

        NextStores.getInstance().getInjector().injectMembers(this);
    }

    @Override
    protected void configureInventory(Viewer viewer, InventoryEditor editor) {

        editor.setItem(0, DefaultItem.BACK.toInventoryItem(viewer));

        Optional<Store> store = NextStoresAPI.getInstance().findStoreByPlayer(viewer.getPlayer());
        if (!store.isPresent()) {

            editor.setItem(13, InventoryItem.of(
                    new ItemBuilder(InventoryButton.getSkullItemStackName(viewer.getName()))
                            .name(ChatColor.GREEN + "Criar uma loja")
                            .lore(
                                    "",
                                    ChatColor.GRAY + "Clique aqui para criar uma nova loja.",
                                    ""
                            )
                            .addItemFlags(ItemFlag.values())
                            .result()
                    ).defaultCallback(callback -> {
                        Player player = callback.getPlayer();

                        storeManager.addStore(Store.builder()
                                .owner(player.getUniqueId())
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

            storeItems(playerStore, editor);
        }

    }

    @Override
    protected void configureViewer(SimpleViewer viewer) {

        ViewerConfiguration configuration = viewer.getConfiguration();
        configuration.backInventory("stores.main");

    }

    private void storeItems(Store store, InventoryEditor editor) {

        InventoryButton infoButton = inventoryButtonRegistry.get("store.info");

        infoButton.setUsername(Bukkit.getOfflinePlayer(store.getOwner()).getName());

        editor.setItem(
                infoButton.getInventorySlot(),
                InventoryItem.of(infoButton.getItemStack())
        );

        InventoryButton locationButton = inventoryButtonRegistry.get("store.location");
        editor.setItem(
                locationButton.getInventorySlot(),
                InventoryItem.of(locationButton.getItemStack())
        );

        InventoryButton descriptionButton = inventoryButtonRegistry.get("store.description");
        editor.setItem(
                descriptionButton.getInventorySlot(),
                InventoryItem.of(descriptionButton.getItemStack())
        );

        InventoryButton stateButton = inventoryButtonRegistry.get("store.state");
        editor.setItem(
                stateButton.getInventorySlot(),
                InventoryItem.of(stateButton.getItemStack())
        );

    }

}
