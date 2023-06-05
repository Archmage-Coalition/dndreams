package net.eman3600.dndreams.items.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public abstract class AliasedEnchantment extends Enchantment {
    private final int maxLevel;

    public AliasedEnchantment(Rarity weight, int maxLevel, EquipmentSlot[] slotTypes) {
        super(weight, EnchantmentTarget.VANISHABLE, slotTypes);
        this.maxLevel = maxLevel;
    }

    @Override
    public int getMaxLevel() {
        return maxLevel;
    }

    @Override
    public abstract boolean isAcceptableItem(ItemStack stack);
}
