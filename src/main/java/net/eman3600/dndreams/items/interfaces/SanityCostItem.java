package net.eman3600.dndreams.items.interfaces;

import net.eman3600.dndreams.cardinal_components.TormentComponent;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Vanishable;
import net.minecraft.text.Text;

public interface SanityCostItem extends Vanishable {
    float getBaseSanityCost();

    boolean isSanityOptional(ItemStack stack);

    default float getSanityCost(ItemStack stack) {
        return getBaseSanityCost();
    }

    default boolean canAffordSanity(PlayerEntity player, ItemStack stack) {
        if (player != null) {
            TormentComponent torment = EntityComponents.TORMENT.get(player);
            return isSanityOptional(stack) || torment.canAfford(getSanityCost(stack));
        }
        return false;
    }

    default void spendSanity(PlayerEntity player, ItemStack stack) {
        if (canAffordSanity(player, stack)) {
            TormentComponent torment = EntityComponents.TORMENT.get(player);
            torment.spendSanity(getSanityCost(stack));
        }
    }

    default Text getTooltipSanity(ItemStack stack) {
        return Text.translatable("tooltip.dndreams.sanity_cost", "§d" + getSanityCost(stack));
    }
}
