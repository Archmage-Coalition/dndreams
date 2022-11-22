package net.eman3600.dndreams.items.consumable;

import net.eman3600.dndreams.util.ModTags;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.EyeOfEnderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class TaintedPearlItem extends Item {
    public TaintedPearlItem(Settings settings) {
        super(settings);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        user.setCurrentHand(hand);
        if (world instanceof ServerWorld serverWorld) {
            BlockPos blockPos = serverWorld.locateStructure(ModTags.TAINTED_PEARL_LOCATED, user.getBlockPos(), 100, false);
            if (blockPos != null) {
                EyeOfEnderEntity taintedPearlEntity = new EyeOfEnderEntity(world, user.getX(), user.getBodyY(0.5D), user.getZ());
                taintedPearlEntity.setItem(itemStack);
                taintedPearlEntity.initTargetPos(blockPos);
                world.emitGameEvent(GameEvent.PROJECTILE_SHOOT, taintedPearlEntity.getPos(), GameEvent.Emitter.of(user));
                world.spawnEntity(taintedPearlEntity);
                if (user instanceof ServerPlayerEntity) {
                    Criteria.USED_ENDER_EYE.trigger((ServerPlayerEntity)user, blockPos);
                }

                world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ENDER_EYE_LAUNCH, SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
                world.syncWorldEvent(null, 1003, user.getBlockPos(), 0);
                if (!user.getAbilities().creativeMode) {
                    itemStack.decrement(1);
                }

                user.incrementStat(Stats.USED.getOrCreateStat(this));
                user.swingHand(hand, true);
                return TypedActionResult.success(itemStack);
            }
        }

        return TypedActionResult.consume(itemStack);
    }
}
