package com.nextplugins.stores.api;

import com.google.common.collect.ImmutableSet;
import com.nextplugins.stores.NextStores;
import com.nextplugins.stores.api.model.store.Store;
import com.nextplugins.stores.manager.StoreManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NextStoresAPI {

    @Getter
    public static final NextStoresAPI instance = new NextStoresAPI();

    private final StoreManager storeManager = NextStores.getInstance().getStoreManager();

    /**
     * Get a store by owner name
     *
     * @param owner name
     * @return {@link Store} found by owner name
     */
    public Optional<Store> findStoreByOwner(String owner) {
        return Optional.ofNullable(storeManager.getStores().getOrDefault(owner, null));
    }

    public Optional<Store> findStoreByPlayer(Player player) {
        return findStoreByOwner(player.getName());
    }

    /**
     * Search all stores to look for one with the entered custom filter
     *
     * @param filter custom filter to search
     * @return {@link Optional} with the {@link Store} found
     */
    public Optional<Store> findStoreByFilter(Predicate<Store> filter) {
        return allStores().stream().filter(filter).findAny();
    }

    /**
     * Search all stores to look for every with the entered custom filter
     *
     * @param filter custom filter to search
     * @return {@link Optional} with the {@link Store} found
     */
    public Set<Store> findStoresByFilter(Predicate<Store> filter) {
        return allStores().stream().filter(filter).collect(Collectors.toSet());
    }

    /**
     * Delete {@link Store} by owner name
     *
     * @param owner name
     */
    public void deleteStoreByOwner(String owner) {
        storeManager.getStores().remove(owner);
    }

    /**
     * Get all stores
     *
     * @return {@link Set} of all {@link Store}
     */
    public Set<Store> allStores() {
        return ImmutableSet.copyOf(storeManager.getStores().values());
    }
}
