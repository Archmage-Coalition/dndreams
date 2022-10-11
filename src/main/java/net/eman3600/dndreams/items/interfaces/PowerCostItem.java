package net.eman3600.dndreams.items.interfaces;

import net.eman3600.dndreams.initializers.EntityComponents;
import net.eman3600.dndreams.mixin_interfaces.ClientWorldMixinI;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Vanishable;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public interface PowerCostItem extends Vanishable {
    float getBasePowerCost();

    default float getPowerCost(ItemStack stack) {
        return getBasePowerCost();
    }

    default boolean canAffordPower(PlayerEntity player, ItemStack stack) {
        if (player != null)
            return EntityComponents.INFUSION.get(player).getPower() >= getPowerCost(stack);
        return false;
    }

    default Text getTooltipPower(World world, ItemStack stack) {
        try {
            if (world instanceof ClientWorldMixinI access && !EntityComponents.INFUSION.get(access.getClient().player).infused()) {
                return Text.translatable("tooltip.dndreams.power_required");
            }
        } catch (ClassCastException | NullPointerException ignored) {}
        return Text.translatable("tooltip.dndreams.power_cost", "§d" + getPowerCost(stack));
    }

    default void spendPower(PlayerEntity player, ItemStack stack) {
        if (canAffordPower(player, stack))
            EntityComponents.INFUSION.get(player).usePower(getPowerCost(stack));
    }
}
