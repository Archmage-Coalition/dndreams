package net.eman3600.dndreams.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.task.SonicBoomTask;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SonicBoomTask.class)
public abstract class SonicBoomDisabler {
    @Inject(method = "shouldRun(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LivingEntity;)Z", at = @At("HEAD"), cancellable = true)
    public void dndreams$disasbleSonicBoom(ServerWorld world, LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }

}
