package com.nextplugins.stores.dao.adapter;

import com.google.inject.Singleton;
import com.henryfabio.sqlprovider.executor.adapter.SQLResultAdapter;
import com.henryfabio.sqlprovider.executor.result.SimpleResultSet;
import com.nextplugins.stores.api.store.Store;
import com.nextplugins.stores.parser.impl.LocationParser;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */

@Singleton
public class StoreAdapter implements SQLResultAdapter<Store> {

    @Override
    public Store adaptResult(SimpleResultSet resultSet) {

        return Store.builder()
                .owner(resultSet.get("owner"))
                .description(resultSet.get("description"))
                .openned(resultSet.get("openned"))
                .visits(resultSet.get("visits"))
                .likes(resultSet.get("likes"))
                .dislikes(resultSet.get("dislikes"))
                .note(resultSet.get("note"))
                .location(LocationParser.getInstance().decode(resultSet.get("location")))
                .build();

    }

}