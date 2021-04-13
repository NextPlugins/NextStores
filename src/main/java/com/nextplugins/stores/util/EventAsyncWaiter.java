package com.nextplugins.stores.util;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class EventAsyncWaiter<E extends Event> {

    private final Class<E> clazz;
    private final Plugin plugin;

    private Predicate<E> filter;
    private Consumer<E> action;

    private long expiringTime;
    private Runnable timeoutRunnable;

    private RegisteredListener listener;

    private final AtomicBoolean finished;

    private EventAsyncWaiter(Class<E> clazz, Plugin plugin) {
        this.clazz = clazz;
        this.plugin = plugin;
        this.filter = Objects::nonNull;
        this.action = Objects::nonNull;
        this.expiringTime = 1000;

        this.finished = new AtomicBoolean(false);
    }

    public static <E extends Event> EventAsyncWaiter<E> newAsyncWaiter(Class<E> clazz, Plugin plugin) {
        return new EventAsyncWaiter<>(clazz, plugin);
    }

    public EventAsyncWaiter<E> filter(Predicate<E> predicate) {
        filter = filter.and(predicate);
        return this;
    }

    public EventAsyncWaiter<E> expiringAfter(long time, TimeUnit unit) {
        this.expiringTime = unit.toSeconds(time);
        return this;
    }

    public EventAsyncWaiter<E> thenAccept(Consumer<E> consumer) {
        action = action.andThen(consumer);
        return this;
    }

    public EventAsyncWaiter<E> withTimeOutAction(Runnable action) {
        timeoutRunnable = action;
        return this;
    }


    public void await(boolean async) {
        this.register(event -> {

            if (!clazz.isInstance(event)) return;

            E castedEvent = clazz.cast(event);
            if (!filter.test(castedEvent)) return;

            if (async) Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> action.accept(castedEvent));
            else action.accept(castedEvent);

            this.timeoutRunnable = null;
            this.unregister();
            finished.set(true);

        });


        if (!async) Bukkit.getScheduler().runTaskLater(plugin, this::unregister, 20 * expiringTime);
        else Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, this::unregister, 20 * expiringTime);

    }

    private void register(Consumer<Event> consumer) {

        this.listener = new RegisteredListener(
                new EmptyListener(),
                (listener, event) -> consumer.accept(event),
                EventPriority.LOWEST,
                plugin,
                false
        );

        for (HandlerList handlerList : HandlerList.getHandlerLists()) {
            handlerList.register(listener);
        }

    }

    private void unregister() {

        if (finished.get()) return;
        if (timeoutRunnable != null) timeoutRunnable.run();
        if (listener == null) return;

        for (HandlerList handlerList : HandlerList.getHandlerLists()) {
            handlerList.unregister(listener);
        }

    }

    // Tag class
    class EmptyListener implements Listener {
    }

}
