package com.nextplugins.stores.conversation;

import com.nextplugins.stores.NextStores;
import com.nextplugins.stores.configuration.values.MessageValue;
import lombok.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ChatConversation {

    private static final Map<String, Request> requests = new LinkedHashMap<>();

    public static void registerListener() {
        Bukkit.getPluginManager().registerEvents(new ConversationListener(), NextStores.getInstance());
    }

    public static void scheduleTimeoutRunnable() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(NextStores.getInstance(), () -> {
            for (Map.Entry<String, Request> requestEntry : requests.entrySet()) {
                Player player = Bukkit.getPlayer(requestEntry.getKey());

                Request request = requestEntry.getValue();
                if (request.isTimeout()) {
                    request.sendTimeoutWarn(player);
                    requests.remove(player.getName());
                }
            }
        }, 0, 20);
    }

    public static void awaitResponse(Player player, Request request) {
        requests.put(player.getName(), request);
        request.sendRequestMessage(player);
    }

    public static void removeAwaitResponse(Player player) {
        requests.remove(player.getName());
    }

    public static Optional<Request> findPlayerRequest(String playerName) {
        return Optional.ofNullable(requests.get(playerName));
    }

    public static class ConversationListener implements Listener {

        @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
        public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
            Player player = event.getPlayer();

            Request request = findPlayerRequest(player.getName()).orElse(null);
            if (request == null || request.isTimeout()) return;

            event.setCancelled(true);

            String message = event.getMessage();
            if (message.equalsIgnoreCase("cancelar")) {
                player.sendMessage(MessageValue.get(MessageValue::cancelChatResponse));
                removeAwaitResponse(player);
                return;
            }

            if (request.acceptResponse(message)) {
                removeAwaitResponse(player);
            }
        }

        @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
        public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
            Player player = event.getPlayer();

            Request request = findPlayerRequest(player.getName()).orElse(null);
            if (request == null || request.isTimeout()) return;

            event.setCancelled(true);
            player.sendMessage(MessageValue.get(MessageValue::commandChatResponse));
        }

        @EventHandler
        public void onPlayerQuit(PlayerQuitEvent event) {
            Player player = event.getPlayer();
            removeAwaitResponse(player);
        }

    }

    @Builder
    @Data
    public static class Request {

        private final List<String> messages;

        private final Duration timeoutDuration;
        private final String timeoutWarn;

        @Builder.Default private final Predicate<String> responseFilter = response -> true;
        private final Consumer<String> responseConsumer;

        private final Instant requestInstant = Instant.now();

        public void sendRequestMessage(Player player) {
            messages.forEach(player::sendMessage);
        }

        public void sendTimeoutWarn(Player player) {
            player.sendMessage(timeoutWarn);
        }

        public boolean acceptResponse(String response) {
            if (responseFilter.test(response)) {
                responseConsumer.accept(response);
                return true;
            }

            return false;
        }

        public boolean isTimeout() {
            val timeoutInstant = requestInstant.plus(timeoutDuration);
            return timeoutInstant.isBefore(Instant.now());
        }

    }

}
