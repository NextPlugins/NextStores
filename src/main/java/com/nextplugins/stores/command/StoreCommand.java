package com.nextplugins.stores.command;

import com.google.inject.Inject;
import com.nextplugins.stores.configuration.values.MessageValue;
import com.nextplugins.stores.registry.InventoryRegistry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */
public final class StoreCommand implements CommandExecutor {

    @Inject private InventoryRegistry inventoryRegistry;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageValue.get(MessageValue::commandPlayerOnly));
            return true;
        }

        Player player = (Player) sender;

        this.inventoryRegistry.getStoreInventory().openInventory(player);

        return false;
    }
}
