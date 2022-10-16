package net.eman3600.dndreams.initializers;

import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.blocks.entities.DeepslateCoreBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static net.eman3600.dndreams.Initializer.MODID;

public class ModBlockEntities {
    //public static BlockEntityType<DreamTableEntity> DREAM_TABLE;
    public static BlockEntityType<DeepslateCoreBlockEntity> DEEPSLATE_CORE_ENTITY;

    public static void registerBlockEntities() {
        /*DREAM_TABLE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Initializer.MODID, "dream_table"),
                FabricBlockEntityTypeBuilder.create(DreamTableEntity::new, ModBlocks.DREAM_TABLE).build());*/

    }

    private static BlockEntityType registerEntity(String id, BlockEntityType type) {
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MODID, id),
                type);
    }

    static {
        DEEPSLATE_CORE_ENTITY = registerEntity("deepslate_core",
                FabricBlockEntityTypeBuilder.create(DeepslateCoreBlockEntity::new, ModBlocks.DEEPSLATE_CORE).build());
    }
}
