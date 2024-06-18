package net.eman3600.dndreams.items.consumable;

import net.eman3600.dndreams.entities.projectiles.GlowBoltEntity;
import net.eman3600.dndreams.items.TooltipItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class LightPowderItem extends TooltipItem {
    public LightPowderItem(Settings settings) {
        super(settings);
    }


    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.7f, 1.3f);

        user.getItemCooldownManager().set(this, 10);
        if (!user.isCreative()) stack.decrement(1);

        if (!world.isClient) {
            GlowBoltEntity bolt = new GlowBoltEntity(user, world);
            bolt.setVelocity(user, Math.max(user.getPitch() - 7.5f, -90f), user.getYaw(), 0.0f, 1.4f, 0.3f);
            world.spawnEntity(bolt);
        }

        return TypedActionResult.success(stack, world.isClient());
    }
}
