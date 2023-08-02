package net.eman3600.dndreams.initializers.entity;

import net.eman3600.dndreams.entities.misc.ShadeRiftEntity;
import net.eman3600.dndreams.entities.mobs.*;
import net.eman3600.dndreams.entities.projectiles.*;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;

import static net.eman3600.dndreams.Initializer.MODID;
import static net.eman3600.dndreams.entities.mobs.BloodSkeletonEntity.createBloodSkeletonAttributes;
import static net.eman3600.dndreams.entities.mobs.BloodZombieEntity.createBloodZombieAttributes;
import static net.eman3600.dndreams.entities.mobs.FacelessEntity.createFacelessAttributes;
import static net.eman3600.dndreams.entities.mobs.TormentorEntity.createTormentorAttributes;
import static net.eman3600.dndreams.entities.mobs.WardenRagdollEntity.createWardenRagdollAttributes;

public class ModEntities {
    public static final EntityType<ShadeRiftEntity> SHADE_RIFT = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(MODID, "shade_rift"),
            FabricEntityTypeBuilder.<ShadeRiftEntity>create(SpawnGroup.MISC, ShadeRiftEntity::new)
                    .dimensions(EntityDimensions.fixed(0f, 0f))
                    .build()
    );


    public static final EntityType<CrownedSlashEntity> CROWNED_SLASH = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(MODID, "crowned_slash"),
            FabricEntityTypeBuilder.<CrownedSlashEntity>create(SpawnGroup.MISC, CrownedSlashEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25f, 0.25f))
                    .trackRangeBlocks(4).trackedUpdateRate(10)
                    .disableSummon()
                    .build()
    );

    public static final EntityType<CrownedBeamEntity> CROWNED_BEAM = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(MODID, "crowned_beam"),
            FabricEntityTypeBuilder.<CrownedBeamEntity>create(SpawnGroup.MISC, CrownedBeamEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25f, 0.25f))
                    .trackRangeBlocks(4).trackedUpdateRate(10)
                    .disableSummon()
                    .build()
    );

    public static final EntityType<TeslaSlashEntity> TESLA_SLASH = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(MODID, "tesla_slash"),
            FabricEntityTypeBuilder.<TeslaSlashEntity>create(SpawnGroup.MISC, TeslaSlashEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25f, 0.25f))
                    .trackRangeBlocks(4).trackedUpdateRate(10)
                    .disableSummon()
                    .build()
    );

    public static final EntityType<SparkBoltEntity> SPARK_BOLT = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(MODID, "spark_bolt"),
            FabricEntityTypeBuilder.<SparkBoltEntity>create(SpawnGroup.MISC, SparkBoltEntity::new)
                    .dimensions(EntityDimensions.fixed(0.375f, 0.375f))
                    .trackRangeBlocks(4).trackedUpdateRate(10)
                    .build()
    );

    public static final EntityType<GlowBoltEntity> GLOW_BOLT = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(MODID, "glow_bolt"),
            FabricEntityTypeBuilder.<GlowBoltEntity>create(SpawnGroup.MISC, GlowBoltEntity::new)
                    .dimensions(EntityDimensions.fixed(0.375f, 0.375f))
                    .trackRangeBlocks(4).trackedUpdateRate(10)
                    .build()
    );



    public static final EntityType<BrewSplashEntity> BREW_SPLASH = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(MODID, "brew_splash"),
            FabricEntityTypeBuilder.<BrewSplashEntity>create(SpawnGroup.MISC, BrewSplashEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25f, 0.25f))
                    .trackRangeBlocks(4).trackedUpdateRate(10)
                    .build()
    );

    public static final EntityType<BrewLingeringEntity> BREW_LINGERING = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(MODID, "brew_lingering"),
            FabricEntityTypeBuilder.<BrewLingeringEntity>create(SpawnGroup.MISC, BrewLingeringEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25f, 0.25f))
                    .trackRangeBlocks(4).trackedUpdateRate(10)
                    .build()
    );

    public static final EntityType<BrewGasEntity> BREW_GAS = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(MODID, "brew_gas"),
            FabricEntityTypeBuilder.<BrewGasEntity>create(SpawnGroup.MISC, BrewGasEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25f, 0.25f))
                    .trackRangeBlocks(4).trackedUpdateRate(10)
                    .build()
    );

    public static final EntityType<BrewLiquidEntity> BREW_LIQUID = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(MODID, "brew_liquid"),
            FabricEntityTypeBuilder.<BrewLiquidEntity>create(SpawnGroup.MISC, BrewLiquidEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25f, 0.25f))
                    .trackRangeBlocks(4).trackedUpdateRate(10)
                    .build()
    );



    public static final EntityType<BloodZombieEntity> BLOOD_ZOMBIE = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(MODID, "blood_moon/blood_zombie"),
            FabricEntityTypeBuilder.<BloodZombieEntity>create(SpawnGroup.MONSTER, BloodZombieEntity::new)
                    .dimensions(EntityType.ZOMBIE.getDimensions())
                    .build()
    );

    public static final EntityType<BloodSkeletonEntity> BLOOD_SKELETON = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(MODID, "blood_moon/blood_skeleton"),
            FabricEntityTypeBuilder.<BloodSkeletonEntity>create(SpawnGroup.MONSTER, BloodSkeletonEntity::new)
                    .dimensions(EntityType.STRAY.getDimensions())
                    .build()
    );

    public static final EntityType<TormentorEntity> TORMENTOR = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(MODID, "tormentor"),
            FabricEntityTypeBuilder.<TormentorEntity>create(SpawnGroup.MISC, TormentorEntity::new)
                    .dimensions(new EntityDimensions(0.6f, 1.7f, true))
                    .fireImmune()
                    .build()
    );

    public static final EntityType<FacelessEntity> FACELESS = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(MODID, "faceless"),
            FabricEntityTypeBuilder.<FacelessEntity>create(SpawnGroup.MISC, FacelessEntity::new)
                    .dimensions(new EntityDimensions(0.9f, 2.9f, false))
                    .fireImmune()
                    .build()
    );


    public static final EntityType<WardenRagdollEntity> WARDEN_RAGDOLL = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(MODID, "warden_ragdoll"),
            FabricEntityTypeBuilder.<WardenRagdollEntity>create(SpawnGroup.MISC, WardenRagdollEntity::new)
                    .dimensions(EntityType.WARDEN.getDimensions())
                    .fireImmune()
                    .trackRangeBlocks(16)
                    .build()
    );






    public static void registerEntities() {
        FabricDefaultAttributeRegistry.register(BLOOD_ZOMBIE, createBloodZombieAttributes());
        FabricDefaultAttributeRegistry.register(BLOOD_SKELETON, createBloodSkeletonAttributes());
        FabricDefaultAttributeRegistry.register(WARDEN_RAGDOLL, createWardenRagdollAttributes());
        FabricDefaultAttributeRegistry.register(TORMENTOR, createTormentorAttributes());
        FabricDefaultAttributeRegistry.register(FACELESS, createFacelessAttributes());
    }

    public static void registerSpawns() {

        SpawnRestriction.register(TORMENTOR, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, TormentorEntity::isValidNaturalSpawn);
        SpawnRestriction.register(FACELESS, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, FacelessEntity::isValidNaturalSpawn);
    }
}
