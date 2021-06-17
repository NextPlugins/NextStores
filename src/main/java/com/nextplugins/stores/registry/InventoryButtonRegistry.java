package com.nextplugins.stores.registry;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.nextplugins.stores.NextStores;
import com.nextplugins.stores.inventory.button.InventoryButton;
import com.nextplugins.stores.parser.InventoryButtonParser;
import org.bukkit.configuration.ConfigurationSection;

import java.util.LinkedHashMap;
import java.util.Map;

@Singleton
public final class InventoryButtonRegistry {

    private final Map<String, InventoryButton> inventoryButtonMap = new LinkedHashMap<>();

    @Inject @Named("buttons" ) private ConfigurationSection buttonsConfig;
    @Inject private InventoryButtonParser inventoryButtonParser;

    public void init() {

        register("main.yourStore", inventoryButtonParser.parse(
            buttonsConfig.getConfigurationSection("buttons.yourStore" )
        ));

        register("main.allStores", inventoryButtonParser.parse(
            buttonsConfig.getConfigurationSection("buttons.allStores" )
        ));

        // store inventory

        register("store.info", inventoryButtonParser.parse(
            NextStores.getInstance().getStoreInventoryConfig().getConfigurationSection("inventory.buttons.yourStore" )
        ));

        register("store.location", inventoryButtonParser.parse(
            NextStores.getInstance().getStoreInventoryConfig().getConfigurationSection("inventory.buttons.location" )
        ));

        register("store.description", inventoryButtonParser.parse(
            NextStores.getInstance().getStoreInventoryConfig().getConfigurationSection("inventory.buttons.description" )
        ));

        register("store.state", inventoryButtonParser.parse(
            NextStores.getInstance().getStoreInventoryConfig().getConfigurationSection("inventory.buttons.state" )
        ));

        register("store.delete", inventoryButtonParser.parse(
            NextStores.getInstance().getStoreInventoryConfig().getConfigurationSection("inventory.buttons.delete" )
        ));

    }

    public void register(String id, InventoryButton inventoryButton) {
        inventoryButtonMap.put(id, inventoryButton);
    }

    public InventoryButton get(String id) {
        return inventoryButtonMap.get(id);
    }

}
