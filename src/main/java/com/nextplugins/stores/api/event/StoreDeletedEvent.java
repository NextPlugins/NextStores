package com.nextplugins.stores.api.event;

import com.nextplugins.stores.api.model.store.Store;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.entity.Player;

/**
 * Evento chamado ao deletar uma loja
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class StoreDeletedEvent extends StoreEvent {

    private final Player player;
    private final Store store;

}
