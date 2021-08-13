package com.nextplugins.stores.configuration.values.inventories;

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
public final class StoreInventoryValue {

    private static final StoreInventoryValue instance = new StoreInventoryValue();

    private final ConfigurationSection configuration = NextStores.getInstance().getStoreInventoryConfig().getConfigurationSection("inventory");

    private final String title = message("title");
    private final int lines = configuration.getInt("lines");

    public static <T> T get(Function<StoreInventoryValue, T> supplier) {
        return supplier.apply(StoreInventoryValue.instance);
    }

    private String colors(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    private String message(String key) {
        return colors(Objects.requireNonNull(configuration).getString(key));
    }

}
