package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.DragonFireballEntity;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(DragonFireballEntity.class)
public abstract class DragonFireballEntityMixin extends ExplosiveProjectileEntity {

    protected DragonFireballEntityMixin(EntityType<? extends ExplosiveProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "onCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/AreaEffectCloudEntity;setParticleType(Lnet/minecraft/particle/ParticleEffect;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void dndreams$onCollision$addMortality(HitResult hitResult, CallbackInfo ci, List list, AreaEffectCloudEntity areaEffectCloudEntity, Entity entity) {
        areaEffectCloudEntity.addEffect(new StatusEffectInstance(ModStatusEffects.MORTAL, 300));
    }
}
