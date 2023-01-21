package com.nextplugins.stores.dao;

import com.henryfabio.sqlprovider.executor.SQLExecutor;
import com.nextplugins.stores.api.model.store.Store;
import com.nextplugins.stores.dao.adapter.StoreAdapter;
import com.nextplugins.stores.serializer.impl.LocationSerializer;
import com.nextplugins.stores.util.MapHelper;
import lombok.AllArgsConstructor;

import java.util.Set;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */
@AllArgsConstructor
public final class StoreDAO {

    private static final String TABLE = "stores";

    private final SQLExecutor sqlExecutor;

    public void createTable() {

        sqlExecutor.updateQuery(
                "CREATE TABLE IF NOT EXISTS " + TABLE + "(" + "owner VARCHAR(16) NOT NULL PRIMARY KEY UNIQUE,"
                        + "open TEXT,"
                        + "description TEXT,"
                        + "visits INTEGER,"
                        + "likes INTEGER,"
                        + "dislikes INTEGER,"
                        + "location TEXT,"
                        + "ratings TEXT"
                        + ");");
    }

    public Set<Store> selectAll() {
        return sqlExecutor.resultManyQuery("SELECT * FROM " + TABLE, simpleStatement -> {}, StoreAdapter.class);
    }

    public Set<Store> selectAll(String preferences) {

        return sqlExecutor.resultManyQuery(
                "SELECT * FROM " + TABLE + " " + preferences, simpleStatement -> {}, StoreAdapter.class);
    }

    public void insert(Store store) {
        sqlExecutor.updateQuery(String.format("REPLACE INTO %s VALUES(?,?,?,?,?,?,?,?)", TABLE), statement -> {
            statement.set(1, store.getOwner());
            statement.set(2, store.isOpen() ? "true" : "false");
            statement.set(3, store.getDescription());
            statement.set(4, store.getVisits());
            statement.set(5, store.getLikes());
            statement.set(6, store.getDislikes());
            statement.set(7, LocationSerializer.getInstance().encode(store.getLocation()));
            statement.set(8, MapHelper.toDatabase(store.getPlayersWhoRated()));
        });
    }

    public void delete(String owner) {
        sqlExecutor.updateQuery(String.format("DELETE FROM %s WHERE owner = '%s'", TABLE, owner));
    }
}
