package com.nextplugins.stores.api.event;

import com.nextplugins.stores.api.model.store.Store;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

/**
 * Evento chamado quando um jogador visita uma loja
 */
@Data
@EqualsAndHashCode(callSuper = true)
public final class PlayerVisitStoreEvent extends StoreEvent implements Cancellable {

    private final Player player;
    private final Store store;
    private boolean cancelled;
}
