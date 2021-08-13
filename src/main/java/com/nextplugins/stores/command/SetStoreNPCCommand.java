package com.nextplugins.stores.command;

import com.nextplugins.stores.NextStores;
import com.nextplugins.stores.configuration.ConfigurationManager;
import com.nextplugins.stores.configuration.values.MessageValue;
import com.nextplugins.stores.npc.runnable.NPCRunnable;
import com.nextplugins.stores.util.LocationUtils;
import lombok.val;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */
public class SetStoreNPCCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageValue.get(MessageValue::commandPlayerOnly));
            return true;
        }

        if (!sender.hasPermission("nextstores.command.setstore")) {
            sender.sendMessage(MessageValue.get(MessageValue::noPermission));
        }

        val player = (Player) sender;

        val location = player.getLocation();
        val configManager = ConfigurationManager.of("npc.yml");

        val config = configManager.load();
        config.set("position", LocationUtils.serialize(location));

        try {

            config.save(configManager.getFile());

            val runnable = (NPCRunnable) NextStores.getInstance().getNpcManager().getRunnable();
            runnable.spawnDefault(location);

            player.sendMessage(MessageValue.get(MessageValue::npcSuccess));

        } catch (Exception exception) {
            player.sendMessage(MessageValue.get(MessageValue::npcError));
        }

        return true;

    }
}
