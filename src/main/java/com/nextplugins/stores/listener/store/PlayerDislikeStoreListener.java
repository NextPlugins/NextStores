package com.nextplugins.stores.listener.store;

import com.nextplugins.stores.api.event.PlayerDislikeStoreEvent;
import com.nextplugins.stores.api.model.store.Store;
import com.nextplugins.stores.configuration.values.MessageValue;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class PlayerDislikeStoreListener implements Listener {

    @EventHandler
    public void handlePlayerDislike(PlayerDislikeStoreEvent event) {
        final Store store = event.getStore();
        final Player player = event.getPlayer();

        store.dislike();
        player.sendMessage(MessageValue.get(MessageValue::storeDislike));
    }

}
