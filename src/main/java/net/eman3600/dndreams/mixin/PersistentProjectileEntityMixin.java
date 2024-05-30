package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.entities.projectiles.GravityProjectileEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin extends ProjectileEntity {

    public PersistentProjectileEntityMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;hasNoGravity()Z"))
    private boolean dndreams$tick$modifyGravity(PersistentProjectileEntity instance) {

        if (instance instanceof GravityProjectileEntity e && !instance.hasNoGravity()) {
            Vec3d velocity = instance.getVelocity();
            instance.setVelocity(velocity.x, velocity.y - e.getGravity(), velocity.z);

            return true;
        }

        return instance.hasNoGravity();
    }
}
