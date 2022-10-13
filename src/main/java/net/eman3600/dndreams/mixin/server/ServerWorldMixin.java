package net.eman3600.dndreams.mixin.server;

import net.eman3600.dndreams.cardinal_components.BloodMoonComponent;
import net.eman3600.dndreams.entities.mobs.BloodSkeletonEntity;
import net.eman3600.dndreams.entities.mobs.BloodZombieEntity;
import net.eman3600.dndreams.initializers.WorldComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtTagSizeTracker;
import net.minecraft.nbt.scanner.NbtCollector;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World implements StructureWorldAccess {
    @Shadow protected abstract boolean addEntity(Entity entity);

    protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, RegistryEntry<DimensionType> dimension, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long seed, int maxChainedNeighborUpdates) {
        super(properties, registryRef, dimension, profiler, isClient, debugWorld, seed, maxChainedNeighborUpdates);
    }

    @Inject(method = "spawnEntity", at = @At("HEAD"), cancellable = true)
    private void dndreams$spawnEntity(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        try {
            BloodMoonComponent component = WorldComponents.BLOOD_MOON.get(this);

            if (component.isBloodMoon() && entity instanceof MobEntity mob) {
                cir.setReturnValue(addEntity(convertBloodMoon(mob)));
            }
        } catch (RuntimeException ignored) {}
    }

    @Unique
    private Entity convertBloodMoon(MobEntity entity) {
        ServerWorld world = (ServerWorld)(Object)this;

        MobEntity result = entity;

        if (entity.getType() == EntityType.ZOMBIE) {
            result = new BloodZombieEntity(world);
        } else if (entity.getType() == EntityType.SKELETON) {
            result = new BloodSkeletonEntity(world);
        }

        if (entity != result) {
            result.initialize(world, getLocalDifficulty(entity.getBlockPos()), SpawnReason.NATURAL, null, null);

            result.setPosition(entity.getPos());

            entity.remove(Entity.RemovalReason.DISCARDED);
        }

        return result;
    }
}
