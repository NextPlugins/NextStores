package com.nextplugins.stores.inventory;

import com.google.inject.Inject;
import com.henryfabio.minecraft.inventoryapi.editor.InventoryEditor;
import com.henryfabio.minecraft.inventoryapi.inventory.impl.simple.SimpleInventory;
import com.henryfabio.minecraft.inventoryapi.item.InventoryItem;
import com.henryfabio.minecraft.inventoryapi.item.enums.DefaultItem;
import com.henryfabio.minecraft.inventoryapi.viewer.Viewer;
import com.henryfabio.minecraft.inventoryapi.viewer.configuration.ViewerConfiguration;
import com.henryfabio.minecraft.inventoryapi.viewer.impl.simple.SimpleViewer;
import com.henryfabio.minecraft.inventoryapi.viewer.property.ViewerPropertyMap;
import com.nextplugins.stores.NextStores;
import com.nextplugins.stores.api.NextStoresAPI;
import com.nextplugins.stores.api.model.store.Store;
import com.nextplugins.stores.configuration.values.MessageValue;
import com.nextplugins.stores.configuration.values.inventories.StoreInventoryValue;
import com.nextplugins.stores.conversation.ChatConversation;
import com.nextplugins.stores.inventory.button.InventoryButton;
import com.nextplugins.stores.manager.StoreManager;
import com.nextplugins.stores.registry.InventoryButtonRegistry;
import com.nextplugins.stores.util.item.ItemBuilder;
import com.nextplugins.stores.util.number.NumberFormat;
import com.nextplugins.stores.util.text.FancyText;
import lombok.val;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.SkullMeta;

import java.time.Duration;
import java.util.stream.Collectors;

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

        ViewerPropertyMap propertyMap = viewer.getPropertyMap();
        Store store = propertyMap.get("store" );
        if (store == null) {

            editor.setItem(13, InventoryItem.of(
                new ItemBuilder(InventoryButton.getSkullItemStackName(viewer.getName()))
                    .name(ChatColor.GREEN + "Criar uma loja" )
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
                        .owner(player.getName())
                        .likes(0)
                        .dislikes(0)
                        .location(player.getLocation())
                        .open(false)
                        .visits(0)
                        .description(
                            MessageValue.get(MessageValue::defaultStoreDescription)
                                .replace("$player", player.getName())
                        )
                        .build()
                    );

                    player.closeInventory();

                    final TextComponent storeCreatedMessage = new FancyText("§aA sua loja foi criada com sucesso!" )
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
            storeItems(viewer.getPlayer(), store, editor);
        }

    }

    @Override
    protected void configureViewer(SimpleViewer viewer) {
        ViewerConfiguration configuration = viewer.getConfiguration();
        configuration.backInventory("stores.main" );

        ViewerPropertyMap propertyMap = viewer.getPropertyMap();
        propertyMap.set("store", NextStoresAPI.getInstance().findStoreByOwner(viewer.getName()).orElse(null));
    }

    private void storeItems(Player player, Store store, InventoryEditor editor) {
        InventoryButton locationButton = inventoryButtonRegistry.get("store.location" );
        editor.setItem(
            locationButton.getInventorySlot(),
            InventoryItem.of(locationButton.getItemStack()).defaultCallback(callback -> {
                store.setLocation(player.getLocation());

                player.closeInventory();
                player.sendMessage(MessageValue.get(MessageValue::locationSet));
            })
        );

        InventoryButton descriptionButton = inventoryButtonRegistry.get("store.description" );
        editor.setItem(
            descriptionButton.getInventorySlot(),
            InventoryItem.of(descriptionButton.getItemStack()).defaultCallback(callback -> {
                player.closeInventory();

                ChatConversation.awaitResponse(player, ChatConversation.Request.builder()
                    .messages(MessageValue.get(MessageValue::changeStoreDescription))
                    .timeoutDuration(Duration.ofSeconds(30))
                    .timeoutWarn(MessageValue.get(MessageValue::descriptionChangeTimeOut))
                    .responseConsumer(response -> {
                        val responseDescription = ChatColor.translateAlternateColorCodes('&', response);
                        store.setDescription(responseDescription);

                        this.openInventory(player);
                    })
                    .build());
            })
        );

        InventoryButton stateButton = inventoryButtonRegistry.get("store.state" );
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

    @Override
    protected void update(Viewer viewer, InventoryEditor editor) {
        ViewerPropertyMap propertyMap = viewer.getPropertyMap();
        Store store = propertyMap.get("store" );

        if (store != null) {
            getInfoButton(viewer.getPlayer(), store, editor);
        }
    }

    private void getInfoButton(Player player, Store store, InventoryEditor editor) {
        InventoryButton infoButton = inventoryButtonRegistry.get("store.info" );

        editor.setItem(
            infoButton.getInventorySlot(),
            InventoryItem.of(
                new ItemBuilder(infoButton.getItemStack().clone())
                    .acceptItemMeta(itemMeta -> {
                        SkullMeta skullMeta = (SkullMeta) itemMeta;

                        if (infoButton.getUsername() == null || infoButton.getUsername().isEmpty()) {
                            skullMeta.setOwner(player.getName());
                        } else {
                            skullMeta.setOwner(infoButton.getUsername());
                        }

                        val lore = itemMeta.getLore();

                        val replacedLore = lore.stream()
                            .map(line -> line
                                .replace("$description", store.getDescription())
                                .replace("$likes", String.valueOf(store.getLikes()))
                                .replace("$dislikes", String.valueOf(store.getDislikes()))
                                .replace("$rating", NumberFormat.format(store.getRating()))
                                .replace("$open", store.isOpen() ? "Sim" : "Não" )
                                .replace("$visits", String.valueOf(store.getVisits()))
                            )
                            .collect(Collectors.toList());

                        itemMeta.setLore(replacedLore);
                    })
                    .result()
            )
        );
    }

}
