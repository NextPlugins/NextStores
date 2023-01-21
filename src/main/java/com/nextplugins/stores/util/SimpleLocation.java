package com.nextplugins.stores.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.Serializable;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SimpleLocation implements Serializable {

    private final String world;

    private final double x;
    private final double y;
    private final double z;

    private final float yaw;
    private final float pitch;

    public static SimpleLocation of(Location location) {

        return new SimpleLocation(
                location.getWorld().getName(),
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch());
    }

    public World getWorld() {
        return Bukkit.getWorld(this.world);
    }

    public Location toBukkit() {
        return new Location(this.getWorld(), x, y, z, this.yaw, this.pitch);
    }
}
