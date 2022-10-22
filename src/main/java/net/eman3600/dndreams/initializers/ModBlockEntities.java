package net.eman3600.dndreams.initializers;

import net.eman3600.dndreams.blocks.entities.*;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static net.eman3600.dndreams.Initializer.MODID;

public class ModBlockEntities {
    public static BlockEntityType<DeepslateCoreBlockEntity> DEEPSLATE_CORE_ENTITY;
    public static BlockEntityType<CosmicFountainBlockEntity> COSMIC_FOUNTAIN_ENTITY;
    public static BlockEntityType<CosmicPortalBlockEntity> COSMIC_PORTAL_ENTITY;
    public static BlockEntityType<CosmicFountainPoleBlockEntity> COSMIC_FOUNTAIN_POLE_ENTITY;

    public static void registerBlockEntities() {


    }

    private static BlockEntityType registerEntity(String id, BlockEntityType type) {
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MODID, id),
                type);
    }

    static {
        DEEPSLATE_CORE_ENTITY = registerEntity("deepslate_core",
                FabricBlockEntityTypeBuilder.create(DeepslateCoreBlockEntity::new, ModBlocks.DEEPSLATE_CORE).build());
        COSMIC_FOUNTAIN_ENTITY = registerEntity("cosmic_fountain",
                FabricBlockEntityTypeBuilder.create(CosmicFountainBlockEntity::new, ModBlocks.COSMIC_FOUNTAIN).build());
        COSMIC_PORTAL_ENTITY = registerEntity("cosmic_portal",
                FabricBlockEntityTypeBuilder.create(CosmicPortalBlockEntity::new, ModBlocks.COSMIC_PORTAL).build());
        COSMIC_FOUNTAIN_POLE_ENTITY = registerEntity("cosmic_fountain_pole",
                FabricBlockEntityTypeBuilder.create(CosmicFountainPoleBlockEntity::new, ModBlocks.COSMIC_FOUNTAIN_POLE).build());
    }
}
