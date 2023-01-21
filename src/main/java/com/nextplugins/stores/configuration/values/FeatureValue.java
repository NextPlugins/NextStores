package com.nextplugins.stores.configuration.values;

import com.nextplugins.stores.NextStores;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.bukkit.configuration.Configuration;

import java.util.function.Function;

@Getter
@Accessors(fluent = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FeatureValue {

    private static final FeatureValue instance = new FeatureValue();

    private final Configuration configuration = NextStores.getInstance().getConfig();

    private final boolean useBStats = configuration.getBoolean("bStats.enabled");

    public static <T> T get(Function<FeatureValue, T> supplier) {
        return supplier.apply(FeatureValue.instance);
    }
}
