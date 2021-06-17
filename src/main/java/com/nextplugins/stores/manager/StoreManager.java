package com.nextplugins.stores.manager;

import com.google.inject.Inject;
import com.nextplugins.stores.api.model.store.Store;
import com.nextplugins.stores.dao.StoreDAO;
import lombok.Getter;
import org.bukkit.entity.Player;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */

@Singleton
public final class StoreManager {

    @Getter private final Map<String, Store> stores = new HashMap<>();

    @Inject @Getter private StoreDAO storeDAO;

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

    public void rateStore(Store store, Player player, String rate) {
        if (rate.equalsIgnoreCase("like")) {
            store.getPlayersWhoRated().put(player.getName(), rate);
            store.like();
        } else {
            store.getPlayersWhoRated().put(player.getName(), rate);
            store.dislike();
        }
    }

}
