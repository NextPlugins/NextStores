package com.nextplugins.stores.api.model.store;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */
@Data
@AllArgsConstructor
public class MaterialData {

    private final Material material;
    private final int data;
    private final boolean ignoreData;

    public static MaterialData of(@NonNull ItemStack item, boolean ignoreData) {
        return new MaterialData(item.getType(), item.getDurability(), ignoreData);
    }

    public ItemStack toItemStack(int quantity) {
        return new ItemStack(material, quantity, (short) data);
    }
}
