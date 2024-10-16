package net.eman3600.dndreams.initializers.entity;

import net.eman3600.dndreams.entities.misc.RisingBlockEntity;
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
import static net.eman3600.dndreams.entities.mobs.DreamSheepEntity.createDreamSheepAttributes;
import static net.eman3600.dndreams.entities.mobs.FacelessEntity.createFacelessAttributes;
import static net.eman3600.dndreams.entities.mobs.ShamblerEntity.createShamblerAttributes;
import static net.eman3600.dndreams.entities.mobs.TormentorEntity.createTormentorAttributes;

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

    public static final EntityType<CloudSlashEntity> CLOUD_SLASH = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(MODID, "cloud_slash"),
            FabricEntityTypeBuilder.<CloudSlashEntity>create(SpawnGroup.MISC, CloudSlashEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25f, 0.25f))
                    .trackRangeBlocks(4).trackedUpdateRate(10)
                    .disableSummon()
                    .build()
    );

    public static final EntityType<FallingStarEntity> FALLING_STAR = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(MODID, "falling_star"),
            FabricEntityTypeBuilder.<FallingStarEntity>create(SpawnGroup.MISC, FallingStarEntity::new)
                    .dimensions(EntityDimensions.fixed(0.375f, 0.375f))
                    .trackRangeBlocks(10).trackedUpdateRate(10)
                    .build()
    );

    public static final EntityType<SparkBoltEntity> SPARK_BOLT = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(MODID, "spark_bolt"),
            FabricEntityTypeBuilder.<SparkBoltEntity>create(SpawnGroup.MISC, SparkBoltEntity::new)
                    .dimensions(EntityDimensions.fixed(0.375f, 0.375f))
                    .trackRangeBlocks(10).trackedUpdateRate(10)
                    .build()
    );

    public static final EntityType<GlowBoltEntity> GLOW_BOLT = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(MODID, "glow_bolt"),
            FabricEntityTypeBuilder.<GlowBoltEntity>create(SpawnGroup.MISC, GlowBoltEntity::new)
                    .dimensions(EntityDimensions.fixed(0.375f, 0.375f))
                    .trackRangeBlocks(32).trackedUpdateRate(10)
                    .build()
    );

    public static final EntityType<FlameBoltEntity> FLAME_BOLT = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(MODID, "flame_bolt"),
            FabricEntityTypeBuilder.<FlameBoltEntity>create(SpawnGroup.MISC, FlameBoltEntity::new)
                    .dimensions(EntityDimensions.fixed(0.375f, 0.375f))
                    .trackRangeBlocks(32).trackedUpdateRate(10)
                    .build()
    );

    public static final EntityType<StrifeEntity> STRIFE = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(MODID, "strife"),
            FabricEntityTypeBuilder.<StrifeEntity>create(SpawnGroup.MISC, StrifeEntity::new)
                    .dimensions(EntityDimensions.fixed(0.375f, 0.375f))
                    .trackRangeBlocks(10).trackedUpdateRate(10)
                    .build()
    );



    public static final EntityType<BrewSplashEntity> BREW_SPLASH = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(MODID, "brew_splash"),
            FabricEntityTypeBuilder.<BrewSplashEntity>create(SpawnGroup.MISC, BrewSplashEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25f, 0.25f))
                    .trackRangeBlocks(10).trackedUpdateRate(10)
                    .build()
    );

    public static final EntityType<BrewLingeringEntity> BREW_LINGERING = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(MODID, "brew_lingering"),
            FabricEntityTypeBuilder.<BrewLingeringEntity>create(SpawnGroup.MISC, BrewLingeringEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25f, 0.25f))
                    .trackRangeBlocks(10).trackedUpdateRate(10)
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

    public static final EntityType<SpringVialEntity> SPRING_VIAL = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(MODID, "spring_vial"),
            FabricEntityTypeBuilder.<SpringVialEntity>create(SpawnGroup.MISC, SpringVialEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25f, 0.25f))
                    .trackRangeBlocks(10).trackedUpdateRate(10)
                    .build()
    );

    public static final EntityType<RisingBlockEntity> RISING_BLOCK = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(MODID, "rising_block"),
            FabricEntityTypeBuilder.<RisingBlockEntity>create(SpawnGroup.MISC, RisingBlockEntity::new)
                    .dimensions(EntityDimensions.fixed(0.98f, 0.98f))
                    .trackRangeChunks(10).trackedUpdateRate(20)
                    .build()
    );



    public static final EntityType<BloodSkeletonEntity> BLOOD_SKELETON = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(MODID, "blood_moon/blood_skeleton"),
            FabricEntityTypeBuilder.<BloodSkeletonEntity>create(SpawnGroup.MONSTER, BloodSkeletonEntity::new)
                    .dimensions(EntityType.STRAY.getDimensions())
                    .build()
    );

    public static final EntityType<ShamblerEntity> SHAMBLER = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(MODID, "shambler"),
            FabricEntityTypeBuilder.<ShamblerEntity>create(SpawnGroup.MONSTER, ShamblerEntity::new)
                    .dimensions(EntityType.ZOMBIE.getDimensions())
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

    public static final EntityType<DreamSheepEntity> DREAM_SHEEP = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(MODID, "dream_sheep"),
            FabricEntityTypeBuilder.<DreamSheepEntity>create(SpawnGroup.MISC, DreamSheepEntity::new)
                    .dimensions(EntityType.SHEEP.getDimensions())
                    .trackRangeChunks(10)
                    .build()
    );


    public static final EntityType<ManatwineArrowEntity> MANATWINE_ARROW = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(MODID, "manatwine_arrow"),
            FabricEntityTypeBuilder.<ManatwineArrowEntity>create(SpawnGroup.MISC, ManatwineArrowEntity::new)
                    .dimensions(EntityType.ARROW.getDimensions())
                    .fireImmune()
                    .trackRangeChunks(4)
                    .build()
    );

    public static final EntityType<MindstrungArrowEntity> MINDSTRUNG_ARROW = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(MODID, "mindstrung_arrow"),
            FabricEntityTypeBuilder.<MindstrungArrowEntity>create(SpawnGroup.MISC, MindstrungArrowEntity::new)
                    .dimensions(EntityType.ARROW.getDimensions())
                    .fireImmune()
                    .trackRangeChunks(4)
                    .build()
    );

    public static final EntityType<ManagoldArrowEntity> MANAGOLD_ARROW = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(MODID, "managold_arrow"),
            FabricEntityTypeBuilder.<ManagoldArrowEntity>create(SpawnGroup.MISC, ManagoldArrowEntity::new)
                    .dimensions(EntityType.ARROW.getDimensions())
                    .fireImmune()
                    .trackRangeChunks(4)
                    .build()
    );

    public static final EntityType<SkyboundArrowEntity> SKYBOUND_ARROW = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(MODID, "skybound_arrow"),
            FabricEntityTypeBuilder.<SkyboundArrowEntity>create(SpawnGroup.MISC, SkyboundArrowEntity::new)
                    .dimensions(EntityType.ARROW.getDimensions())
                    .fireImmune()
                    .trackRangeChunks(4)
                    .build()
    );

    public static final EntityType<GhostArrowEntity> GHOST_ARROW = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(MODID, "ghost_arrow"),
            FabricEntityTypeBuilder.<GhostArrowEntity>create(SpawnGroup.MISC, GhostArrowEntity::new)
                    .dimensions(EntityType.ARROW.getDimensions())
                    .fireImmune()
                    .trackRangeChunks(4)
                    .build()
    );

    public static final EntityType<DreadfulArrowEntity> DREADFUL_ARROW = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(MODID, "dreadful_arrow"),
            FabricEntityTypeBuilder.<DreadfulArrowEntity>create(SpawnGroup.MISC, DreadfulArrowEntity::new)
                    .dimensions(EntityType.ARROW.getDimensions()).
                    fireImmune().
                    build()
    );

