package com.nextplugins.stores.listener.store;

import com.nextplugins.stores.api.event.PlayerLikeStoreEvent;
import lombok.val;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class PlayerLikeStoreListener implements Listener {

    @EventHandler
    public void handlePlayerLikeStore(PlayerLikeStoreEvent event) {
        val store = event.getStore();
        val player = event.getPlayer();

        store.rate(player, "like");
    }
}
