package com.nextplugins.stores.registry;

import com.nextplugins.stores.inventory.ConfigureStoryInventory;
import com.nextplugins.stores.inventory.StoreInventory;
import lombok.Getter;

import javax.inject.Singleton;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */

@Getter
@Singleton
public class InventoryRegistry {

    private StoreInventory storeInventory;
    private ConfigureStoryInventory configureStoryInventory;

    public void init() {

        this.storeInventory = new StoreInventory().init();
        this.configureStoryInventory = new ConfigureStoryInventory().init();

    }

}
