package com.nextplugins.stores.registry;

import com.nextplugins.stores.NextStores;
import com.nextplugins.stores.inventory.ConfigureStoryInventory;
import com.nextplugins.stores.inventory.StoreInventory;
import com.nextplugins.stores.inventory.StoreListInventory;
import lombok.Getter;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */

@Getter
public class InventoryRegistry {

    private StoreInventory storeInventory;
    private ConfigureStoryInventory configureStoryInventory;
    private StoreListInventory storeListInventory;

    public void init() {

        this.storeInventory = new StoreInventory().init();
        this.configureStoryInventory = new ConfigureStoryInventory().init();
        this.storeListInventory = new StoreListInventory(NextStores.getInstance()).init();

    }

}
