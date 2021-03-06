package com.nextplugins.stores.configuration.values;

import com.nextplugins.stores.NextStores;
import com.nextplugins.stores.util.ColorUtils;
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

    // messages

    private final String incorrectUsage = message("messages.incorrect-usage");

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
