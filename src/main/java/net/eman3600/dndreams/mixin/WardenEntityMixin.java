package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.entities.mobs.FacelessEntity;
import net.eman3600.dndreams.entities.mobs.TormentorEntity;
import net.eman3600.dndreams.mixin_interfaces.WardenEntityAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WardenEntity.class)
public abstract class WardenEntityMixin extends HostileEntity implements WardenEntityAccess {
    protected WardenEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "addDarknessToClosePlayers", at = @At("HEAD"), cancellable = true)
    private static void dndreams$addDarknessToClosePlayers(ServerWorld world, Vec3d pos, Entity entity, int range, CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "isValidTarget", at = @At("HEAD"), cancellable = true)
    private void dndreams$isValidTarget(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof TormentorEntity || entity instanceof FacelessEntity) {
            cir.setReturnValue(false);
        }
    }
}
