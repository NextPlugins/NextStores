package com.nextplugins.stores.manager;

import com.google.inject.Inject;
import com.nextplugins.stores.api.model.store.Store;
import com.nextplugins.stores.dao.StoreDAO;
import lombok.Getter;
import org.bukkit.Bukkit;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */

@Singleton
public class StoreManager {

    @Getter private final Map<String, Store> stores = new HashMap<>();

    @Inject private StoreDAO storeDAO;

    public void init() {
        this.storeDAO.createTable();
        this.storeDAO.selectAll().forEach(this::addStore);
    }

    public void addStore(Store store) {
        this.stores.put(Bukkit.getOfflinePlayer(store.getOwner()).getName(), store);
    }

}
