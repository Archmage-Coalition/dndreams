package net.eman3600.dndreams.items.managold;

import net.eman3600.dndreams.initializers.entity.ModAttributes;
import net.eman3600.dndreams.items.ModArmorItem;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ArmorMaterial;

public class ManagoldArmorItem extends ModArmorItem {
    public ManagoldArmorItem(ArmorMaterial material, EquipmentSlot slot, Settings settings) {
        super(material, slot, settings, (builder, uUID) -> {
            builder.put(ModAttributes.PLAYER_MAX_MANA, new EntityAttributeModifier(uUID, "Max mana bonus", 2, EntityAttributeModifier.Operation.ADDITION));
        });
    }
}
