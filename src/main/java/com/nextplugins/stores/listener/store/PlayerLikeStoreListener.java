package com.nextplugins.stores.listener.store;

import com.google.inject.Inject;
import com.nextplugins.stores.api.event.PlayerLikeStoreEvent;
import com.nextplugins.stores.api.model.store.Store;
import com.nextplugins.stores.configuration.values.MessageValue;
import com.nextplugins.stores.manager.StoreManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class PlayerLikeStoreListener implements Listener {

    @Inject private StoreManager storeManager;

    @EventHandler
    public void handlePlayerLikeStore(PlayerLikeStoreEvent event) {
        final Store store = event.getStore();
        final Player player = event.getPlayer();

        storeManager.rateStore(store, player, "like");
        player.sendMessage(MessageValue.get(MessageValue::storeLike));
    }

}
