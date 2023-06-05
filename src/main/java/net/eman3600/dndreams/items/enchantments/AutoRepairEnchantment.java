package net.eman3600.dndreams.items.enchantments;

import net.eman3600.dndreams.util.ModTags;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public class AutoRepairEnchantment extends AliasedEnchantment {

    public AutoRepairEnchantment(Rarity weight, int maxLevel, EquipmentSlot[] slotTypes) {
        super(weight, maxLevel, slotTypes);
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.isIn(ModTags.AUTO_REPAIRING_TOOLS);
    }
}
