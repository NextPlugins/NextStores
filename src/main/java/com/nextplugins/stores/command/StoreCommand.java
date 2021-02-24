package com.nextplugins.stores.command;

import com.google.inject.Inject;
import com.nextplugins.stores.registry.InventoryRegistry;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.entity.Player;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */
public class StoreCommand {

    @Inject private InventoryRegistry inventoryRegistry;

    @Command(
            name = "store",
            aliases = {"lojas", "setloja"},
            target = CommandTarget.PLAYER
    )
    public void storeCommandContext(Context<Player> context) {
        this.inventoryRegistry.getStoreInventory().openInventory(context.getSender());
    }

}
