package com.nextplugins.stores.inventory;

import com.henryfabio.minecraft.inventoryapi.editor.InventoryEditor;
import com.henryfabio.minecraft.inventoryapi.inventory.impl.paged.PagedInventory;
import com.henryfabio.minecraft.inventoryapi.item.InventoryItem;
import com.henryfabio.minecraft.inventoryapi.item.supplier.InventoryItemSupplier;
import com.henryfabio.minecraft.inventoryapi.viewer.impl.paged.PagedViewer;
import com.nextplugins.stores.NextStores;
import com.nextplugins.stores.api.model.store.Store;
import com.nextplugins.stores.configuration.values.MessageValue;
import com.nextplugins.stores.configuration.values.inventories.StoresInventoryValue;
import com.nextplugins.stores.inventory.button.InventoryButton;
import com.nextplugins.stores.util.item.ItemBuilder;
import com.nextplugins.stores.util.number.NumberFormat;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public final class StoreListInventory extends PagedInventory {

    private final NextStores plugin;

    public StoreListInventory(NextStores plugin) {
        super(
                "stores.storeList",
                "Lojas disponíveis",
                6 * 9
        );
        this.plugin = plugin;
    }

    @Override
    protected List<InventoryItemSupplier> createPageItems(PagedViewer viewer) {
        return getStores();
    }

    @Override
    protected void update(PagedViewer viewer, InventoryEditor editor) {
        getStores();
    }

    public List<InventoryItemSupplier> getStores() {

        List<InventoryItemSupplier> items = new LinkedList<>();

        for (Store store : plugin.getStoreManager().getStores().values()) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(store.getOwner());

            items.add(() ->
                    InventoryItem.of(
                            new ItemBuilder(InventoryButton.getSkullItemStackName(player.getName()).clone())
                                    .name(StoresInventoryValue.get(StoresInventoryValue::title).replace("$player", player.getName()))
                                    .lore(
                                            StoresInventoryValue.get(StoresInventoryValue::lore).stream()
                                                    .map(line -> line
                                                            .replace("$description", store.getDescription())
                                                            .replace("$likes", String.valueOf(store.getLikes()))
                                                            .replace("$dislikes", String.valueOf(store.getDislikes()))
                                                            .replace("$rating", NumberFormat.format(store.getRating()))
                                                            .replace("$open", store.isOpen() ? "Sim" : "Não")
                                                    )
                                                    .collect(Collectors.toList())
                                    )
                                    .result()
                    ).callback(
                            ClickType.LEFT,
                            callback -> {
                                final Player callbackPlayer = callback.getPlayer();

                                if (store.isOpen()) {
                                    callbackPlayer.teleport(store.getLocation());
                                    callbackPlayer.sendMessage(MessageValue.get(MessageValue::teleportedToTheStore)
                                            .replace("$player", player.getName()));
                                } else {
                                    callbackPlayer.sendMessage(MessageValue.get(MessageValue::storeClosed));
                                }

                                callbackPlayer.closeInventory();
                            }
                    ).callback(
                            ClickType.SHIFT_LEFT,
                            callback -> {
                                val callbackPlayer = callback.getPlayer();

                                store.like();
                                callbackPlayer.sendMessage(MessageValue.get(MessageValue::storeLike));

                                this.updateInventory(callbackPlayer);
                            }
                    ).callback(
                            ClickType.SHIFT_RIGHT,
                            callback -> {
                                val callbackPlayer = callback.getPlayer();

                                store.dislike();
                                callbackPlayer.sendMessage(MessageValue.get(MessageValue::storeDislike));

                                this.updateInventory(callbackPlayer);
                            }
                    )
            );
        }

        return items;
    }

}
