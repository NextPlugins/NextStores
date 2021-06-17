package com.nextplugins.stores.listener;

import com.nextplugins.stores.NextStores;
import com.nextplugins.stores.api.NextStoresAPI;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public final class UserDisconnectListener implements Listener {

    private final NextStores plugin;

    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) {

        val player = event.getPlayer();
        val nextStoresAPI = NextStoresAPI.getInstance();

        nextStoresAPI.findStoreByPlayer(player).ifPresent(store -> plugin.getStoreManager().getStoreDAO().insert(store));
    }

}
