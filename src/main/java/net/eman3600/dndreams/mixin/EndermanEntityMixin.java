package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.initializers.cca.WorldComponents;
import net.eman3600.dndreams.initializers.world.ModDimensions;
import net.eman3600.dndreams.mixin_interfaces.ClientWorldAccess;
import net.eman3600.dndreams.mixin_interfaces.EndermanEntityAccess;
import net.eman3600.dndreams.mixin_interfaces.HudAccess;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(EndermanEntity.class)
public abstract class EndermanEntityMixin extends HostileEntity implements Angerable, EndermanEntityAccess {

    @Shadow @Nullable public abstract UUID getAngryAt();

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

    @Inject(method = "playAngrySound", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FFZ)V"))
    private void dndreams$playAngrySound(CallbackInfo ci) {

        if (world instanceof ClientWorldAccess access && isPlayerStaring(access.getPlayer()) && world.getScoreboard() != null && !WorldComponents.BOSS_STATE.get(world.getScoreboard()).dragonSlain()) {

            ((HudAccess)access.getClient().inGameHud).setDragonFlash(24);
        }
    }

    @ModifyArg(method = "initGoals", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/goal/GoalSelector;add(ILnet/minecraft/entity/ai/goal/Goal;)V", ordinal = 9))
    private Goal dndreams$createDifferentRevengeGoal(Goal instance) {
        return new RevengeGoal((EndermanEntity) (Object) this, EnderDragonEntity.class);
    }
}
