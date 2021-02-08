package com.nextplugins.stores.api.event;

import lombok.Builder;
import org.bukkit.event.Cancellable;

/**
 * Evento chamado quando uma loja é criada
 */
@Builder
public final class StoreCreatedEvent extends StoreEvent {
}
