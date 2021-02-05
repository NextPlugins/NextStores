package com.nextplugins.stores.api.store;

import lombok.Builder;
import lombok.Data;
import org.bukkit.Location;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */

@Data
@Builder
public class Store {

    private final String owner;
    private String description;

    private boolean openned;

    private int visits;
    private int likes;
    private int dislikes;
    private int note;

    private Location location;

}
