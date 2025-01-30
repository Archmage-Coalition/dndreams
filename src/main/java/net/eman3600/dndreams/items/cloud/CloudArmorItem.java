package net.eman3600.dndreams.items.cloud;

import net.eman3600.dndreams.items.tool_mirror.ModArmorItem;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ArmorMaterial;

public class CloudArmorItem extends ModArmorItem {
    public CloudArmorItem(ArmorMaterial material, EquipmentSlot slot, Settings settings) {
        super(material, slot, settings, (builder, uUID) -> {
            builder.put(EntityAttributes.GENERIC_MOVEMENT_SPEED, new EntityAttributeModifier(uUID, "Movement speed bonus", .025, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
        });
    }
}
