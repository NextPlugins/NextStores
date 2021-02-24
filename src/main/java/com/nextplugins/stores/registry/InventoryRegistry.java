package com.nextplugins.stores.registry;

import com.nextplugins.stores.NextStores;
import com.nextplugins.stores.inventory.ConfigureStoryInventory;
import com.nextplugins.stores.inventory.StoreInventory;
import com.nextplugins.stores.inventory.StoreListInventory;
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
    private StoreListInventory storeListInventory;

    public void init() {

        this.storeInventory = new StoreInventory().init();
        this.configureStoryInventory = new ConfigureStoryInventory().init();
        this.storeListInventory = new StoreListInventory().init();

        NextStores.getInstance().getInjector().injectMembers(storeInventory);
        NextStores.getInstance().getInjector().injectMembers(configureStoryInventory);
        NextStores.getInstance().getInjector().injectMembers(storeListInventory);

    }

}
