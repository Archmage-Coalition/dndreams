package net.eman3600.dndreams.items.consumable.brew;

import net.eman3600.dndreams.entities.projectiles.AbstractBrewEntity;
import net.eman3600.dndreams.entities.projectiles.BrewSplashEntity;
import net.eman3600.dndreams.util.Function2;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class BrewThrownItem extends AbstractBrewItem {
    private final Function2<PlayerEntity, World, AbstractBrewEntity> brewEntity;

    public BrewThrownItem(Settings settings, float durationMod, Function2<PlayerEntity, World, AbstractBrewEntity> brewEntity) {
        super(settings, durationMod);
        this.brewEntity = brewEntity;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_SPLASH_POTION_THROW, SoundCategory.PLAYERS, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
        ItemStack stack = user.getStackInHand(hand);
        if (!world.isClient) {
            AbstractBrewEntity entity = brewEntity.apply(user, world);
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
