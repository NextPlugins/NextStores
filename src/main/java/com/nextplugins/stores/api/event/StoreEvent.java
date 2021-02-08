package com.nextplugins.stores.api.event;

import com.nextplugins.stores.api.model.store.Store;
import lombok.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@Builder
public abstract class StoreEvent extends Event implements Cancellable {

    @Getter private static final HandlerList handlerList = new HandlerList();

    private final Player player;
    private final Store store;
    @Setter private boolean cancelled;

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

}
