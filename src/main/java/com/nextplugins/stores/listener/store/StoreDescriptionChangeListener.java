package com.nextplugins.stores.listener.store;

import com.nextplugins.stores.NextStores;
import com.nextplugins.stores.api.model.store.Store;
import com.nextplugins.stores.inventory.ConfigureStoreInventory;
import com.nextplugins.stores.manager.StoreManager;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@RequiredArgsConstructor
public final class StoreDescriptionChangeListener implements Listener {

    private final StoreManager storeManager;

    @EventHandler
    @SneakyThrows
    public void handleStoreDescriptionChange(AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();

        final Store store = storeManager.getPlayersChangingStoreDescription().getIfPresent(player.getUniqueId());

        if (store == null) return;

        event.setCancelled(true);
        val responseDescription = ChatColor.translateAlternateColorCodes('&', event.getMessage());
        store.setDescription(responseDescription);

        final ConfigureStoreInventory configureStoreInventory = new ConfigureStoreInventory(NextStores.getInstance());

        Bukkit.getScheduler().runTask(NextStores.getInstance(), () -> configureStoreInventory.openInventory(player));
        storeManager.getPlayersChangingStoreDescription().invalidate(player.getUniqueId());
    }
}
