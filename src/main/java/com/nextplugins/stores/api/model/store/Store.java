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

    private final String owner;
    private String description;

    private boolean open;

    private int visits, likes, dislikes;

    private double rating;

    public double getRating() {
        if (likes != 0 && dislikes != 0) {
            return Math.floor((double) likes / dislikes);
        } else {
            return 0;
        }
    }

    private Location location;

}
