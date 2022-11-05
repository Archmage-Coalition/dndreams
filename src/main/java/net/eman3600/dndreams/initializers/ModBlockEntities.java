package net.eman3600.dndreams.initializers;

import net.eman3600.dndreams.blocks.entities.SmokestackBlockEntity;
import net.eman3600.dndreams.blocks.entities.*;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static net.eman3600.dndreams.Initializer.MODID;

public class ModBlockEntities {
    public static BlockEntityType<DeepslateCoreBlockEntity> DEEPSLATE_CORE_ENTITY;
    public static BlockEntityType<CosmicFountainBlockEntity> COSMIC_FOUNTAIN_ENTITY;
    public static BlockEntityType<CosmicPortalBlockEntity> COSMIC_PORTAL_ENTITY;
    public static BlockEntityType<CosmicFountainPoleBlockEntity> COSMIC_FOUNTAIN_POLE_ENTITY;
    public static BlockEntityType<AttunementChamberBlockEntity> ATTUNEMENT_CHAMBER_ENTITY;
    public static BlockEntityType<SoulCandleBlockEntity> SOUL_CANDLE_ENTITY;
    public static BlockEntityType<EchoCandleBlockEntity> ECHO_CANDLE_ENTITY;
    public static BlockEntityType<SmokestackBlockEntity> SMOKESTACK_ENTITY;
    public static BlockEntityType<RefineryBlockEntity> REFINERY_ENTITY;

    public static void registerBlockEntities() {


    }

    private static <T extends BlockEntity> BlockEntityType<T> registerEntity(String id, BlockEntityType<T> type) {
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
        ATTUNEMENT_CHAMBER_ENTITY = registerEntity("attunement_chamber",
                FabricBlockEntityTypeBuilder.create(AttunementChamberBlockEntity::new, ModBlocks.ATTUNEMENT_CHAMBER).build());
        SOUL_CANDLE_ENTITY = registerEntity("soul_candle",
                FabricBlockEntityTypeBuilder.create(SoulCandleBlockEntity::new, ModBlocks.SOUL_CANDLE).build());
        ECHO_CANDLE_ENTITY = registerEntity("echo_candle",
                FabricBlockEntityTypeBuilder.create(EchoCandleBlockEntity::new, ModBlocks.ECHO_CANDLE).build());
        SMOKESTACK_ENTITY = registerEntity("smokestack",
                FabricBlockEntityTypeBuilder.create(SmokestackBlockEntity::new, ModBlocks.SMOKESTACK).build());
        REFINERY_ENTITY = registerEntity("refinery",
                FabricBlockEntityTypeBuilder.create(RefineryBlockEntity::new, ModBlocks.REFINERY).build());
    }
}
