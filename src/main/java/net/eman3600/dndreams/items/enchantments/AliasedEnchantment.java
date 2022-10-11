package net.eman3600.dndreams.items.enchantments;

import net.fabricmc.fabric.api.util.BooleanFunction;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.function.ToBooleanBiFunction;

import java.lang.invoke.LambdaMetafactory;

public abstract class AliasedEnchantment extends Enchantment {
    private final int maxLevel;

    protected AliasedEnchantment(Rarity weight, int maxLevel, EquipmentSlot[] slotTypes) {
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
