package com.nextplugins.stores.command;

import com.nextplugins.stores.NextStores;
import com.nextplugins.stores.configuration.values.MessageValue;
import lombok.RequiredArgsConstructor;
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

        Player player = (Player) sender;

        plugin.getInventoryRegistry().getStoreInventory().openInventory(player);

        return false;
    }

}
