package net.eman3600.dndreams.items.hellsteel;

import net.eman3600.dndreams.items.ModArmorItem;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;

public class CorruptArmorItem extends ModArmorItem {
    public CorruptArmorItem(ArmorMaterial material, EquipmentSlot slot, Settings settings) {
        super(material, slot, settings, (builder, uUID) -> {});
    }
}
