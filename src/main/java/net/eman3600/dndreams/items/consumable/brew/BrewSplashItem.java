package net.eman3600.dndreams.items.consumable.brew;

import net.eman3600.dndreams.entities.projectiles.BrewSplashEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class BrewSplashItem extends AbstractBrewItem {
    public BrewSplashItem(Settings settings) {
        super(settings, 0.8f);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!world.isClient) {
            BrewSplashEntity entity = new BrewSplashEntity(user, world);
            entity.setItem(stack);
            entity.setVelocity(user, user.getPitch(), user.getYaw(), -20.0f, 0.5f, 1.0f);
            world.spawnEntity(entity);
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            stack.decrement(1);
        }
        return TypedActionResult.success(stack, world.isClient());
    }
}