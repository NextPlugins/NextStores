package com.nextplugins.stores.registry;

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

    private final StoreInventory storeInventory = new StoreInventory();

    public void init() {
        this.storeInventory.init();
    }

}
