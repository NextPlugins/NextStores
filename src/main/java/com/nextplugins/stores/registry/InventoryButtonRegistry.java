package com.nextplugins.stores.registry;

import com.nextplugins.stores.NextStores;
import com.nextplugins.stores.inventory.button.InventoryButton;
import com.nextplugins.stores.parser.InventoryButtonParser;
import lombok.val;

import java.util.LinkedHashMap;
import java.util.Map;

public final class InventoryButtonRegistry {

    private final Map<String, InventoryButton> inventoryButtonMap = new LinkedHashMap<>();

    public void init() {

        val nextStores = NextStores.getInstance();
        val storeInventoryConfig = nextStores.getStoreInventoryConfig();
        val storesInventoryConfig = nextStores.getStoresInventoryConfig();
        val buttonsConfig = nextStores.getMainInventoryConfig().getConfigurationSection("inventory");
        val buttonParser = new InventoryButtonParser();

        register("visit", buttonParser.parse(
                storesInventoryConfig.getConfigurationSection("visit")
        ));

        register("main.yourStore", buttonParser.parse(
                buttonsConfig.getConfigurationSection("buttons.yourStore")
        ));

        register("main.allStores", buttonParser.parse(
                buttonsConfig.getConfigurationSection("buttons.allStores")
        ));

        // store inventory

        register("store.info", buttonParser.parse(
                storeInventoryConfig.getConfigurationSection("inventory.buttons.yourStore")
        ));

        register("store.location", buttonParser.parse(
                storeInventoryConfig.getConfigurationSection("inventory.buttons.location")
        ));

        register("store.description", buttonParser.parse(
                storeInventoryConfig.getConfigurationSection("inventory.buttons.description")
        ));

        register("store.state", buttonParser.parse(
                storeInventoryConfig.getConfigurationSection("inventory.buttons.state")
        ));

        register("store.delete", buttonParser.parse(
                storeInventoryConfig.getConfigurationSection("inventory.buttons.delete")
        ));

    }

    public void register(String id, InventoryButton inventoryButton) {
        inventoryButtonMap.put(id, inventoryButton);
    }

    public InventoryButton get(String id) {
        return inventoryButtonMap.get(id);
    }

}
