package net.eman3600.dndreams.items.misc_armor;

import net.eman3600.dndreams.initializers.entity.ModAttributes;
import net.eman3600.dndreams.items.ModArmorItem;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ArmorMaterial;

public class ManagoldArmorItem extends ModArmorItem {
    public ManagoldArmorItem(ArmorMaterial material, EquipmentSlot slot, Settings settings) {
        super(material, slot, settings, (builder, uUID) -> {
            builder.put(ModAttributes.PLAYER_MANA_REGEN, new EntityAttributeModifier(uUID, "Mana regen bonus", .05, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
        });
    }
}
