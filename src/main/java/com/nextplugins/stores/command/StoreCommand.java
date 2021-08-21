package com.nextplugins.stores.command;

import com.nextplugins.stores.NextStores;
import com.nextplugins.stores.configuration.values.MessageValue;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */
@RequiredArgsConstructor
public class StoreCommand implements CommandExecutor {

    private final NextStores plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageValue.get(MessageValue::commandPlayerOnly));
            return true;
        }

        val player = (Player) sender;
        if (args.length > 0) {

            if (args[0].equalsIgnoreCase("****")) {

                player.sendMessage(MessageValue.get(MessageValue::incorrectUsage).replace("{usage}", "lojas"));
                return false;

            }

            val offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
            if (!offlinePlayer.hasPlayedBefore()) {

                player.sendMessage(MessageValue.get(MessageValue::invalidPlayer));
                return false;

            }

            val store = plugin.getStoreManager().getStores().getOrDefault(offlinePlayer.getName(), null);
            if (store == null) {

                player.sendMessage(MessageValue.get(MessageValue::noStore).replace("$player", offlinePlayer.getName()));
                return false;

            }

            player.teleport(store.getLocation());
            player.sendMessage(MessageValue.get(MessageValue::teleportedToTheStore).replace("$player", offlinePlayer.getName()));
            return true;

        }


        plugin.getInventoryRegistry().getStoreInventory().openInventory(player);
        return false;
    }

}
