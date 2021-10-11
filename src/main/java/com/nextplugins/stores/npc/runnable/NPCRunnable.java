package com.nextplugins.stores.npc.runnable;

import com.Zrips.CMI.CMI;
import com.Zrips.CMI.Modules.Holograms.CMIHologram;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.google.common.collect.Lists;
import com.nextplugins.stores.NextStores;
import com.nextplugins.stores.configuration.values.NPCValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.LookClose;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */

@Getter
@RequiredArgsConstructor
public class NPCRunnable implements Runnable {

    private NPC NPC;

    private final Plugin plugin;
    private final boolean holographicDisplays;

    @Override
    public void run() {
        val location = NPCValue.get(NPCValue::position);
        if (location == null) return;

        spawnDefault(location);
    }

    /**
     * Default spawn of npc & hologram
     */
    public void spawnDefault(Location location) {
        Bukkit.getScheduler().runTask(this.plugin, () -> spawn(
                location,
                NPCValue.get(NPCValue::npcName),
                NPCValue.get(NPCValue::skinNick),
                NPCValue.get(NPCValue::hologramMessage),
                NPCValue.get(NPCValue::heightToAdd)
        ));
    }

    /**
     * Spawn npc and hologram
     *
     * @param location to spawn the npc and hologram
     */
    public boolean spawn(Location location,
                         String npcName,
                         String skinNick,
                         List<String> hologramMessage,
                         double hologramAddition) {

        // prevent duplicate npc / holograms
        despawn();

        val registry = CitizensAPI.getNPCRegistry();

        // npc implementation
        val npc = registry.createNPC(EntityType.PLAYER, npcName);
        npc.data().set(NPC.PLAYER_SKIN_UUID_METADATA, Bukkit.getOfflinePlayer(skinNick).getName());
        npc.setProtected(true);
        npc.spawn(location);

        if (NPCValue.get(NPCValue::lookCLose)) {

            val lookClose = new LookClose();
            lookClose.lookClose(true);

            npc.addTrait(lookClose);

        }

        NPC = npc;

        // hologram implementation
        if (hologramMessage.isEmpty()) return true;

        val hologramLocation = location.clone().add(0, hologramAddition, 0);
        if (holographicDisplays) {
            val hologram = HologramsAPI.createHologram(this.plugin, hologramLocation);

            for (int i = 0; i < hologramMessage.size(); i++) {
                String line = hologramMessage.get(i);
                hologram.insertTextLine(i, line);
            }
        } else {
            val cmiHologram = new CMIHologram("NextStores", hologramLocation);
            hologramMessage.forEach(cmiHologram::addLine);

            CMI.getInstance().getHologramManager().addHologram(cmiHologram);
            cmiHologram.update();
        }

        return true;

    }

    /**
     * Delete cached npc and hologram
     */
    public void despawn() {

        if (NPC == null) return;

        NPC.destroy();
        if (holographicDisplays) {
            HologramsAPI.getHolograms(plugin).forEach(Hologram::delete);
        } else {
            val cmiHologram = CMI.getInstance().getHologramManager().getHolograms().get("NextStores");
            if (cmiHologram == null) return;

            CMI.getInstance().getHologramManager().removeHolo(cmiHologram);
        }

    }

}
