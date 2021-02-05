package com.nextplugins.stores.configuration;

import com.nextplugins.stores.NextStores;
import lombok.Data;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */

@Data(staticConstructor = "of")
public final class ConfigurationManager {

    private final String config;

    public ConfigurationManager saveDefault() {

        NextStores instance = NextStores.getInstance();
        instance.saveResource(this.config, false);

        return this;

    }

    public Configuration load() {

        NextStores instance = NextStores.getInstance();

        return YamlConfiguration.loadConfiguration(
                new File(instance.getDataFolder(), this.config)
        );

    }


}
