package com.nextplugins.stores.listener.store;

import com.nextplugins.stores.api.event.PlayerVisitStoreEvent;
import com.nextplugins.stores.api.model.store.Store;
import com.nextplugins.stores.configuration.values.MessageValue;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class PlayerVisitStoreListener implements Listener {

    @EventHandler
    public void handlePlayerVisitStore(PlayerVisitStoreEvent event) {
        final Store store = event.getStore();
        final Player player = event.getPlayer();

        if (store.isOpen()) {
            player.teleport(store.getLocation());
            player.sendMessage(MessageValue.get(MessageValue::teleportedToTheStore)
                .replace("$player", player.getName()));

            store.setVisits(store.getVisits() + 1);
        } else {
            player.sendMessage(MessageValue.get(MessageValue::storeClosed));
        }
    }

}
