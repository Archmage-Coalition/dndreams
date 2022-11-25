package net.eman3600.dndreams.items.interfaces;

import net.eman3600.dndreams.cardinal_components.TormentComponent;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Vanishable;
import net.minecraft.text.Text;

public interface SanityCostItem extends Vanishable {
    float getBaseSanityCost();
    boolean isPermanent(ItemStack stack);

    default float getSanityCost(ItemStack stack) {
        return getBaseSanityCost();
    }

    default boolean canAffordSanity(PlayerEntity player, ItemStack stack) {
        if (player != null) {
            TormentComponent torment = EntityComponents.TORMENT.get(player);
            return isPermanent(stack) ?
                    torment.getMaxSanity() - getSanityCost(stack) >= TormentComponent.MIN_MAX_SANITY :
                    torment.getSanity() - getSanityCost(stack) >= TormentComponent.MIN_SANITY;
        }
        return false;
    }
    default Text getTooltipSanity(ItemStack stack) {
        return Text.translatable((isPermanent(stack) ? "tooltip.dndreams.max_sanity_cost" : "tooltip.dndreams.sanity_cost"), "§d" + getSanityCost(stack));
    }
    default void spendSanity(PlayerEntity player, ItemStack stack) {
        if (canAffordSanity(player, stack)) {
            TormentComponent torment = EntityComponents.TORMENT.get(player);
            if (isPermanent(stack)) {
                torment.lowerMaxSanity(getSanityCost(stack));
            } else {
                torment.lowerSanity(getSanityCost(stack));
            }
        }
    }
}
