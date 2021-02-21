package com.nextplugins.stores.api.model.store;

import lombok.Builder;
import lombok.Data;
import org.bukkit.Location;

import java.util.UUID;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */

@Data
@Builder
public class Store {

    private final UUID owner;
    private String description;

    private boolean open;

    private int visits;
    private int likes;
    private int dislikes;
    private double rating;

    private Location location;

}
