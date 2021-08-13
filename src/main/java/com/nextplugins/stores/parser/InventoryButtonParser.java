package com.nextplugins.stores.parser;

import com.nextplugins.stores.api.model.store.MaterialData;
import com.nextplugins.stores.inventory.button.InventoryButton;
import com.nextplugins.stores.util.TypeUtil;
import com.nextplugins.stores.util.text.ColorUtil;
import lombok.val;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.stream.Collectors;

public final class InventoryButtonParser {

    public InventoryButton parse(ConfigurationSection section) {

        val itemStack = TypeUtil.convertFromLegacy(
                section.getString("material"),
                (byte) section.getInt("data"));

        return InventoryButton.builder()
                .displayName(ColorUtil.colored(section.getString("displayName")))
                .lore(section.getStringList("lore").stream()
                        .map(ColorUtil::colored)
                        .collect(Collectors.toList()))
                .materialData(itemStack == null ? new MaterialData(Material.BARRIER, 0, false) : MaterialData.of(itemStack, false))
                .inventorySlot(section.getInt("inventorySlot"))
                .username(section.getString("username"))
                .build();
    }

}