//    public static final EntityType<DreadfulArrowEntity> DREADFUL_ARROW = Registry.register(
//            Registry.ENTITY_TYPE, new Identifier(MODID, "dreadful_arrow"),
//            FabricEntityTypeBuilder.<SkyboundArrowEntity>create(SpawnGroup.MISC, SkyboundArrowEntity::new)
//                    .dimensions(EntityType.ARROW.getDimensions())
//                    .fireImmune()
//                    .trackRangeChunks(4)
//                    .build()
//    );

    public static final EntityType<StormArrowEntity> STORM_ARROW = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(MODID, "storm_arrow"),
            FabricEntityTypeBuilder.<StormArrowEntity>create(SpawnGroup.MISC, StormArrowEntity::new)
                    .dimensions(EntityType.ARROW.getDimensions())
                    .fireImmune()
                    .trackRangeChunks(4)
                    .build()
    );


    public static final EntityType<VariableLightningEntity> CUSTOM_LIGHTNING = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(MODID, "custom_lightning"),
            FabricEntityTypeBuilder.<VariableLightningEntity>create(SpawnGroup.MISC, VariableLightningEntity::new)
                    .disableSaving()
                    .dimensions(EntityType.LIGHTNING_BOLT.getDimensions())
                    .trackRangeChunks(16)
                    .trackedUpdateRate(Integer.MAX_VALUE)
                    .build()
    );


    public static void registerEntities() {
        FabricDefaultAttributeRegistry.register(BLOOD_SKELETON, createBloodSkeletonAttributes());
        FabricDefaultAttributeRegistry.register(SHAMBLER, createShamblerAttributes());
        FabricDefaultAttributeRegistry.register(TORMENTOR, createTormentorAttributes());
        FabricDefaultAttributeRegistry.register(FACELESS, createFacelessAttributes());
        FabricDefaultAttributeRegistry.register(DREAM_SHEEP, createDreamSheepAttributes());
    }

    public static void registerSpawns() {

        SpawnRestriction.register(TORMENTOR, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, TormentorEntity::isValidNaturalSpawn);
        SpawnRestriction.register(FACELESS, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, FacelessEntity::isValidNaturalSpawn);
    }
}
