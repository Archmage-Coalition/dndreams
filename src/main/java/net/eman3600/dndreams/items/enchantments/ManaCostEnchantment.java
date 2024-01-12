package net.eman3600.dndreams.items.enchantments;

import net.eman3600.dndreams.items.interfaces.ManaCostItem;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public class ManaCostEnchantment extends AliasedEnchantment {
    public ManaCostEnchantment(Rarity weight, int maxLevel, EquipmentSlot[] slotTypes) {
        super(weight, maxLevel, slotTypes);
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof ManaCostItem;
    }

    @Override
    public int getMinPower(int level) {
        return 16;
    }

    @Override
    public int getMaxPower(int level) {
        return 70;
    }
}
