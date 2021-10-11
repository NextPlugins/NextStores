package com.nextplugins.stores.npc.manager;

import com.nextplugins.stores.NextStores;
import com.nextplugins.stores.listener.InteractNPCListener;
import com.nextplugins.stores.npc.runnable.NPCRunnable;
import lombok.Getter;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import java.util.logging.Level;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */

@Getter
public class NPCManager {

    protected final NextStores plugin = NextStores.getInstance();
    protected final PluginManager MANAGER = Bukkit.getPluginManager();

    private boolean enabled;
    private Runnable runnable;
    private boolean holographicDisplays;

    public void init() {

        if (!MANAGER.isPluginEnabled("CMI") && !MANAGER.isPluginEnabled("HolographicDisplays")) {
            plugin.getLogger().log(Level.WARNING,
                    "Dependência não encontrada ({0}) O ranking em NPC, Holograma e ArmorStand não serão usados.",
                    "HolographicDisplays ou CMI"
            );

            return;
        }

        if (!MANAGER.isPluginEnabled("Citizens")) {
            plugin.getLogger().log(Level.WARNING,
                    "Dependências não encontrada ({0}) O NPC não será usado.",
                    "Citizens"
            );

            return;
        }

        holographicDisplays = MANAGER.isPluginEnabled("HolographicDisplays");

        runnable = new NPCRunnable(plugin, holographicDisplays);
        runnable.run();

        enabled = true;

        val interactNPCListener = new InteractNPCListener(this);
        Bukkit.getPluginManager().registerEvents(interactNPCListener, plugin);

    }

}
