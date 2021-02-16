package com.nextplugins.stores.configuration.values;

import com.nextplugins.stores.NextStores;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Accessors(fluent = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class InventoryValue {

    private static final InventoryValue instance = new InventoryValue();

    private final ConfigurationSection configuration = NextStores.getInstance().getMessagesConfig().getConfigurationSection("inventory");

    private final String mainInventoryTitle = message("main.title");
    private final int mainInventoryLines = configuration.getInt("main.lines");

    private final String configureInventoryTitle = message("configure.title");
    private final int configureInventoryLines = configuration.getInt("configure.lines");

    public static <T> T get(Function<InventoryValue, T> supplier) {
        return supplier.apply(InventoryValue.instance);
    }

    private String colors(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    private String message(String key) {
        return colors(Objects.requireNonNull(configuration).getString(key));
    }

    private List<String> messageList(String key) {
        return Objects.requireNonNull(configuration).getStringList(key)
                .stream()
                .map(this::colors)
                .collect(Collectors.toList());
    }

}
