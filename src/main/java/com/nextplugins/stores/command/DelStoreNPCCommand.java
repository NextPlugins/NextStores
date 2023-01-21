package com.nextplugins.stores.command;

import com.nextplugins.stores.NextStores;
import com.nextplugins.stores.configuration.ConfigurationManager;
import com.nextplugins.stores.configuration.values.MessageValue;
import com.nextplugins.stores.npc.runnable.NPCRunnable;
import com.nextplugins.stores.util.ColorUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DelStoreNPCCommand implements CommandExecutor {

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageValue.get(MessageValue::commandPlayerOnly));

            return true;
        }

        final Player player = (Player) sender;

        if (!player.hasPermission("nextstores.command.setstore")) {
            player.sendMessage(MessageValue.get(MessageValue::noPermission));

            return true;
        }

        final ConfigurationManager manager = ConfigurationManager.of("npc.yml");
        final FileConfiguration configuration = manager.load();

        configuration.set("position", "");

        try {
            configuration.save(manager.getFile());

            final NPCRunnable runnable =
                    (NPCRunnable) NextStores.getInstance().getNpcManager().getRunnable();

            runnable.despawn();

            player.sendMessage(ColorUtil.colored("&aNPC deletado com sucesso."));
        } catch (Exception exception) {
            player.sendMessage(MessageValue.get(MessageValue::npcError));
        }
        return false;
    }
}
