package com.nextplugins.stores.registry;

import com.nextplugins.stores.NextStores;
import com.nextplugins.stores.inventory.ConfigureStoreInventory;
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
    private ConfigureStoreInventory configureStoreInventory;
    private StoreListInventory storeListInventory;

    public void init() {

        this.storeInventory = new StoreInventory().init();
        this.configureStoreInventory = new ConfigureStoreInventory().init();
        this.storeListInventory = new StoreListInventory(NextStores.getInstance()).init();

    }

}
