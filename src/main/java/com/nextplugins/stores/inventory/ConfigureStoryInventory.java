package com.nextplugins.stores.inventory;

import com.henryfabio.minecraft.inventoryapi.editor.InventoryEditor;
import com.henryfabio.minecraft.inventoryapi.inventory.impl.simple.SimpleInventory;
import com.henryfabio.minecraft.inventoryapi.item.InventoryItem;
import com.henryfabio.minecraft.inventoryapi.item.enums.DefaultItem;
import com.henryfabio.minecraft.inventoryapi.viewer.Viewer;
import com.henryfabio.minecraft.inventoryapi.viewer.impl.simple.SimpleViewer;
import com.intellectualcrafters.plot.object.Location;
import com.intellectualcrafters.plot.object.Plot;
import com.nextplugins.stores.NextStores;
import com.nextplugins.stores.api.NextStoresAPI;
import com.nextplugins.stores.api.event.StoreCreatedEvent;
import com.nextplugins.stores.api.event.StoreStateChangeEvent;
import com.nextplugins.stores.api.model.store.Store;
import com.nextplugins.stores.configuration.values.MessageValue;
import com.nextplugins.stores.configuration.values.inventories.StoreInventoryValue;
import com.nextplugins.stores.inventory.button.InventoryButton;
import com.nextplugins.stores.manager.StoreManager;
import com.nextplugins.stores.registry.InventoryButtonRegistry;
import com.nextplugins.stores.util.ChatConversationUtils;
import com.nextplugins.stores.util.FancyText;
import com.nextplugins.stores.util.ItemBuilder;
import com.nextplugins.stores.util.NumberUtil;
import lombok.val;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.SkullMeta;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */
public class ConfigureStoryInventory extends SimpleInventory {

    private final StoreManager storeManager = NextStores.getInstance().getStoreManager();
    private final InventoryButtonRegistry inventoryButtonRegistry = NextStores.getInstance().getInventoryButtonRegistry();
    private final boolean usePlots;

    public ConfigureStoryInventory() {
        super(
            "stores.configure",
            StoreInventoryValue.get(StoreInventoryValue::title),
            StoreInventoryValue.get(StoreInventoryValue::lines) * 9
        );

        usePlots = Bukkit.getPluginManager().isPluginEnabled("PlotSquared") && NextStores.getInstance().getConfig().getBoolean("usePlots");
    }

    @Override
    protected void configureInventory(Viewer viewer, InventoryEditor editor) {

        editor.setItem(0, DefaultItem.BACK.toInventoryItem(viewer));

        val propertyMap = viewer.getPropertyMap();
        val store = (Store) propertyMap.get("store");
        if (store == null) {

            editor.setItem(13, InventoryItem.of(
                    new ItemBuilder(viewer.getName())
                        .name(ChatColor.GREEN + "Criar uma loja")
                        .lore("",
                            ChatColor.GRAY + "Parece que você ainda não possui uma loja...", "",
                            ChatColor.GREEN + "Clique aqui para criar uma."
                        )
                        .addItemFlags(ItemFlag.values())
                        .result()
                ).defaultCallback(callback -> {
                    val player = callback.getPlayer();

                    if (!player.hasPermission("nextstores.stores.create")) {
                        player.sendMessage(MessageValue.get(MessageValue::noPermissionToCreateStore));
                        player.closeInventory();
                        return;
                    }

                    if (plotCheck(player)) return;

                    final Store createdStore = Store.builder()
                        .owner(player.getName())
                        .location(player.getLocation())
                        .description(
                            MessageValue.get(MessageValue::defaultStoreDescription)
                                .replace("$player", player.getName())
                        )
                        .playersWhoRated(new LinkedHashMap<>())
                        .build();

                    storeManager.addStore(createdStore);

                    player.closeInventory();

                    val storeCreatedMessage = new FancyText(ChatColor.GREEN + "A sua loja foi criada com sucesso!")
                        .click(
                            ClickEvent.Action.RUN_COMMAND,
                            "/store"
                        )
                        .hover(
                            HoverEvent.Action.SHOW_TEXT,
                            ChatColor.GRAY + "Clique aqui abrir a configuração da sua loja."
                        )
                        .build();

                    player.spigot().sendMessage(storeCreatedMessage);

                    final StoreCreatedEvent storeCreatedEvent = new StoreCreatedEvent(player, createdStore);
                    Bukkit.getPluginManager().callEvent(storeCreatedEvent);
                })
            );

        } else storeItems(viewer.getPlayer(), store, editor);

    }

    @Override
    protected void configureViewer(SimpleViewer viewer) {
        val configuration = viewer.getConfiguration();
        configuration.backInventory("stores.main");

        val propertyMap = viewer.getPropertyMap();
        propertyMap.set("store", NextStoresAPI.getInstance().findStoreByOwner(viewer.getName()).orElse(null));
    }

