package com.nextplugins.stores.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.henryfabio.sqlprovider.executor.SQLExecutor;
import com.nextplugins.stores.api.model.store.Store;
import com.nextplugins.stores.dao.adapter.StoreAdapter;
import com.nextplugins.stores.serializer.impl.LocationSerializer;

import java.util.Set;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */

@Singleton
public final class StoreDAO {

    private static final String TABLE = "stores";

    @Inject private SQLExecutor sqlExecutor;

    public void createTable() {

        this.sqlExecutor.updateQuery("CREATE TABLE IF NOT EXISTS " + TABLE + "(" +
                "owner CHAR(36) NOT NULL PRIMARY KEY UNIQUE," +
                "open TEXT," +
                "description TEXT," +
                "visits INTEGER," +
                "likes INTEGER," +
                "dislikes INTEGER," +
                "rating DOUBLE," +
                "location TEXT" +
                ");");

    }

    public Set<Store> selectAll() {

        return this.sqlExecutor.resultManyQuery(
                "SELECT * FROM " + TABLE,
                simpleStatement -> {
                },
                StoreAdapter.class
        );

    }

    public Set<Store> selectAll(String preferences) {

        return this.sqlExecutor.resultManyQuery(
                "SELECT * FROM " + TABLE + " " + preferences,
                simpleStatement -> {
                },
                StoreAdapter.class
        );

    }

    public void insert(Store store) {

        this.sqlExecutor.updateQuery(
                String.format("REPLACE INTO %s VALUES(?,?,?,?,?,?,?,?)", TABLE),
                statement -> {

                    statement.set(1, store.getOwner().toString());
                    statement.set(2, store.isOpen() ? "true" : "false");
                    statement.set(3, store.getDescription());
                    statement.set(4, store.getVisits());
                    statement.set(5, store.getLikes());
                    statement.set(6, store.getDislikes());
                    statement.set(7, store.getRating());
                    statement.set(8, LocationSerializer.getInstance().encode(store.getLocation()));

                }
        );

    }

}
