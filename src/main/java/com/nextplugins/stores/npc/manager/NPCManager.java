package com.nextplugins.stores.npc.manager;

import com.nextplugins.stores.NextStores;
import com.nextplugins.stores.listener.InteractNPCListener;
import com.nextplugins.stores.npc.runnable.NPCRunnable;
import lombok.Getter;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */

public class NPCManager {

    protected final NextStores plugin = NextStores.getInstance();
    protected final PluginManager MANAGER = Bukkit.getPluginManager();

    protected final String CITIZENS = "Citizens";
    protected final String HOLOGRAPHIC_DISPLAYS = "HolographicDisplays";

    @Getter private boolean enabled;
    @Getter private Runnable runnable;

    public void init() {

        if (!MANAGER.isPluginEnabled(CITIZENS) || !MANAGER.isPluginEnabled(HOLOGRAPHIC_DISPLAYS)) {

            plugin.getLogger().warning(
                    String.format("Dependências não encontradas (%s, %s) O NPC não será usado.",
                            CITIZENS,
                            HOLOGRAPHIC_DISPLAYS
                    )
            );

            return;

        }

        runnable = new NPCRunnable(plugin);
        runnable.run();

        enabled = true;

        val interactNPCListener = new InteractNPCListener(this);
        Bukkit.getPluginManager().registerEvents(interactNPCListener, plugin);

    }

}
