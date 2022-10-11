package net.eman3600.dndreams.initializers;

import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.entities.projectiles.CrownedBeamEntity;
import net.eman3600.dndreams.entities.projectiles.CrownedSlashEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModEntities {
    public static final EntityType<CrownedSlashEntity> CROWNED_SLASH_ENTITY_TYPE = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(Initializer.MODID, "crowned_slash"),
            FabricEntityTypeBuilder.<CrownedSlashEntity>create(SpawnGroup.MISC, CrownedSlashEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25f, 0.25f))
                    .trackRangeBlocks(4).trackedUpdateRate(10)
                    .build()
    );

    public static final EntityType<CrownedBeamEntity> CROWNED_BEAM_ENTITY_TYPE = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(Initializer.MODID, "crowned_beam"),
            FabricEntityTypeBuilder.<CrownedBeamEntity>create(SpawnGroup.MISC, CrownedBeamEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25f, 0.25f))
                    .trackRangeBlocks(4).trackedUpdateRate(10)
                    .build()
    );

    public static void registerEntities() {
        System.out.println("Registering entities for " + Initializer.MODID);
    }
}
