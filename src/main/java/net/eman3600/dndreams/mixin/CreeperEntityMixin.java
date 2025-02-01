package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.entities.mobs.ParryableEntity;
import net.eman3600.dndreams.mixin_interfaces.CreeperEntityAccess;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CreeperEntity.class)
public abstract class CreeperEntityMixin extends HostileEntity implements CreeperEntityAccess, ParryableEntity {
    @Shadow @Final private static TrackedData<Boolean> CHARGED;

    @Shadow private int fuseTime;

    @Shadow private int currentFuseTime;

    @Shadow @Final private static TrackedData<Boolean> IGNITED;

    @Shadow protected abstract void spawnEffectsCloud();

    @Shadow public abstract boolean shouldRenderOverlay();

    @Shadow private int explosionRadius;

    protected CreeperEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }


    @Override
    public TrackedData<Boolean> getChargedTracker() {
        return CHARGED;
    }

    @Override
    public boolean canParry() {
        return this.fuseTime - this.currentFuseTime <= 8;
    }

    @Override
    public float parryInterruptDamage() {
        return 6;
    }

    @Override
    public void onParry(PlayerEntity player) {
        Explosion.DestructionType destructionType = Explosion.DestructionType.NONE;
        float f = this.shouldRenderOverlay() ? 3.0f : 1.5f;
        this.dead = true;
        this.world.createExplosion(player, this.getX(), this.getY(), this.getZ(), (float)this.explosionRadius * f, destructionType);
        this.discard();
        this.spawnEffectsCloud();
    }
}