    private void storeItems(Player player, Store store, InventoryEditor editor) {
        val locationButton = inventoryButtonRegistry.get("store.location");
        editor.setItem(locationButton.getInventorySlot(),
            InventoryItem.of(locationButton.getItemStack()).defaultCallback(callback -> {
                if (plotCheck(player)) return;

                store.setLocation(player.getLocation());

                player.closeInventory();
                player.sendMessage(MessageValue.get(MessageValue::locationSet));
            })
        );

        val descriptionButton = inventoryButtonRegistry.get("store.description");
        editor.setItem(descriptionButton.getInventorySlot(),
            InventoryItem.of(descriptionButton.getItemStack()).defaultCallback(callback -> {

                player.closeInventory();

                ChatConversationUtils.awaitResponse(player, ChatConversationUtils.Request.builder()
                    .messages(MessageValue.get(MessageValue::changeStoreDescription))
                    .timeoutDuration(Duration.ofSeconds(30))
                    .timeoutWarn(MessageValue.get(MessageValue::descriptionChangeTimeOut))
                    .responseConsumer(response -> {
                        val responseDescription = ChatColor.translateAlternateColorCodes('&', response);
                        store.setDescription(responseDescription);

                        Bukkit.getScheduler().runTask(NextStores.getInstance(), () -> this.openInventory(player));
                    })
                    .build());

            })
        );

        val stateButton = inventoryButtonRegistry.get("store.state");
        editor.setItem(stateButton.getInventorySlot(),
            InventoryItem.of(stateButton.getItemStack()).defaultCallback(callback -> {
                final StoreStateChangeEvent storeStateChangeEvent = new StoreStateChangeEvent(player, store);
                Bukkit.getPluginManager().callEvent(storeStateChangeEvent);
                updateInventory(player);
            })
        );

        val deleteButton = inventoryButtonRegistry.get("store.delete");

        editor.setItem(deleteButton.getInventorySlot(),
            InventoryItem.of(deleteButton.getItemStack()).defaultCallback(callback -> {

                callback.getPlayer().closeInventory();

                ChatConversationUtils.awaitResponse(callback.getPlayer(), ChatConversationUtils.Request.builder()
                    .messages(MessageValue.get(MessageValue::deletingStore))
                    .timeoutDuration(Duration.ofSeconds(30))
                    .timeoutWarn(MessageValue.get(MessageValue::storeDeleteTimeOut))
                    .responseConsumer(response -> {
                        if (response.equalsIgnoreCase("confirmar")) {
                            storeManager.deleteStore(store);
                            callback.getPlayer().sendMessage(MessageValue.get(MessageValue::storeDeleted));
                        }
                    })
                    .build());
            })
        );
    }

    private boolean plotCheck(Player player) {
        if (usePlots) {

            val plotLocation = new Location(player.getLocation().getWorld().getName(),
                (int) player.getLocation().getX(),
                (int) player.getLocation().getY(),
                (int) player.getLocation().getZ()
            );

            val plot = Plot.getPlot(plotLocation);
            if (plot == null || !plot.getOwners().contains(player.getUniqueId())) {

                player.sendMessage(MessageValue.get(MessageValue::onlyPlotMessage));
                return true;

            }

        } else {

            if (!MessageValue.get(MessageValue::worlds).contains(player.getWorld().getName())) {

                player.sendMessage(MessageValue.get(MessageValue::wrongWorld));
                return true;

            }

        }
        return false;
    }

    @Override
    protected void update(Viewer viewer, InventoryEditor editor) {

        val propertyMap = viewer.getPropertyMap();
        val store = (Store) propertyMap.get("store");

        if (store != null) getInfoButton(viewer.getPlayer(), store, editor);

    }

    private void getInfoButton(Player player, Store store, InventoryEditor editor) {
        val infoButton = (InventoryButton) inventoryButtonRegistry.get("store.info");

        editor.setItem(
            infoButton.getInventorySlot(),
            InventoryItem.of(new ItemBuilder(infoButton.getItemStack().clone())
                .changeItemMeta(itemMeta -> {
                    val skullMeta = (SkullMeta) itemMeta;

                    if (infoButton.getUsername() == null || infoButton.getUsername().isEmpty()) {
                        skullMeta.setOwner(player.getName());
                    } else {
                        skullMeta.setOwner(infoButton.getUsername());
                    }

                    val replacedLore = itemMeta.getLore()
                        .stream()
                        .map(line -> line
                            .replace("$description", store.getDescription())
                            .replace("$likes", NumberUtil.format(store.getLikes()))
                            .replace("$dislikes", NumberUtil.format(store.getDislikes()))
                            .replace("$rating", store.getRating() == 0 ? "0" : NumberUtil.format(store.getRating()))
                            .replace("$visits", NumberUtil.format(store.getVisits()))
                            .replace("$open", store.isOpen() ? "Sim" : "Não")
                        )
                        .collect(Collectors.toList());

                    itemMeta.setLore(replacedLore);
                })
                .result()
            )
        );
    }

}
