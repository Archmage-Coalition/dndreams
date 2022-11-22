package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.entities.projectiles.CrownedSlashEntity;
import net.eman3600.dndreams.entities.projectiles.ProjectileOverhaulEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin extends Entity {
    public PersistentProjectileEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }


    @Redirect(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/damage/DamageSource;arrow(Lnet/minecraft/entity/projectile/PersistentProjectileEntity;Lnet/minecraft/entity/Entity;)Lnet/minecraft/entity/damage/DamageSource;"))
    private DamageSource dndreams$onEntityHit(PersistentProjectileEntity projectile, Entity attacker) {
        if (projectile instanceof CrownedSlashEntity entity) {
            return DamageSource.magic(entity, attacker);
        }
        return DamageSource.arrow(projectile, attacker);
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;setYaw(F)V"))
    private void dndreams$tick(PersistentProjectileEntity instance, float v) {
        if (this instanceof ProjectileOverhaulEntity) {
            return;
        }

        setYaw(v);
    }

}
