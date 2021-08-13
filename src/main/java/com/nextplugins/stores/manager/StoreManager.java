package com.nextplugins.stores.manager;

import com.nextplugins.stores.api.model.store.Store;
import com.nextplugins.stores.dao.StoreDAO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */

@AllArgsConstructor
public final class StoreManager {

    @Getter private final Map<String, Store> stores = new HashMap<>();

    @Getter private final StoreDAO storeDAO;

    public void init() {
        this.storeDAO.createTable();
        this.storeDAO.selectAll().forEach(this::addStore);
    }

    public void addStore(Store store) {
        this.stores.put(store.getOwner(), store);
        this.storeDAO.insert(store);
    }

    public void deleteStore(Store store) {
        this.stores.remove(store.getOwner());
        this.storeDAO.delete(store.getOwner());
    }

}
