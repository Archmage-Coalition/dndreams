package net.eman3600.dndreams.items.pericharite;

import net.eman3600.dndreams.initializers.entity.ModAttributes;
import net.eman3600.dndreams.items.ModArmorItem;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ArmorMaterial;

public class PerichariteArmorItem extends ModArmorItem {
    public PerichariteArmorItem(ArmorMaterial material, EquipmentSlot slot, Settings settings) {
        super(material, slot, settings, (builder, uUID) -> {
            builder.put(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, new EntityAttributeModifier(uUID, "Knockback resistance bonus", .1, EntityAttributeModifier.Operation.ADDITION));
            builder.put(EntityAttributes.GENERIC_MAX_HEALTH, new EntityAttributeModifier(uUID, "Max health bonus", 2, EntityAttributeModifier.Operation.ADDITION));
            builder.put(ModAttributes.PLAYER_MAX_MANA, new EntityAttributeModifier(uUID, "Max mana bonus", 5, EntityAttributeModifier.Operation.ADDITION));
        });
    }
}
