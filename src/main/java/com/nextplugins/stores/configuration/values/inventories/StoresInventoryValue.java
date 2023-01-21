package com.nextplugins.stores.configuration.values.inventories;

import com.nextplugins.stores.NextStores;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Accessors(fluent = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StoresInventoryValue {

    private static final StoresInventoryValue instance = new StoresInventoryValue();

    private final Configuration configuration = NextStores.getInstance().getStoresInventoryConfig();

    private final String inventoryTitle = message("inventory-title");

    private final String title = message("item.title");
    private final List<String> lore = messageList("item.lore");

    public static <T> T get(Function<StoresInventoryValue, T> supplier) {
        return supplier.apply(StoresInventoryValue.instance);
    }

    private String colors(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    private String message(String key) {
        return colors(Objects.requireNonNull(configuration).getString(key));
    }

    private List<String> messageList(String key) {
        return Objects.requireNonNull(configuration).getStringList(key).stream()
                .map(this::colors)
                .collect(Collectors.toList());
    }
}
