package com.nextplugins.stores.manager;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.nextplugins.stores.api.model.store.Store;
import com.nextplugins.stores.dao.StoreDAO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */

@AllArgsConstructor
public final class StoreManager {

    @Getter private final Map<String, Store> stores = new HashMap<>();

    @Getter private final LoadingCache<UUID, Store> playersChangingStoreDescription = CacheBuilder.newBuilder()
        .maximumSize(1000)
        .expireAfterWrite(30, TimeUnit.SECONDS)
        .build(
            new CacheLoader<UUID, Store>() {
                @Override
                public Store load(@NotNull UUID key) {
                    final Player player = Bukkit.getPlayer(key);

                    if (player == null) return null;

                    return stores.get(player.getName());
                }
            }
        );

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
