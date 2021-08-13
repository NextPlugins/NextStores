package com.nextplugins.stores.dao.adapter;

import com.henryfabio.sqlprovider.executor.adapter.SQLResultAdapter;
import com.henryfabio.sqlprovider.executor.result.SimpleResultSet;
import com.nextplugins.stores.api.model.store.Store;
import com.nextplugins.stores.serializer.impl.LocationSerializer;
import com.nextplugins.stores.util.MapHelper;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */

public class StoreAdapter implements SQLResultAdapter<Store> {

    @Override
    public Store adaptResult(SimpleResultSet resultSet) {

        final Map<String, String> ratings = MapHelper.fromDatabase(resultSet.get("ratings"));

        return Store.builder()
            .owner(resultSet.get("owner"))
            .description(resultSet.get("description"))
            .open(Boolean.parseBoolean(resultSet.get("open")))
            .visits(resultSet.get("visits"))
            .likes(resultSet.get("likes"))
            .dislikes(resultSet.get("dislikes"))
            .location(LocationSerializer.getInstance().decode(resultSet.get("location")))
            .playersWhoRated(ratings == null ? new LinkedHashMap<>() : ratings)
            .build();
    }

}