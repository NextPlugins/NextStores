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
import com.nextplugins.stores.configuration.values.MessageValue;
import com.nextplugins.stores.configuration.values.inventories.StoreInventoryValue;
import com.nextplugins.stores.inventory.button.InventoryButton;
import com.nextplugins.stores.manager.StoreManager;
import com.nextplugins.stores.registry.InventoryButtonRegistry;
import com.nextplugins.stores.util.EventAsyncWaiter;
import com.nextplugins.stores.util.item.ItemBuilder;
import com.nextplugins.stores.util.text.FancyText;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */
public class ConfigureStoryInventory extends SimpleInventory {

    @Inject private StoreManager storeManager;
    @Inject private InventoryButtonRegistry inventoryButtonRegistry;

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
                                    ChatColor.GRAY + "Parece que você ainda não possui uma loja...",
                                    "",
                                    ChatColor.GREEN + "Clique aqui para criar uma."
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

                        final TextComponent storeCreatedMessage = new FancyText("§aA sua loja foi criada com sucesso!")
                                .click(
                                        ClickEvent.Action.RUN_COMMAND,
                                        "/store"
                                )
                                .hover(
                                        HoverEvent.Action.SHOW_TEXT,
                                        "§7Clique aqui abrir a configuração da sua loja."
                                )
                                .build();

                        player.spigot().sendMessage(storeCreatedMessage);

                    })
            );

        } else {
            Store playerStore = store.get();

            storeItems(viewer.getPlayer(), playerStore, editor);
        }

    }

    @Override
    protected void configureViewer(SimpleViewer viewer) {

        ViewerConfiguration configuration = viewer.getConfiguration();
        configuration.backInventory("stores.main");

    }

    private void storeItems(Player player, Store store, InventoryEditor editor) {

        InventoryButton infoButton = inventoryButtonRegistry.get("store.info");

        infoButton.setUsername(Bukkit.getOfflinePlayer(store.getOwner()).getName());

        editor.setItem(
                infoButton.getInventorySlot(),
                InventoryItem.of(
                        new ItemBuilder(infoButton.getItemStack())
                                .acceptItemMeta(itemMeta -> {
                                    SkullMeta skullMeta = (SkullMeta) itemMeta;

                                    if (infoButton.getUsername() == null || infoButton.getUsername().isEmpty()) {
                                        skullMeta.setOwner(player.getName());
                                    } else {
                                        skullMeta.setOwner(infoButton.getUsername());
                                    }
                                })
                                .result()
                ).defaultCallback(handler -> System.out.println(store.toString()))
        );

        InventoryButton locationButton = inventoryButtonRegistry.get("store.location");
        editor.setItem(
                locationButton.getInventorySlot(),
                InventoryItem.of(locationButton.getItemStack()).defaultCallback(callback -> {
                    store.setLocation(player.getLocation());

                    player.closeInventory();
                    player.sendMessage(MessageValue.get(MessageValue::locationSet));
                })
        );

        InventoryButton descriptionButton = inventoryButtonRegistry.get("store.description");
        editor.setItem(
                descriptionButton.getInventorySlot(),
                InventoryItem.of(descriptionButton.getItemStack()).defaultCallback(callback -> {
                    player.closeInventory();

                    final List<String> changeStoreDescriptionMessage = MessageValue.get(MessageValue::changeStoreDescription);

                    changeStoreDescriptionMessage.forEach(player::sendMessage);

                    EventAsyncWaiter.newAsyncWaiter(AsyncPlayerChatEvent.class, NextStores.getInstance())
                            .expiringAfter(30, TimeUnit.SECONDS)
                            .withTimeOutAction(() -> player.sendMessage(MessageValue.get(MessageValue::descriptionChangeTimeOut)))
                            .filter(event -> event.getPlayer().getUniqueId().equals(player.getUniqueId()))
                            .thenAccept(event -> {
                                event.setCancelled(true);

                                if (event.getMessage().equalsIgnoreCase("cancelar")) return;

                                final String newDescription = ChatColor.translateAlternateColorCodes('&', event.getMessage());

                                store.setDescription(newDescription);

                                this.openInventory(player);
                            })
                            .await(true);
                })
        );

        InventoryButton stateButton = inventoryButtonRegistry.get("store.state");
        editor.setItem(
                stateButton.getInventorySlot(),
                InventoryItem.of(stateButton.getItemStack()).defaultCallback(callback -> {
                    if (store.isOpen()) {
                        store.setOpen(false);
                        player.sendMessage(
                                MessageValue.get(MessageValue::storeStateChange)
                                        .replace("$state", MessageValue.get(MessageValue::closeState).toLowerCase())
                        );
                    } else {
                        store.setOpen(true);
                        player.sendMessage(
                                MessageValue.get(MessageValue::storeStateChange)
                                        .replace("$state", MessageValue.get(MessageValue::openState).toLowerCase())
                        );
                    }

                    updateInventory(player);
                })
        );

    }

}
