package net.eman3600.dndreams.mixin;

import net.minecraft.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DrownedEntity.DrownedAttackGoal.class)
public abstract class DrownedAttackGoalMixin extends ZombieAttackGoal {
    @Shadow @Final private DrownedEntity drowned;

    public DrownedAttackGoalMixin(ZombieEntity zombie, double speed, boolean pauseWhenMobIdle) {
        super(zombie, speed, pauseWhenMobIdle);
    }

    @Inject(method = "canStart", at = @At("HEAD"), cancellable = true)
    private void dndreams$canStart(CallbackInfoReturnable<Boolean> cir) {
        if (this.drowned.getMainHandStack().isOf(Items.TRIDENT)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "shouldContinue", at = @At("HEAD"), cancellable = true)
    private void dndreams$shouldContinue(CallbackInfoReturnable<Boolean> cir) {
        if (this.drowned.getMainHandStack().isOf(Items.TRIDENT)) {
            cir.setReturnValue(false);
        }
    }
}
