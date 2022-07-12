package net.eman3600.dndreams.items.managold;

import net.eman3600.dndreams.initializers.ModAttributes;
import net.eman3600.dndreams.util.ModArmorMaterials;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;

import java.util.UUID;

public class ManagoldArmor extends ArmorItem {
    public ManagoldArmor(ArmorMaterial material, EquipmentSlot slot, Settings settings) {
        super(material, slot, settings);
    }
}
