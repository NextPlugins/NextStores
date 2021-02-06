package com.nextplugins.stores.api;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.nextplugins.stores.api.store.Store;
import com.nextplugins.stores.manager.StoreManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Getter public static final NextStoresAPI instance = new NextStoresAPI();

    @Inject private StoreManager storeManager;

    /**
     * Get a store by owner name
     *
     * @param owner name
     * @return {@link Store} finded by owner name
     */
    public Store findStoreByOwner(String owner) {
        return storeManager.getStores().getOrDefault(owner, null);
    }

    /**
     * Search all stores to look for one with the entered custom filter
     *
     * @param filter custom filter to search
     * @return {@link Optional} with the {@link Store} found
     */
    public Optional<Store> findStoreByFilter(Predicate<Store> filter) {
        return allStores()
                .stream()
                .filter(filter)
                .findAny();
    }

    /**
     * Search all stores to look for every with the entered custom filter
     *
     * @param filter custom filter to search
     * @return {@link Optional} with the {@link Store} found
     */
    public Set<Store> findStoresByFilter(Predicate<Store> filter) {
        return allStores()
                .stream()
                .filter(filter)
                .collect(Collectors.toSet());
    }

    public Set<Store> allStores() {
        return ImmutableSet.copyOf(storeManager.getStores().values());
    }

}
