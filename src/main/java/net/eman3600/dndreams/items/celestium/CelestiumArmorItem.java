package net.eman3600.dndreams.items.celestium;

import net.eman3600.dndreams.items.ModArmorItem;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ArmorMaterial;

public class CelestiumArmorItem extends ModArmorItem {
    public CelestiumArmorItem(ArmorMaterial material, EquipmentSlot slot, Settings settings) {
        super(material, slot, settings, (builder, uUID) -> {
            builder.put(EntityAttributes.GENERIC_MOVEMENT_SPEED, new EntityAttributeModifier(uUID, "Movement speed bonus", .05, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
        });
    }
}
