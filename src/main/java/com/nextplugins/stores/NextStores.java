package com.nextplugins.stores;

import com.henryfabio.minecraft.inventoryapi.manager.InventoryManager;
import com.henryfabio.sqlprovider.connector.SQLConnector;
import com.henryfabio.sqlprovider.connector.type.impl.MySQLDatabaseType;
import com.henryfabio.sqlprovider.connector.type.impl.SQLiteDatabaseType;
import com.henryfabio.sqlprovider.executor.SQLExecutor;
import com.nextplugins.stores.api.metric.MetricProvider;
import com.nextplugins.stores.command.SetStoreNPCCommand;
import com.nextplugins.stores.command.StoreCommand;
import com.nextplugins.stores.configuration.ConfigurationManager;
import com.nextplugins.stores.dao.StoreDAO;
import com.nextplugins.stores.listener.UserDisconnectListener;
import com.nextplugins.stores.listener.store.*;
import com.nextplugins.stores.manager.StoreManager;
import com.nextplugins.stores.npc.manager.NPCManager;
import com.nextplugins.stores.npc.runnable.NPCRunnable;
import com.nextplugins.stores.registry.InventoryButtonRegistry;
import com.nextplugins.stores.registry.InventoryRegistry;
import com.nextplugins.stores.util.ChatConversationUtils;
import lombok.Getter;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

@Getter
public final class NextStores extends JavaPlugin {

    private SQLConnector sqlConnector;

    private Configuration messagesConfig;
    private Configuration mainInventoryConfig;
    private Configuration storeInventoryConfig;
    private Configuration storesInventoryConfig;
    private Configuration npcConfig;

    private InventoryRegistry inventoryRegistry;
    private InventoryButtonRegistry inventoryButtonRegistry;

    private StoreDAO storeDAO;
    private StoreManager storeManager;
    private NPCManager npcManager;

    public static NextStores getInstance() {
        return getPlugin(NextStores.class);
    }

    @Override
    public void onLoad() {

        saveDefaultConfig();

        messagesConfig = ConfigurationManager.of("messages.yml").saveDefault().load();
        mainInventoryConfig = ConfigurationManager.of("inventories/main.yml").saveDefault().load();
        storeInventoryConfig = ConfigurationManager.of("inventories/store.yml").saveDefault().load();
        storesInventoryConfig = ConfigurationManager.of("inventories/stores.yml").saveDefault().load();
        npcConfig = ConfigurationManager.of("npc.yml").saveDefault().load();

    }

    @Override
    public void onEnable() {
        InventoryManager.enable(this);
        sqlProvider(getConfig());

        inventoryRegistry = new InventoryRegistry();
        inventoryButtonRegistry = new InventoryButtonRegistry();
        storeManager = new StoreManager(storeDAO);
        npcManager = new NPCManager();

        inventoryRegistry.init();
        inventoryButtonRegistry.init();
        storeManager.init();
        npcManager.init();

        getCommand("store").setExecutor(new StoreCommand(this));
        getCommand("storesetnpc").setExecutor(new SetStoreNPCCommand());

        listener();
        MetricProvider.of(this).register();

        ChatConversationUtils.registerListener();
        ChatConversationUtils.scheduleTimeoutRunnable();
    }

    @Override
    public void onDisable() {
        if (!npcManager.isEnabled()) return;

        val npcRunnable = (NPCRunnable) npcManager.getRunnable();
        npcRunnable.despawn();
    }

    private void sqlProvider(ConfigurationSection section) {
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

            val sqliteSection = section.getConfigurationSection("connection.sqlite");

            sqlConnector = SQLiteDatabaseType.builder()
                .file(new File(this.getDataFolder(), sqliteSection.getString("file")))
                .build()
                .connect();

        }

        storeDAO = new StoreDAO(new SQLExecutor(sqlConnector));
    }

    private void listener() {
        val pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new UserDisconnectListener(this), this);
        pluginManager.registerEvents(new StoreStateChangeListener(), this);
        pluginManager.registerEvents(new StoreDescriptionChangeListener(this.storeManager), this);

        val playerLikeStoreListener = new PlayerLikeStoreListener();
        val playerVisitStoreListener = new PlayerVisitStoreListener();
        val playerDislikeStoreListener = new PlayerDislikeStoreListener();

        pluginManager.registerEvents(playerLikeStoreListener, this);
        pluginManager.registerEvents(playerVisitStoreListener, this);
        pluginManager.registerEvents(playerDislikeStoreListener, this);

    }

}
