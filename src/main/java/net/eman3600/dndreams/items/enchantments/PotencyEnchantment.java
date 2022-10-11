package net.eman3600.dndreams.items.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;

public class PotencyEnchantment extends MagicDamageEnchantment {
    public PotencyEnchantment(Rarity weight, int maxLevel, EquipmentSlot[] slotTypes) {
        super(weight, maxLevel, slotTypes);
    }

    @Override
    protected boolean canAccept(Enchantment other) {
        return other.canCombine(Enchantments.SHARPNESS) && other != Enchantments.SHARPNESS;
    }
}
