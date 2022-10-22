package net.eman3600.dndreams.initializers;

import net.eman3600.dndreams.entities.mobs.BloodSkeletonEntity;
import net.eman3600.dndreams.entities.mobs.BloodZombieEntity;
import net.eman3600.dndreams.entities.mobs.WardenRagdollEntity;
import net.eman3600.dndreams.entities.projectiles.CrownedBeamEntity;
import net.eman3600.dndreams.entities.projectiles.CrownedSlashEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static net.eman3600.dndreams.Initializer.MODID;
import static net.eman3600.dndreams.entities.mobs.BloodSkeletonEntity.createBloodSkeletonAttributes;
import static net.eman3600.dndreams.entities.mobs.BloodZombieEntity.createBloodZombieAttributes;
import static net.eman3600.dndreams.entities.mobs.WardenRagdollEntity.createWardenRagdollAttributes;

public class ModEntities {
    public static final EntityType<CrownedSlashEntity> CROWNED_SLASH_ENTITY_TYPE = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(MODID, "crowned_slash"),
            FabricEntityTypeBuilder.<CrownedSlashEntity>create(SpawnGroup.MISC, CrownedSlashEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25f, 0.25f))
                    .trackRangeBlocks(4).trackedUpdateRate(10)
                    .disableSummon()
                    .build()
    );

    public static final EntityType<CrownedBeamEntity> CROWNED_BEAM_ENTITY_TYPE = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(MODID, "crowned_beam"),
            FabricEntityTypeBuilder.<CrownedBeamEntity>create(SpawnGroup.MISC, CrownedBeamEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25f, 0.25f))
                    .trackRangeBlocks(4).trackedUpdateRate(10)
                    .disableSummon()
                    .build()
    );



    public static final EntityType<BloodZombieEntity> BLOOD_ZOMBIE_ENTITY_TYPE = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(MODID, "blood_moon/blood_zombie"),
            FabricEntityTypeBuilder.<BloodZombieEntity>create(SpawnGroup.MONSTER, BloodZombieEntity::new)
                    .dimensions(EntityType.ZOMBIE.getDimensions())
                    .build()
    );

    public static final EntityType<BloodSkeletonEntity> BLOOD_SKELETON_ENTITY_TYPE = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(MODID, "blood_moon/blood_skeleton"),
            FabricEntityTypeBuilder.<BloodSkeletonEntity>create(SpawnGroup.MONSTER, BloodSkeletonEntity::new)
                    .dimensions(EntityType.STRAY.getDimensions())
                    .build()
    );


    public static final EntityType<WardenRagdollEntity> WARDEN_RAGDOLL_ENTITY_TYPE = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(MODID, "warden_ragdoll"),
            FabricEntityTypeBuilder.<WardenRagdollEntity>create(SpawnGroup.MISC, WardenRagdollEntity::new)
                    .dimensions(EntityType.WARDEN.getDimensions())
                    .fireImmune()
                    .trackRangeBlocks(16)
                    .build()
    );






    public static void registerEntities() {
        FabricDefaultAttributeRegistry.register(BLOOD_ZOMBIE_ENTITY_TYPE, createBloodZombieAttributes());
        FabricDefaultAttributeRegistry.register(BLOOD_SKELETON_ENTITY_TYPE, createBloodSkeletonAttributes());
        FabricDefaultAttributeRegistry.register(WARDEN_RAGDOLL_ENTITY_TYPE, createWardenRagdollAttributes());
    }
}
