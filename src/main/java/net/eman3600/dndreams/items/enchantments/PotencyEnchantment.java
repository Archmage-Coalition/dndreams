package net.eman3600.dndreams.items.enchantments;

import net.eman3600.dndreams.items.interfaces.MagicDamageItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public class PotencyEnchantment extends AliasedEnchantment {
    public PotencyEnchantment(Rarity weight, int maxLevel, EquipmentSlot[] slotTypes) {
        super(weight, maxLevel, slotTypes);
    }

    @Override
    protected boolean canAccept(Enchantment other) {
        return other.canCombine(Enchantments.SHARPNESS) && other != Enchantments.SHARPNESS;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof MagicDamageItem;
    }
}
