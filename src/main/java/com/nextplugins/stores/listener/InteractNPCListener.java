package com.nextplugins.stores.listener;

import com.nextplugins.stores.npc.manager.NPCManager;
import com.nextplugins.stores.npc.runnable.NPCRunnable;
import lombok.AllArgsConstructor;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */

@AllArgsConstructor
public class InteractNPCListener implements Listener {

    private final NPCManager npcManager;

    @EventHandler
    public void interactNpc(NPCRightClickEvent event) {

        if (event.getNPC() == null || !this.npcManager.isEnabled()) return;

        NPCRunnable runnable = (NPCRunnable) this.npcManager.getRunnable();
        if (runnable.getNPC() == null || event.getNPC().getId() != runnable.getNPC().getId()) return;

        event.getClicker().performCommand("lojas");

    }

}
