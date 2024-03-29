package com.nextplugins.stores.api.metric;

import lombok.Data;
import org.bukkit.plugin.java.JavaPlugin;

@Data(staticConstructor = "of")
public final class MetricProvider {

    private final JavaPlugin plugin;

    public void register() {

        System.setProperty("bstats.relocatecheck", "false");

        new MetricsConnector(plugin, 10227);
        plugin.getLogger().info("Métrica de uso habilitada com sucesso.");
    }
}
