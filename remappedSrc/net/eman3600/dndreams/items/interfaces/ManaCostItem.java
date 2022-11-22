package net.eman3600.dndreams.items.interfaces;

import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.initializers.basics.ModEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Vanishable;
import net.minecraft.text.Text;

public interface ManaCostItem extends Vanishable {
    int getBaseManaCost();

    default int getManaCost(ItemStack stack) {
        return Math.max(1, getBaseManaCost() - EnchantmentHelper.getLevel(ModEnchantments.THRIFTY, stack));
    }

    default boolean canAffordMana(PlayerEntity player, ItemStack stack) {
        if (player != null)
            return EntityComponents.MANA.get(player).canAfford(getManaCost(stack));
        return false;
    }
    default Text getTooltipMana(ItemStack stack) {
        return Text.translatable("tooltip.dndreams.mana_cost", "§d" + getManaCost(stack));
    }
    default void spendMana(PlayerEntity player, ItemStack stack) {
        if (canAffordMana(player, stack))
            EntityComponents.MANA.get(player).useMana(getManaCost(stack));
    }
}
