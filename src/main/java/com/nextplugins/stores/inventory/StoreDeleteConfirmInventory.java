package com.nextplugins.stores.inventory;

import com.henryfabio.minecraft.inventoryapi.editor.InventoryEditor;
import com.henryfabio.minecraft.inventoryapi.inventory.impl.simple.SimpleInventory;
import com.henryfabio.minecraft.inventoryapi.item.InventoryItem;
import com.henryfabio.minecraft.inventoryapi.viewer.Viewer;
import com.nextplugins.stores.NextStores;
import com.nextplugins.stores.api.event.StoreDeletedEvent;
import com.nextplugins.stores.api.model.store.Store;
import com.nextplugins.stores.configuration.values.MessageValue;
import com.nextplugins.stores.manager.StoreManager;
import com.nextplugins.stores.registry.InventoryButtonRegistry;
import com.nextplugins.stores.registry.InventoryRegistry;
import com.nextplugins.stores.util.ColorUtil;
import com.nextplugins.stores.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class StoreDeleteConfirmInventory extends SimpleInventory {

    private final InventoryButtonRegistry inventoryButtonRegistry;
    private final InventoryRegistry inventoryRegistry;
    private final StoreManager storeManager;

    public StoreDeleteConfirmInventory(final NextStores instance) {
        super("stores.delete.confirm", "Confirmar", 27);

        this.inventoryButtonRegistry = instance.getInventoryButtonRegistry();
        this.inventoryRegistry = instance.getInventoryRegistry();
        this.storeManager = instance.getStoreManager();
    }

    @Override
    protected void configureInventory(@NotNull Viewer viewer, @NotNull InventoryEditor editor) {
        final Store store = viewer.getPropertyMap().get("meta.store");

        editor.setItem(
                11,
                InventoryItem.of(new ItemBuilder(getWoolItem(DyeColor.LIME))
                                .name(ColorUtil.colored("&aConfirmar"))
                                .lore("&7Clique aqui para confirmar", "&7sua ação e deletar", "&7sua loja.")
                                .result())
                        .callback(ClickType.LEFT, (event) -> {
                            final Player player = event.getPlayer();

                            Bukkit.getPluginManager().callEvent(new StoreDeletedEvent(player, store));
                            storeManager.deleteStore(store);

                            player.closeInventory();
                            player.sendMessage(MessageValue.get(MessageValue::storeDeleted));
                        }));

        editor.setItem(
                15,
                InventoryItem.of(new ItemBuilder(getWoolItem(DyeColor.RED))
                                .name(ColorUtil.colored("&cCancelar"))
                                .lore("&7Clique aqui para cancelar", "&7sua ação.")
                                .result())
                        .callback(ClickType.LEFT, event -> {
                            final Player player = event.getPlayer();

                            player.closeInventory();
                            player.sendMessage(MessageValue.get(MessageValue::storeDeletionCancelled));
                        }));
    }

    private ItemStack getWoolItem(DyeColor color) {
        Material material = Material.getMaterial(color.name() + "_WOOL");

        if (material != null) return new ItemStack(material);
        else return new ItemStack(Material.getMaterial("WOOL"), 1, color.getWoolData());
    }
}
