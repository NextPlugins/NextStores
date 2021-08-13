package com.nextplugins.stores.api.model.store;

import com.nextplugins.stores.configuration.values.MessageValue;
import lombok.Builder;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.entity.Player;

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
    private int visits;
    private int likes;
    private int dislikes;

    private Location location;
    private Map<String, String> playersWhoRated;

    public double getRating() {
        return Math.floor((double) likes / (likes + dislikes) * 100);
    }

    public void like() {
        setLikes(getLikes() + 1);
    }

    public void dislike() {
        setDislikes(getDislikes() + 1);
    }

    public void rate(Player player, String rateType) {
        if (rateType.equalsIgnoreCase("like")) {
            // like

            if (playersWhoRated.containsKey(player.getName())) {
                if (playersWhoRated.get(player.getName()).equalsIgnoreCase("like")) {
                    player.sendMessage("§cVocê já avaliou positivamente esta loja anteriormente.");
                } else {
                    playersWhoRated.replace(player.getName(), rateType);
                    --dislikes;
                    like();
                    player.sendMessage(MessageValue.get(MessageValue::storeLike));
                }
            } else {
                playersWhoRated.put(player.getName(), rateType);
                like();
                player.sendMessage(MessageValue.get(MessageValue::storeLike));
            }
        } else {
            // dislike

            if (playersWhoRated.containsKey(player.getName())) {
                if (playersWhoRated.get(player.getName()).equalsIgnoreCase("dislike")) {
                    player.sendMessage("§cVocê já avaliou negativamente esta loja anteriormente.");
                } else {
                    playersWhoRated.replace(player.getName(), rateType);
                    --likes;
                    dislike();
                    player.sendMessage(MessageValue.get(MessageValue::storeDislike));
                }
            } else {
                playersWhoRated.put(player.getName(), rateType);
                dislike();
                player.sendMessage(MessageValue.get(MessageValue::storeDislike));
            }
        }
    }

}
