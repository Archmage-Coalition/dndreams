package net.eman3600.dndreams.items.consumable;

import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.items.TooltipItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class DragonfruitItem extends TooltipItem {
    public DragonfruitItem(Settings settings) {
        super(settings);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return super.getMaxUseTime(stack) * 2;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof PlayerEntity player) {
            EntityComponents.REVIVE.get(player).setRecharging(true);
        }
        return super.finishUsing(stack, world, user);
    }
}
