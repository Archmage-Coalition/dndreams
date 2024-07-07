package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.mixin_interfaces.TridentEntityAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ZombieEntity.class)
public abstract class ZombieEntityMixin extends HostileEntity {

    protected ZombieEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void dndreams$tick$pickupTridentForDrowned(CallbackInfo ci) {
        if ((Object) this instanceof DrownedEntity drowned && isAlive()) {
            Box box = this.hasVehicle() && !this.getVehicle().isRemoved() ? this.getBoundingBox().union(this.getVehicle().getBoundingBox()).expand(1.0, 0.0, 1.0) : this.getBoundingBox().expand(0.5, 0.25, 0.5);

            List<Entity> list = this.world.getOtherEntities(this, box);
            for (Entity collider : list) {
                if (collider instanceof TridentEntityAccess trident) {
                    trident.collideWithDrowned(drowned);
                }
            }
        }
    }
}
