package com.nextplugins.stores.listener.store;

import com.nextplugins.stores.api.event.PlayerLikeStoreEvent;
import com.nextplugins.stores.api.model.store.Store;
import com.nextplugins.stores.configuration.values.MessageValue;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class PlayerLikeStoreListener implements Listener {

    @EventHandler
    public void handlePlayerLikeStore(PlayerLikeStoreEvent event) {
        final Store store = event.getStore();
        final Player player = event.getPlayer();

        store.like();
        player.sendMessage(MessageValue.get(MessageValue::storeLike));
    }

}
