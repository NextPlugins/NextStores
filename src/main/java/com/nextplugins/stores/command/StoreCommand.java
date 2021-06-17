package com.nextplugins.stores.command;

import com.nextplugins.stores.NextStores;
import com.nextplugins.stores.configuration.ConfigurationManager;
import com.nextplugins.stores.configuration.values.MessageValue;
import com.nextplugins.stores.npc.runnable.NPCRunnable;
import com.nextplugins.stores.util.LocationUtils;
import com.nextplugins.stores.util.text.ColorUtils;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bukkit.ChatColor;
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
        if (args.length > 1 && player.hasPermission("store.admin") && args[0].equalsIgnoreCase("setnpc")) {

            val location = player.getLocation();
            val configManager = ConfigurationManager.of("npc.yml");

            val config = configManager.load();
            config.set("position", LocationUtils.serialize(location));

            try {

                config.save(configManager.getFile());

                val runnable = (NPCRunnable) NextStores.getInstance().getNpcManager().getRunnable();
                runnable.spawnDefault(location);

                player.sendMessage(ColorUtils.colored("&aNPC setado com sucesso."));

            } catch (Exception exception) {
                player.sendMessage(ColorUtils.colored("&cNão foi possível setar o npc, o sistema está desabilitado por falta de dependência."));
            }

            return true;

        }


        plugin.getInventoryRegistry().getStoreInventory().openInventory(player);

        return false;
    }

}
