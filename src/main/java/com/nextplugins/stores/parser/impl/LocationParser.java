package com.nextplugins.stores.parser.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nextplugins.stores.parser.Parser;
import com.nextplugins.stores.utils.SimpleLocation;
import lombok.Getter;
import org.bukkit.Location;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */

public class LocationParser implements Parser<Location> {

    @Getter private static final LocationParser instance = new LocationParser();
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
