package com.nextplugins.stores.serializer.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nextplugins.stores.serializer.Serializer;
import com.nextplugins.stores.util.SimpleLocation;
import lombok.Getter;
import org.bukkit.Location;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */
public class LocationSerializer implements Serializer<Location> {

    @Getter
    private static final LocationSerializer instance = new LocationSerializer();

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public Location decode(String data) {
        return GSON.fromJson(data, SimpleLocation.class).toBukkit();
    }

    @Override
    public String encode(Location data) {
        return GSON.toJson(SimpleLocation.of(data));
    }
}
