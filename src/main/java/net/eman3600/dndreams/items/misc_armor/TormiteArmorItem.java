package net.eman3600.dndreams.items.misc_armor;

import net.eman3600.dndreams.initializers.entity.ModAttributes;
import net.eman3600.dndreams.items.ModArmorItem;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ArmorMaterial;

public class TormiteArmorItem extends ModArmorItem {
    public TormiteArmorItem(ArmorMaterial material, EquipmentSlot slot, Settings settings) {
        super(material, slot, settings, (builder, uUID) -> {
            builder.put(ModAttributes.PLAYER_MANA_REGEN, new EntityAttributeModifier(uUID, "Mana regen bonus", .05, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
            builder.put(ModAttributes.PLAYER_MAX_MANA, new EntityAttributeModifier(uUID, "Max mana bonus", 5, EntityAttributeModifier.Operation.ADDITION));
        });
    }
}