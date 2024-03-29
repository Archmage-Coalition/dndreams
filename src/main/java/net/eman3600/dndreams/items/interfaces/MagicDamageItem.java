package net.eman3600.dndreams.items.interfaces;

import net.eman3600.dndreams.initializers.basics.ModEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Vanishable;
import net.minecraft.text.Text;

public interface MagicDamageItem extends Vanishable {
    float getBaseMagicDamage();

    default float getMagicDamage(ItemStack stack) {

        return getBaseMagicDamage() + EnchantmentHelper.getLevel(ModEnchantments.POTENCY, stack) * .5f;
    }

    default Text getTooltipMagicDamage(ItemStack stack) {
        if (getMagicDamage(stack) % 1 == 0f) {
            return Text.translatable("tooltip.dndreams.magic_damage", "§2" + (int) getMagicDamage(stack));
        } else {
            return Text.translatable("tooltip.dndreams.magic_damage", "§2" + getMagicDamage(stack));
        }
    }
}
