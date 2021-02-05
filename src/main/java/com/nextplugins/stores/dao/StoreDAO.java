package com.nextplugins.stores.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.henryfabio.sqlprovider.executor.SQLExecutor;
import com.nextplugins.stores.api.store.Store;
import com.nextplugins.stores.dao.adapter.StoreAdapter;
import com.nextplugins.stores.parser.impl.LocationParser;

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
                "owner VARCHAR(16) NOT NULL PRIMARY KEY UNIQUE," +
                "openned BOOLEAN," +
                "description TEXT," +
                "visits INTEGER," +
                "likes INTEGER," +
                "dislikes INTEGER," +
                "note INTEGER," +
                "location TEXT" +
                ");");

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
                statment -> {

                    statment.set(1, store.getOwner());
                    statment.set(2, store.isOpenned());
                    statment.set(3, store.getDescription());
                    statment.set(4, store.getVisits());
                    statment.set(5, store.getLikes());
                    statment.set(6, store.getDislikes());
                    statment.set(7, store.getNote());
                    statment.set(8, LocationParser.getInstance().encode(store.getLocation()));

                }
        );

    }

}
