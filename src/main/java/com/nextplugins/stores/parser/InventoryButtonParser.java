package com.nextplugins.stores.parser;

import com.google.inject.Singleton;
import com.nextplugins.stores.inventory.button.InventoryButton;
import com.nextplugins.stores.util.text.ColorUtil;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.material.MaterialData;

import java.util.stream.Collectors;

@Singleton
public final class InventoryButtonParser {

    public InventoryButton parse(ConfigurationSection section) {
        return InventoryButton.builder()
                .displayName(ColorUtil.colored(section.getString("displayName")))
                .lore(section.getStringList("lore").stream()
                        .map(ColorUtil::colored)
                        .collect(Collectors.toList()))
                .materialData(new MaterialData(
                        Material.getMaterial(section.getString("material")),
                        (byte) section.getInt("data")))
                .inventorySlot(section.getInt("inventorySlot"))
                .username(section.getString("username"))
                .build();
    }

}
