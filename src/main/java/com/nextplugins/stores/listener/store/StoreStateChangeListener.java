package com.nextplugins.stores.listener.store;

import com.nextplugins.stores.api.event.StoreStateChangeEvent;
import com.nextplugins.stores.api.model.store.Store;
import com.nextplugins.stores.configuration.values.MessageValue;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class StoreStateChangeListener implements Listener {

    @EventHandler
    public void handleStoreStateChange(StoreStateChangeEvent event) {
        final Store store = event.getStore();
        final Player player = event.getPlayer();

        if (store.isOpen()) {
            store.setOpen(false);
            player.sendMessage(
                MessageValue.get(MessageValue::storeStateChange)
                    .replace("$state", MessageValue.get(MessageValue::closeState).toLowerCase())
            );
        } else {
            store.setOpen(true);
            player.sendMessage(
                MessageValue.get(MessageValue::storeStateChange)
                    .replace("$state", MessageValue.get(MessageValue::openState).toLowerCase())
            );
        }
    }

}
