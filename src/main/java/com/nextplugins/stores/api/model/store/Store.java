package com.nextplugins.stores.api.model.store;

import lombok.Builder;
import lombok.Data;
import org.bukkit.Location;

import java.util.Map;

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

    public double getRating() {
        return Math.floor((double) likes / (likes + dislikes) * 100);
    }

    public void like() {
        setLikes(getLikes() + 1);
    }

    public void dislike() {
        setDislikes(getDislikes() + 1);
    }

    private Map<String, String> playersWhoRated;

    private Location location;

}
