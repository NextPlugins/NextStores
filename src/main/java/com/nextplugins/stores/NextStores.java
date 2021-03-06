package com.nextplugins.stores;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.henryfabio.minecraft.inventoryapi.manager.InventoryManager;
import com.henryfabio.sqlprovider.connector.SQLConnector;
import com.henryfabio.sqlprovider.connector.type.impl.MySQLDatabaseType;
import com.henryfabio.sqlprovider.connector.type.impl.SQLiteDatabaseType;
import com.nextplugins.stores.command.StoreCommand;
import com.nextplugins.stores.configuration.ConfigurationManager;
import com.nextplugins.stores.configuration.values.FeatureValue;
import com.nextplugins.stores.configuration.values.MessageValue;
import com.nextplugins.stores.guice.PluginModule;
import com.nextplugins.stores.manager.StoreManager;
import com.nextplugins.stores.registry.InventoryButtonRegistry;
import com.nextplugins.stores.registry.InventoryRegistry;
import lombok.Getter;
import me.saiintbrisson.bukkit.command.BukkitFrame;
import me.saiintbrisson.minecraft.command.message.MessageType;
import org.bstats.bukkit.Metrics;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

@Getter
public final class NextStores extends JavaPlugin {

    /**
     * Metrics plugin id (used for statistics)
     */
    private static final int PLUGIN_ID = 10227;

    private Injector injector;
    private SQLConnector sqlConnector;

    private Configuration messagesConfig;
    private Configuration mainInventoryConfig;
    private Configuration storeInventoryConfig;
    private Configuration storesInventoryConfig;

    @Inject private InventoryRegistry inventoryRegistry;
    @Inject private InventoryButtonRegistry inventoryButtonRegistry;

    @Inject private StoreManager storeManager;

    public static NextStores getInstance() {
        return getPlugin(NextStores.class);
    }

    @Override
    public void onLoad() {

        this.saveDefaultConfig();
        this.messagesConfig = ConfigurationManager.of("messages.yml").saveDefault().load();
        this.mainInventoryConfig = ConfigurationManager.of("inventories/main.yml").saveDefault().load();
        this.storeInventoryConfig = ConfigurationManager.of("inventories/store.yml").saveDefault().load();
        this.storesInventoryConfig = ConfigurationManager.of("inventories/stores.yml").saveDefault().load();

    }

    @Override
    public void onEnable() {

        //PluginDependencyManager.of(this).loadAllDependencies().thenRun(() -> {

            InventoryManager.enable(this);
            configureSqlProvider(this.getConfig());

            this.injector = PluginModule.from(this).createInjector();
            this.injector.injectMembers(this);

            inventoryRegistry.init();
            inventoryButtonRegistry.init();
            storeManager.init();

            enableCommandFrame();

            configureBStats();

        //});

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void configureSqlProvider(ConfigurationSection section) {

        if (section.getBoolean("connection.mysql.enable")) {

            ConfigurationSection mysqlSection = section.getConfigurationSection("connection.mysql");

            sqlConnector = MySQLDatabaseType.builder()
                    .address(mysqlSection.getString("address"))
                    .username(mysqlSection.getString("username"))
                    .password(mysqlSection.getString("password"))
                    .database(mysqlSection.getString("database"))
                    .build()
                    .connect();

        } else {

            ConfigurationSection sqliteSection = section.getConfigurationSection("connection.sqlite");

            sqlConnector = SQLiteDatabaseType.builder()
                    .file(new File(this.getDataFolder(), sqliteSection.getString("file")))
                    .build()
                    .connect();

        }

    }

    private void enableCommandFrame() {

        BukkitFrame bukkitFrame = new BukkitFrame(this);
        bukkitFrame.registerCommands(
                this.injector.getInstance(StoreCommand.class)
        );

        bukkitFrame.getMessageHolder().setMessage(
                MessageType.INCORRECT_USAGE,
                MessageValue.get(MessageValue::incorrectUsage)
        );

    }

    private void configureBStats() {
        if (!FeatureValue.get(FeatureValue::useBStats)) return;

        new Metrics(this, PLUGIN_ID);
        this.getLogger().info("Enabled bStats successfully, statistics enabled");

    }

}
