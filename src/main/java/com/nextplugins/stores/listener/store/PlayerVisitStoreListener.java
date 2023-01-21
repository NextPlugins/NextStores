package com.nextplugins.stores.listener.store;

import com.nextplugins.stores.api.event.PlayerVisitStoreEvent;
import com.nextplugins.stores.configuration.values.MessageValue;
import lombok.val;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class PlayerVisitStoreListener implements Listener {

    @EventHandler
    public void handlePlayerVisitStore(PlayerVisitStoreEvent event) {
        val store = event.getStore();
        val player = event.getPlayer();

        if (store.isOpen()) {
            player.teleport(store.getLocation());
            player.sendMessage(
                    MessageValue.get(MessageValue::teleportedToTheStore).replace("$player", player.getName()));

            store.setVisits(store.getVisits() + 1);
        } else {
            player.sendMessage(MessageValue.get(MessageValue::storeClosed));
        }
    }
}
