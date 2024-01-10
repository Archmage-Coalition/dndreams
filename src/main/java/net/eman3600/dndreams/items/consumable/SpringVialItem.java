package net.eman3600.dndreams.items.consumable;

import net.eman3600.dndreams.entities.projectiles.AbstractBrewEntity;
import net.eman3600.dndreams.entities.projectiles.SpringVialEntity;
import net.eman3600.dndreams.initializers.basics.ModFluids;
import net.eman3600.dndreams.items.TooltipItem;
import net.eman3600.dndreams.items.consumable.brew.AbstractBrewItem;
import net.eman3600.dndreams.util.Function2;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class SpringVialItem extends TooltipItem {

    public final Fluid fluid;
    public final int color;

    public SpringVialItem(Fluid fluid, int color, Settings settings) {
        super(settings);
        this.fluid = fluid;
        this.color = color;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_SPLASH_POTION_THROW, SoundCategory.PLAYERS, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
        ItemStack stack = user.getStackInHand(hand);
        if (!world.isClient) {
            SpringVialEntity entity = new SpringVialEntity(user, world);
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
