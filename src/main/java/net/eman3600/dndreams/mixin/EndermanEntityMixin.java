package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.initializers.ModDimensions;
import net.eman3600.dndreams.initializers.WorldComponents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndermanEntity.class)
public abstract class EndermanEntityMixin extends HostileEntity implements Angerable {

    protected EndermanEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "isPlayerStaring", at = @At("RETURN"), cancellable = true)
    private void playerStareStopper(PlayerEntity player, CallbackInfoReturnable<Boolean> info) {
        if (world.getScoreboard() != null && WorldComponents.BOSS_STATE.get(world.getScoreboard()).dragonSlain()) {
            info.setReturnValue(false);
        }
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void injectConstructor(EntityType<? extends EndermanEntity> entityType, World world, CallbackInfo info) {
        if (world.getDimensionKey() == ModDimensions.DREAM_TYPE_KEY) {
            this.remove(RemovalReason.DISCARDED);
        }
    }

    @Redirect(method = "initGoals", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/goal/GoalSelector;add(ILnet/minecraft/entity/ai/goal/Goal;)V"))
    private void redirectInitGoals(GoalSelector instance, int priority, Goal goal) {
        if (goal instanceof RevengeGoal) {
            instance.add(priority, new RevengeGoal(this, new Class[] {
                    EnderDragonEntity.class
            }));
        } else {
            instance.add(priority, goal);
        }
    }
}
