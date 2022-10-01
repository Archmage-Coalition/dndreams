package net.eman3600.dndreams.initializers;

import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.blocks.*;
import net.eman3600.dndreams.blocks.crop.SnowbellBlock;
import net.eman3600.dndreams.blocks.crop.WaterArtichokeBlock;
import net.eman3600.dndreams.world.feature.tree.DreamwoodSaplingGenerator;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;

public class ModBlocks {

    // Hellsteel Block
    public static final Block CORRUPT_BLOCK = registerBlock("corrupt_block",
            new Block(FabricBlockSettings.of(Material.METAL, MapColor.BRIGHT_RED).strength(4.0f).resistance(5.0f)
                    .sounds(BlockSoundGroup.METAL)
                    .requiresTool()),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS).fireproof());

    // Hellslate Ores
    public static final Block HELLSLATE = registerBlock("hellslate",
            new OreBlock(FabricBlockSettings.of(Material.STONE, MapColor.BRIGHT_RED).strength(4.0f).resistance(5.0f)
                    .sounds(BlockSoundGroup.NETHERRACK)
                    .requiresTool(), UniformIntProvider.create(2, 5)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS).fireproof());

    public static final Block HELLSAND = registerBlock("hellsand",
            new Hellsand(FabricBlockSettings.of(Material.AGGREGATE, MapColor.BRIGHT_RED).strength(2.0f).resistance(5.0f)
                    .sounds(BlockSoundGroup.SOUL_SAND)
                    .requiresTool(), UniformIntProvider.create(2, 5)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS).fireproof());

    public static final Block HELLSOIL = registerBlock("hellsoil",
            new OreBlock(FabricBlockSettings.of(Material.SOIL, MapColor.BRIGHT_RED).strength(2.0f).resistance(5.0f)
                    .sounds(BlockSoundGroup.SOUL_SOIL)
                    .requiresTool(), UniformIntProvider.create(2, 5)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS).fireproof());

    // Celestium Block
    public static final Block CELESTIUM_BLOCK = registerBlock("celestium_block",
            new Block(FabricBlockSettings.of(Material.METAL, MapColor.PALE_YELLOW).strength(4.0f).resistance(9.0f)
                    .sounds(BlockSoundGroup.METAL)
                    .requiresTool()),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Celestium Ore
    public static final Block CELESTIUM_ORE = registerBlock("celestium_ore",
            new OreBlock(FabricBlockSettings.of(Material.STONE, MapColor.WHITE_GRAY).strength(4.0f).resistance(8.0f)
                    .sounds(BlockSoundGroup.STONE)
                    .requiresTool()),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Managold Block
    public static final Block MANAGOLD_BLOCK = registerBlock("managold_block",
            new Block(FabricBlockSettings.of(Material.METAL, MapColor.GOLD).strength(3.0f).resistance(6.0f)
                    .sounds(BlockSoundGroup.METAL)
                    .requiresTool()),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Tormite Block
    public static final Block TORMITE_BLOCK = registerBlock("tormite_block",
            new Block(FabricBlockSettings.of(Material.METAL, MapColor.GOLD).strength(3.0f).resistance(6.0f)
                    .sounds(BlockSoundGroup.METAL)
                    .requiresTool()),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS).fireproof());

    // World Fountain
    public static final Block WORLD_FOUNTAIN = registerBlock("world_fountain",
            new WorldFountain(FabricBlockSettings.of(Material.STONE).strength(-1.0F, 3600000.0F).dropsNothing().allowsSpawning(ModBlocks::never)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block FLOWING_BEDROCK = registerBlock("flowing_bedrock",
            new FlowingBedrock(FabricBlockSettings.of(Material.STONE).strength(-1.0F, 3600000.0F).dropsNothing().allowsSpawning(ModBlocks::never)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Shimmering blocks
    public static final Block SHIMMERING_STONE = registerBlock("shimmering_stone",
            new Block(FabricBlockSettings.copy(Blocks.STONE)
                    .requiresTool()),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block SHIMMERING_COBBLESTONE = registerBlock("shimmering_cobblestone",
            new Block(FabricBlockSettings.copy(Blocks.COBBLESTONE)
                    .requiresTool()),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block SHIMMERING_BRICKS = registerBlock("shimmering_bricks",
            new Block(FabricBlockSettings.copy(Blocks.STONE_BRICKS)
                    .requiresTool()),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block SHIMMERING_ICE = registerBlock("shimmering_ice",
            new ShimmeringIceBlock(FabricBlockSettings.copy(Blocks.ICE)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block SHIMMERING_GLASS = registerBlock("shimmering_glass",
            new GlassBlock(FabricBlockSettings.copy(Blocks.GLASS)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Dream Weaver
    public static final Block DREAM_TABLE = registerBlock("dream_table",
            new DreamTable(FabricBlockSettings.of(Material.WOOD).strength(2.5f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));

    // Dream Grass
    public static final Block DREAM_GRASS = registerBlock("dream_grass",
            new DreamGrassBlock(FabricBlockSettings.copy(Blocks.GRASS_BLOCK)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Dreamwood Logs
    public static final Block DREAMWOOD_LOG = registerBlock("dreamwood_log",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block DREAMWOOD = registerBlock("dreamwood",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block STRIPPED_DREAMWOOD_LOG = registerBlock("stripped_dreamwood_log",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block STRIPPED_DREAMWOOD = registerBlock("stripped_dreamwood",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Dreamwood Planks
    public static final Block DREAMWOOD_PLANKS = registerBlock("dreamwood_planks",
            new Block(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Dreamwood Leaves & Sapling
    public static final Block DREAMWOOD_LEAVES = registerBlock("dreamwood_leaves",
            new LeavesBlock(FabricBlockSettings.copy(Blocks.AZALEA_LEAVES)
                    .nonOpaque()),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));
    public static final Block DREAMWOOD_SAPLING = registerBlock("dreamwood_sapling",
            new DreamSapling(new DreamwoodSaplingGenerator(), FabricBlockSettings.copy(Blocks.OAK_SAPLING)
                    .nonOpaque()),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));

    // Dreamwood Stairs
    public static final Block DREAMWOOD_STAIRS = registerBlock("dreamwood_stairs",
            new ModStairsBlock(DREAMWOOD_PLANKS.getDefaultState(), FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Dreamwood Slab
    public static final Block DREAMWOOD_SLAB = registerBlock("dreamwood_slab",
            new SlabBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Dreamwood Fence
    public static final Block DREAMWOOD_FENCE = registerBlock("dreamwood_fence",
            new FenceBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Dreamwood Fence Gate
    public static final Block DREAMWOOD_FENCE_GATE = registerBlock("dreamwood_fence_gate",
            new FenceGateBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));



    // Snowbell Crop
    public static final Block SNOWBELL_CROP = registerBlock("snowbell_crop",
            new SnowbellBlock(FabricBlockSettings.copy(Blocks.WHEAT).nonOpaque()));

    // Water Artichoke Crop
    public static final Block WATER_ARTICHOKE = registerBlock("water_artichoke",
            new WaterArtichokeBlock(FabricBlockSettings.copy(Blocks.WHEAT).nonOpaque()));



    public static Block registerBlock(String name, Block block, Item.Settings settings) {
        registerBlockItem(name, block, settings);
        return registerBlock(name, block);
    }

    public static Block registerBlock(String name, Block block) {
        return Registry.register(Registry.BLOCK, new Identifier(Initializer.MODID, name), block);
    }

    public static Item registerBlockItem(String name, Block block, Item.Settings settings) {
        return Registry.register(Registry.ITEM, new Identifier(Initializer.MODID, name),
                new BlockItem(block, settings));
    }

    public static void registerBlocks() {
         System.out.println("Registering blocks for " + Initializer.MODID);
    }

    private static Boolean never(BlockState state, BlockView world, BlockPos pos, EntityType<?> type) {
        return false;
    }
}
