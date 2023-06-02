package net.eman3600.dndreams.items.staff;

import net.eman3600.dndreams.items.TooltipItem;
import net.eman3600.dndreams.items.interfaces.ManaCostItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SearStaffItem extends TooltipItem implements ManaCostItem {

    public SearStaffItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (canAffordMana(user, stack)) {
            spendMana(user, stack);

            user.getItemCooldownManager().set(this, 8);
            if (!user.isCreative()) stack.damage(1, user, p -> p.sendToolBreakStatus(hand));

            if (!world.isClient) {
                Vec3d userVel = user.getVelocity();
                Vec3d vel = Vec3d.fromPolar(user.getPitch(), user.getYaw()).add(userVel.x, user.isOnGround() ? 0 : userVel.y, userVel.z);
                Vec3d pos = user.getEyePos();


                SmallFireballEntity fireball = new SmallFireballEntity(world, user, vel.x, vel.y, vel.z);
                fireball.setPos(pos.x, pos.y, pos.z);
                fireball.setVelocity(vel.multiply(0.5));

                world.spawnEntity(fireball);
                world.syncWorldEvent(null, WorldEvents.BLAZE_SHOOTS, user.getBlockPos(), 0);
            }

            return TypedActionResult.success(stack, world.isClient());
        }

        return super.use(world, user, hand);
    }

    @Override
    public int getBaseManaCost() {
        return 7;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        tooltip.add(getTooltipMana(stack));
    }

    @Override
    public int getEnchantability() {
        return 15;
    }
}
