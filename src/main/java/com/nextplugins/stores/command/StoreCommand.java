package com.nextplugins.stores.command;

import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.entity.Player;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */
public class StoreCommand {

    @Command(
            name = "store",
            target = CommandTarget.PLAYER
    )
    public void storeCommandContext(Context<Player> context) {

        //TODO store command

    }

}
