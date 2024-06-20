package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.phase.AbstractSittingPhase;
import net.minecraft.entity.boss.dragon.phase.SittingFlamingPhase;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SittingFlamingPhase.class)
public abstract class SittingFlamingPhaseMixin extends AbstractSittingPhase {

    @Shadow private @Nullable AreaEffectCloudEntity dragonBreathEntity;

    public SittingFlamingPhaseMixin(EnderDragonEntity enderDragonEntity) {
        super(enderDragonEntity);
    }

    @Inject(method = "serverTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/AreaEffectCloudEntity;setDuration(I)V"))
    private void dndreams$serverTick$addMortality(CallbackInfo ci) {
        this.dragonBreathEntity.addEffect(new StatusEffectInstance(ModStatusEffects.MORTAL, 300));
    }
}
