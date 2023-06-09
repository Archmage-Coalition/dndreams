package net.eman3600.dndreams.mixin_interfaces;

import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;

public interface SculkShriekerBlockEntityAccess {

    @Nullable
    static Entity findResponsibleEntity(@Nullable Entity entity) {
        ServerPlayerEntity serverPlayerEntity2;
        Entity entity2;
        if (entity != null && EntityComponents.WARDEN.isProvidedBy(entity)) {
            return entity;
        }
        if (entity != null && (entity2 = entity.getPrimaryPassenger()) != null && EntityComponents.WARDEN.isProvidedBy(entity2)) {
            return entity2;
        }
        if (entity instanceof ProjectileEntity && (entity2 = ((ProjectileEntity)entity).getOwner()) != null && EntityComponents.WARDEN.isProvidedBy(entity2)) {
            return entity2;
        }
        if (entity instanceof ItemEntity itemEntity && (entity2 = itemEntity.getEventSource()) != null && EntityComponents.WARDEN.isProvidedBy(entity2)) {
            return entity2;
        }
        return null;
    }
}
