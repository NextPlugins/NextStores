package com.nextplugins.stores.dao.adapter;

import com.google.inject.Singleton;
import com.henryfabio.sqlprovider.executor.adapter.SQLResultAdapter;
import com.henryfabio.sqlprovider.executor.result.SimpleResultSet;
import com.nextplugins.stores.api.model.store.Store;
import com.nextplugins.stores.serializer.impl.LocationSerializer;

import java.util.UUID;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */

@Singleton
public class StoreAdapter implements SQLResultAdapter<Store> {

    @Override
    public Store adaptResult(SimpleResultSet resultSet) {

        return Store.builder()
                .owner(UUID.fromString(resultSet.get("owner")))
                .description(resultSet.get("description"))
                .open(Boolean.parseBoolean(resultSet.get("open")))
                .visits(resultSet.get("visits"))
                .likes(resultSet.get("likes"))
                .dislikes(resultSet.get("dislikes"))
                .location(LocationSerializer.getInstance().decode(resultSet.get("location")))
                .build();

    }

}