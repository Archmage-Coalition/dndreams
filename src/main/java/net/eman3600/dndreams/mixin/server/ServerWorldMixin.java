package net.eman3600.dndreams.mixin.server;

import com.google.common.collect.ImmutableList;
import net.eman3600.dndreams.cardinal_components.BloodMoonComponent;
import net.eman3600.dndreams.entities.mobs.BloodSkeletonEntity;
import net.eman3600.dndreams.entities.mobs.BloodZombieEntity;
import net.eman3600.dndreams.entities.spawners.FacelessSpawner;
import net.eman3600.dndreams.entities.spawners.ShadeRiftSpawner;
import net.eman3600.dndreams.entities.spawners.TormentorSpawner;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.initializers.cca.WorldComponents;
import net.eman3600.dndreams.initializers.world.ModDimensions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.level.ServerWorldProperties;
import net.minecraft.world.level.storage.LevelStorage;
import net.minecraft.world.spawner.Spawner;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World implements StructureWorldAccess {
    @Shadow protected abstract boolean addEntity(Entity entity);

    @Mutable
    @Shadow @Final private List<Spawner> spawners;

    @Shadow public abstract List<ServerPlayerEntity> getPlayers();

    @Shadow @Final private List<ServerPlayerEntity> players;

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

    @Inject(method = "<init>", at = @At("TAIL"))
    private void dndreams$init(MinecraftServer server, Executor workerExecutor, LevelStorage.Session session, ServerWorldProperties properties, RegistryKey worldKey, DimensionOptions dimensionOptions, WorldGenerationProgressListener worldGenerationProgressListener, boolean debugWorld, long seed, List spawners, boolean shouldTickTime, CallbackInfo ci) {
        ArrayList<Spawner> tempList = new ArrayList<>();

        for (Object item: spawners) {
            if (item instanceof Spawner spawner) {
                tempList.add(spawner);
            }
        }

        // New Spawners Added Here
        if (getRegistryKey() != ModDimensions.GATEWAY_DIMENSION_KEY) {
            tempList.add(new TormentorSpawner());
            //if (getRegistryKey() != ModDimensions.HAVEN_DIMENSION_KEY) tempList.add(new FacelessSpawner());
            tempList.add(new ShadeRiftSpawner());
        }


        this.spawners = ImmutableList.copyOf(tempList);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;wakeSleepingPlayers()V"))
    private void dndreams$tick$wakeup(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {

        this.players.stream().filter(LivingEntity::isSleeping).toList().forEach(player -> {
            EntityComponents.TORMENT.maybeGet(player).ifPresent(torment -> torment.lowerSanity(-40f));
            player.getHungerManager().addExhaustion(32f);
        });

        /*for (ServerPlayerEntity player: getPlayers()) {
            EntityComponents.TORMENT.maybeGet(player).ifPresent(torment -> torment.lowerSanity(-40f));
        }*/
    }
}
