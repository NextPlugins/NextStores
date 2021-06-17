package com.nextplugins.stores.configuration.values;

import com.nextplugins.stores.NextStores;
import com.nextplugins.stores.util.text.ColorUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.bukkit.configuration.Configuration;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */

@Getter
@Accessors(fluent = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MessageValue {

    private static final MessageValue instance = new MessageValue();

    private final Configuration configuration = NextStores.getInstance().getMessagesConfig();

    // worlds

    private final List<String> worlds = configuration.getStringList("worlds");

    // messages

    private final String incorrectUsage = message("messages.incorrect-usage");
    private final String locationSet = message("messages.location-set");
    private final String descriptionChangeTimeOut = message("messages.description-change-time-out");
    private final String alreadyEditing = message("messages.already-editing");
    private final String storeStateChange = message("messages.store-state-change");
    private final String openState = message("messages.open-state");
    private final String closeState = message("messages.close-state");
    private final String descriptionChangeCancelled = message("messages.description-change-cancelled");
    private final String commandPlayerOnly = message("messages.command-player-only");
    private final String defaultStoreDescription = message("messages.default-store-description");
    private final String teleportedToTheStore = message("messages.teleported-to-the-store");
    private final String storeClosed = message("messages.store-closed");
    private final String commandChatResponse = message("messages.command-chat-response");
    private final String cancelChatResponse = message("messages.cancel-chat-response");
    private final String storeLike = message("messages.store-like");
    private final String storeDislike = message("messages.store-dislike");
    private final String storeDeleted = message("messages.store-deleted");
    private final String storeDeleteTimeOut = message("messages.store-delete-time-out");
    private final String storeDeletionCancelled = message("messages.store-deletion-cancelled");
    private final String onlyPlotMessage = message("messages.only-plot-message");
    private final String wrongWorld = message("messages.world-world");

    private final List<String> changeStoreDescription = messageList("messages.change-store-description");
    private final List<String> deletingStore = messageList("messages.deleting-store");

    public static <T> T get(Function<MessageValue, T> supplier) {
        return supplier.apply(MessageValue.instance);
    }

    private String colored(String message) {
        return ColorUtils.colored(message);
    }

    private String message(String key) {
        return colored(configuration.getString(key));
    }

    private List<String> messageList(String key) {
        return configuration.getStringList(key)
                .stream()
                .map(this::colored)
                .collect(Collectors.toList());
    }

}
