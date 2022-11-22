package net.eman3600.dndreams.mixin_interfaces;

import net.minecraft.item.ItemConvertible;

public interface ComposterBlockAccess {
    void registerCompostable(float levelIncreaseChance, ItemConvertible item);
}
