package net.eman3600.dndreams.items.magic_bow;

import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.eman3600.dndreams.initializers.event.ModParticles;
import net.eman3600.dndreams.items.TooltipItem;
import net.eman3600.dndreams.items.interfaces.AirSwingItem;
import net.eman3600.dndreams.items.interfaces.BloodlustItem;
import net.eman3600.dndreams.networking.packet_s2c.BloodyLaserPacket;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class BloodyCarbineItem extends MagicCrossbowItem {

    public static int RANGE = 25;

    public BloodyCarbineItem(Settings settings) {
        super(settings);
    }

    @Override
    public int pullTime(ItemStack stack) {
        return 30;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        ItemStack stack = user.getStackInHand(hand);

        if (isCharged(stack)) {

            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.NEUTRAL, 0.7f, 1.3f);

            if (!world.isClient) {

                Vec3d vec = user.getEyePos();

                if (!user.isCreative()) stack.damage(1, user, e -> e.sendToolBreakStatus(hand));

                BloodyLaserPacket.send((ServerWorld) world, vec, user.getPitch(), user.getYaw());
                Vec3d angle = AirSwingItem.rayZVector(user.getYaw(), user.getPitch());

                for (int i = 0; i < BloodyCarbineItem.RANGE * 2; i++) {

                    vec = vec.add(angle);

                    Box box = Box.of(vec, 1, 1, 1);
                    Box box2 = Box.of(vec, .5, .5, .5);

                    List<LivingEntity> entities = world.getEntitiesByClass(LivingEntity.class, box, (e) -> e != user);
                    for (LivingEntity entity : entities) {
                        entity.addStatusEffect(new StatusEffectInstance(ModStatusEffects.HEARTBLEED, 200));
                        entity.damage(BloodlustItem.hemorrhage(user), 8f);
                        entity.takeKnockback(0.4f, MathHelper.sin(user.getYaw() * ((float) Math.PI / 180)), -MathHelper.cos(user.getYaw() * ((float) Math.PI / 180)));
                    }

                    if (world.getBlockCollisions(null, box2).iterator().hasNext()) {
                        break;
                    }
                }
            }

            setCharged(stack, false);
            return TypedActionResult.consume(stack);
        }

        user.setCurrentHand(hand);
        return TypedActionResult.consume(stack);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        int i = this.getMaxUseTime(stack) - remainingUseTicks;
        float f = getPullProgress(i, stack);
        if (f >= 1.0f && !isCharged(stack)) {
            user.timeUntilRegen = 5;
            user.damage(BloodlustItem.CRIMSON_SACRIFICE, 4);

            setCharged(stack, true);
            SoundCategory soundCategory = user instanceof PlayerEntity ? SoundCategory.PLAYERS : SoundCategory.HOSTILE;
            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.BLOCK_BEACON_POWER_SELECT, soundCategory, 1.0f, (world.getRandom().nextFloat() * 0.5f + 1.0f) + 0.2f);
        }
    }
}
