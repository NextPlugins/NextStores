package com.nextplugins.stores.listener.store;

import com.nextplugins.stores.api.event.PlayerDislikeStoreEvent;
import lombok.val;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class PlayerDislikeStoreListener implements Listener {

    @EventHandler
    public void handlePlayerDislike(PlayerDislikeStoreEvent event) {
        val store = event.getStore();
        val player = event.getPlayer();

        store.rate(player, "dislike");
    }
}
