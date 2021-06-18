package com.nextplugins.stores.manager;

import com.google.inject.Inject;
import com.nextplugins.stores.api.model.store.Store;
import com.nextplugins.stores.configuration.values.MessageValue;
import com.nextplugins.stores.dao.StoreDAO;
import lombok.Getter;
import org.bukkit.entity.Player;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */

@Singleton
public final class StoreManager {

    @Getter private final Map<String, Store> stores = new HashMap<>();

    @Inject @Getter private StoreDAO storeDAO;

    public void init() {
        this.storeDAO.createTable();
        this.storeDAO.selectAll().forEach(this::addStore);
    }

    public void addStore(Store store) {
        this.stores.put(store.getOwner(), store);
        this.storeDAO.insert(store);
    }

    public void deleteStore(Store store) {
        this.stores.remove(store.getOwner());
        this.storeDAO.delete(store.getOwner());
    }

    public void rateStore(Store store, Player player, String rate) {
        if (rate.equalsIgnoreCase("like")) {
            // like

            if (store.getPlayersWhoRated().containsKey(player.getName())) {
                if (store.getPlayersWhoRated().get(player.getName()).equalsIgnoreCase("like")) {
                    player.sendMessage("§cVocê já avaliou positivamente esta loja anteriormente.");
                } else {
                    store.getPlayersWhoRated().replace(player.getName(), rate);
                    store.setDislikes(store.getDislikes() - 1);
                    store.like();
                    player.sendMessage(MessageValue.get(MessageValue::storeLike));
                }
            } else {
                store.getPlayersWhoRated().put(player.getName(), rate);
                store.like();
                player.sendMessage(MessageValue.get(MessageValue::storeLike));
            }
        } else {
            // dislike

            if (store.getPlayersWhoRated().containsKey(player.getName())) {
                if (store.getPlayersWhoRated().get(player.getName()).equalsIgnoreCase("dislike")) {
                    player.sendMessage("§cVocê já avaliou negativamente esta loja anteriormente.");
                } else {
                    store.getPlayersWhoRated().replace(player.getName(), rate);
                    store.setLikes(store.getLikes() - 1);
                    store.dislike();
                    player.sendMessage(MessageValue.get(MessageValue::storeDislike));
                }
            } else {
                store.getPlayersWhoRated().put(player.getName(), rate);
                store.dislike();
                player.sendMessage(MessageValue.get(MessageValue::storeDislike));
            }
        }
    }

}
