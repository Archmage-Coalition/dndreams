package net.eman3600.dndreams.items.enchantments;

import net.eman3600.dndreams.items.interfaces.MagicDamageItem;
import net.eman3600.dndreams.items.interfaces.ManaCostItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public class ManaCostEnchantment extends Enchantment {
    private final int maxLevel;

    public ManaCostEnchantment(Rarity weight, int maxLevel, EquipmentSlot[] slotTypes) {
        super(weight, EnchantmentTarget.VANISHABLE, slotTypes);

        this.maxLevel = maxLevel;
    }

    @Override
    public int getMaxLevel() {
        return maxLevel;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof ManaCostItem;
    }
}
