package net.eman3600.dndreams.mixin;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.ProjectileAttackGoal;
import net.minecraft.entity.mob.DrownedEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ProjectileAttackGoal.class)
public abstract class ProjectileAttackGoalMixin extends Goal {
    @Inject(method = "shouldContinue", at = @At("HEAD"), cancellable = true)
    private void dndreams$shouldContinue$drownedFix(CallbackInfoReturnable<Boolean> cir) {
        if (((Object)this) instanceof DrownedEntity.TridentAttackGoal goal) {
            if (!canStart()) {
                cir.setReturnValue(false);
            }
        }
    }
}
