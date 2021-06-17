package com.nextplugins.stores.npc.runnable;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.nextplugins.onlinetime.configuration.values.NPCValue;
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

import java.util.List;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */

@RequiredArgsConstructor
public class NPCRunnable implements Runnable {

    @Getter private NPC NPC;
    @Getter private Hologram hologram;

    private final Plugin plugin;

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

        val hologram = HologramsAPI.createHologram(this.plugin, location.clone().add(0, hologramAddition, 0));

        for (int i = 0; i < hologramMessage.size(); i++) {

            String line = hologramMessage.get(i);
            hologram.insertTextLine(i, line);

        }

        this.hologram = hologram;

        return true;

    }

    /**
     * Delete cached npc and hologram
     */
    public void despawn() {

        if (NPC == null || hologram == null) return;

        NPC.destroy();
        hologram.delete();

    }

}
