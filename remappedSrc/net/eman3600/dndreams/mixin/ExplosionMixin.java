package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.util.ImmunityExplosionBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Explosion.class)
public abstract class ExplosionMixin {
    @Shadow @Final private ExplosionBehavior behavior;

    @Redirect(method = "collectBlocksAndDamageEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isImmuneToExplosion()Z"))
    private boolean dndreams$collectBlocksAndDamageEntities(Entity instance) {
        if (this.behavior instanceof ImmunityExplosionBehavior behavior) {
            return true;
        }

        return instance.isImmuneToExplosion();
    }
}
