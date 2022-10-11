package net.eman3600.dndreams.items.enchantments;

import net.eman3600.dndreams.items.interfaces.MagicDamageItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public class MagicDamageEnchantment extends Enchantment {
    private final int maxLevel;

    public MagicDamageEnchantment(Rarity weight, int maxLevel, EquipmentSlot[] slotTypes) {
        super(weight, EnchantmentTarget.VANISHABLE, slotTypes);
        this.maxLevel = maxLevel;
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof MagicDamageItem;
    }
}
