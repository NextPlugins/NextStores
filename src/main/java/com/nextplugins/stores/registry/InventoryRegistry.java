package com.nextplugins.stores.registry;

import com.nextplugins.stores.NextStores;
import com.nextplugins.stores.inventory.ConfigureStoreInventory;
import com.nextplugins.stores.inventory.StoreDeleteConfirmInventory;
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
    private StoreDeleteConfirmInventory storeDeleteConfirmInventory;

    public void init() {
        final NextStores nextStores = NextStores.getInstance();

        this.storeInventory = new StoreInventory(nextStores).init();
        this.configureStoreInventory = new ConfigureStoreInventory(nextStores).init();
        this.storeListInventory = new StoreListInventory(nextStores).init();
        this.storeDeleteConfirmInventory = new StoreDeleteConfirmInventory(nextStores).init();

    }

}
