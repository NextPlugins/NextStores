package com.nextplugins.stores.configuration;

import com.nextplugins.stores.NextStores;
import lombok.Data;
import lombok.val;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */
@Data(staticConstructor = "of")
public final class ConfigurationManager {

    private final String config;

    /**
     * Create config
     * @return instance of class
     */
    public ConfigurationManager saveDefault() {

        val instance = NextStores.getInstance();
        instance.saveResource(this.config, false);

        return this;
    }

    /**
     * Get {@link File} by config file name
     *
     * @return {@link File} finded in folder by name
     */
    public File getFile() {

        val instance = NextStores.getInstance();
        return new File(instance.getDataFolder(), this.config);
    }

    /**
     * Bukkit configuration of {@link File}
     *
     * @return {@link FileConfiguration} by {@link File}
     */
    public FileConfiguration load() {
        return YamlConfiguration.loadConfiguration(getFile());
    }
}
