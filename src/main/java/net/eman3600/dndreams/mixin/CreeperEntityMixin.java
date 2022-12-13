package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.mixin_interfaces.CreeperEntityAccess;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CreeperEntity.class)
public abstract class CreeperEntityMixin extends HostileEntity implements CreeperEntityAccess {
    @Shadow @Final private static TrackedData<Boolean> CHARGED;

    protected CreeperEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }


    @Override
    public TrackedData<Boolean> getChargedTracker() {
        return CHARGED;
    }
}
