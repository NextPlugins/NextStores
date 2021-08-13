package com.nextplugins.stores.inventory;

import com.henryfabio.minecraft.inventoryapi.editor.InventoryEditor;
import com.henryfabio.minecraft.inventoryapi.inventory.impl.paged.PagedInventory;
import com.henryfabio.minecraft.inventoryapi.item.InventoryItem;
import com.henryfabio.minecraft.inventoryapi.item.enums.DefaultItem;
import com.henryfabio.minecraft.inventoryapi.item.supplier.InventoryItemSupplier;
import com.henryfabio.minecraft.inventoryapi.viewer.Viewer;
import com.henryfabio.minecraft.inventoryapi.viewer.configuration.impl.ViewerConfigurationImpl;
import com.henryfabio.minecraft.inventoryapi.viewer.impl.paged.PagedViewer;
import com.nextplugins.stores.NextStores;
import com.nextplugins.stores.api.event.PlayerDislikeStoreEvent;
import com.nextplugins.stores.api.event.PlayerLikeStoreEvent;
import com.nextplugins.stores.api.event.PlayerVisitStoreEvent;
import com.nextplugins.stores.api.model.store.Store;
import com.nextplugins.stores.configuration.values.inventories.StoresInventoryValue;
import com.nextplugins.stores.registry.InventoryButtonRegistry;
import com.nextplugins.stores.util.ItemBuilder;
import com.nextplugins.stores.util.NumberUtil;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.ClickType;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public final class StoreListInventory extends PagedInventory {

    private final NextStores plugin;
    private final InventoryButtonRegistry inventoryButtonRegistry;

    public StoreListInventory(NextStores plugin) {
        super(
            "stores.storeList",
            StoresInventoryValue.get(StoresInventoryValue::inventoryTitle),
            6 * 9
        );

        this.plugin = plugin;
        this.inventoryButtonRegistry = plugin.getInventoryButtonRegistry();
    }

    @Override
    protected void configureInventory(final Viewer viewer, final InventoryEditor editor) {
        editor.setItem(
            45,
            DefaultItem.BACK.toInventoryItem(viewer)
        );

        val visit = inventoryButtonRegistry.get("visit");
        editor.setItem(
                visit.getInventorySlot(),
                InventoryItem.of(visit.getItemStack()).defaultCallback(click -> click.getPlayer().performCommand("lojas ****"))
        );
    }

    @Override
    protected void configureViewer(PagedViewer viewer) {
        final ViewerConfigurationImpl.Paged configuration = viewer.getConfiguration();
        configuration.backInventory("stores.main" );
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
            val owner = store.getOwner();

            items.add(() ->
                InventoryItem.of(
                    new ItemBuilder(owner)
                        .name(StoresInventoryValue.get(StoresInventoryValue::title).replace("$player", owner))
                        .lore(
                            StoresInventoryValue.get(StoresInventoryValue::lore).stream()
                                .map(line -> line
                                    .replace("$description", store.getDescription())
                                    .replace("$likes", String.valueOf(store.getLikes()))
                                    .replace("$dislikes", String.valueOf(store.getDislikes()))
                                    .replace("$rating", NumberUtil.format(store.getRating()))
                                    .replace("$visits", NumberUtil.format(store.getVisits()))
                                    .replace("$open", store.isOpen() ? "Sim" : "Não" )
                                )
                                .collect(Collectors.toList())
                        )
                        .result()
                ).callback(
                    ClickType.LEFT,
                    callback -> {
                        final PlayerVisitStoreEvent playerVisitStoreEvent = new PlayerVisitStoreEvent(callback.getPlayer(), store);
                        Bukkit.getPluginManager().callEvent(playerVisitStoreEvent);

                        callback.getPlayer().closeInventory();
                    }
                ).callback(
                    ClickType.SHIFT_LEFT,
                    callback -> {
                        final PlayerLikeStoreEvent playerLikeStoreEvent = new PlayerLikeStoreEvent(callback.getPlayer(), store);
                        Bukkit.getPluginManager().callEvent(playerLikeStoreEvent);

                        callback.getPlayer().closeInventory();
                    }
                ).callback(
                    ClickType.SHIFT_RIGHT,
                    callback -> {
                        final PlayerDislikeStoreEvent playerDislikeStoreEvent = new PlayerDislikeStoreEvent(callback.getPlayer(), store);
                        Bukkit.getPluginManager().callEvent(playerDislikeStoreEvent);

                        callback.getPlayer().closeInventory();
                    }
                )
            );
        }

        return items;
    }

}
