package com.nextplugins.stores.registry;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.nextplugins.stores.inventory.button.InventoryButton;
import com.nextplugins.stores.parser.InventoryButtonParser;
import org.bukkit.configuration.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Singleton
public final class InventoryButtonRegistry {

    private final Map<String, InventoryButton> inventoryButtonMap = new LinkedHashMap<>();

    @Inject @Named("categories") private Configuration categoriesConfig;
    @Inject private InventoryButtonParser inventoryButtonParser;

    public void init() {

        register("main.yourStore", inventoryButtonParser.parse(
                categoriesConfig.getConfigurationSection("inventory.main.buttons.personalMarket")
        ));

        register("main.allStores", inventoryButtonParser.parse(
                categoriesConfig.getConfigurationSection("inventory.main.buttons.sellingMarket")
        ));

    }

    public void register(String id, InventoryButton inventoryButton) {
        inventoryButtonMap.put(id, inventoryButton);
    }

    public InventoryButton get(String id) {
        return inventoryButtonMap.get(id);
    }

}
