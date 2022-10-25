package net.eman3600.dndreams.initializers;

import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.blocks.*;
import net.eman3600.dndreams.blocks.energy.AttunementChamberBlock;
import net.eman3600.dndreams.blocks.energy.CosmicFountainBlock;
import net.eman3600.dndreams.blocks.energy.CosmicFountainPoleBlock;
import net.eman3600.dndreams.blocks.energy.CosmicPortalBlock;
import net.eman3600.dndreams.blocks.crop.DreamSaplingBlock;
import net.eman3600.dndreams.blocks.crop.SnowbellBlock;
import net.eman3600.dndreams.blocks.crop.WaterArtichokeBlock;
import net.eman3600.dndreams.blocks.portal.GenericPortalBlock;
import net.eman3600.dndreams.items.block_item.DreamyBlockItem;
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
            new HellsandBlock(FabricBlockSettings.of(Material.AGGREGATE, MapColor.BRIGHT_RED).strength(2.0f).resistance(5.0f)
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
            new WorldFountainBlock(FabricBlockSettings.of(Material.STONE).strength(-1.0F, 3600000.0F).dropsNothing().allowsSpawning(ModBlocks::never)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block FLOWING_BEDROCK = registerBlock("flowing_bedrock",
            new FlowingBedrockBlock(FabricBlockSettings.of(Material.STONE).strength(-1.0F, 3600000.0F).dropsNothing().allowsSpawning(ModBlocks::never)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Shimmering blocks
    public static final Block SHIMMERING_STONE = registerBlock("shimmering_stone",
            new Block(FabricBlockSettings.copy(Blocks.STONE)
                    .requiresTool().luminance(state -> 7)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block SHIMMERING_COBBLESTONE = registerBlock("shimmering_cobblestone",
            new Block(FabricBlockSettings.copy(Blocks.COBBLESTONE)
                    .requiresTool().luminance(state -> 7)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block SHIMMERING_BRICKS = registerBlock("shimmering_bricks",
            new Block(FabricBlockSettings.copy(Blocks.STONE_BRICKS)
                    .requiresTool().luminance(state -> 7)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block SHIMMERING_ICE = registerBlock("shimmering_ice",
            new ShimmeringIceBlock(FabricBlockSettings.copy(Blocks.ICE).luminance(state -> 7)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block SHIMMERING_GLASS = registerBlock("shimmering_glass",
            new GlassBlock(FabricBlockSettings.copy(Blocks.GLASS).luminance(state -> 7)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block SHIMMERING_GLASS_PANE = registerBlock("shimmering_glass_pane",
            new PaneBlock(FabricBlockSettings.copy(Blocks.GLASS_PANE).luminance(state -> 7)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Dream Weaver
    public static final Block DREAM_TABLE = registerDreamBlock("dream_table",
            new DreamTableBlock(FabricBlockSettings.of(Material.WOOD).strength(2.5f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));

    // Dreamwood Logs
    public static final Block DREAMWOOD_LOG = registerDreamBlock("dreamwood_log",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOL)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block DREAMWOOD = registerDreamBlock("dreamwood",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOL)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block STRIPPED_DREAMWOOD_LOG = registerDreamBlock("stripped_dreamwood_log",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOL)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block STRIPPED_DREAMWOOD = registerDreamBlock("stripped_dreamwood",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOL)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Dreamwood Planks
    public static final Block DREAMWOOD_PLANKS = registerDreamBlock("dreamwood_planks",
            new Block(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOL)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Dreamwood Leaves & Sapling
    public static final Block DREAMWOOD_LEAVES = registerDreamBlock("dreamwood_leaves",
            new LeavesBlock(FabricBlockSettings.copy(Blocks.AZALEA_LEAVES)
                    .nonOpaque()),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));
    public static final Block DREAMWOOD_SAPLING = registerDreamBlock("dreamwood_sapling",
            new DreamSaplingBlock(new DreamwoodSaplingGenerator(), FabricBlockSettings.copy(Blocks.OAK_SAPLING)
                    .nonOpaque()),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));

    // Dreamwood Stairs
    public static final Block DREAMWOOD_STAIRS = registerDreamBlock("dreamwood_stairs",
            new ModStairsBlock(DREAMWOOD_PLANKS.getDefaultState(), FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOL)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Dreamwood Slab
    public static final Block DREAMWOOD_SLAB = registerDreamBlock("dreamwood_slab",
            new SlabBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOL)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Dreamwood Fence
    public static final Block DREAMWOOD_FENCE = registerDreamBlock("dreamwood_fence",
            new FenceBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOL)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Dreamwood Fence Gate
    public static final Block DREAMWOOD_FENCE_GATE = registerDreamBlock("dreamwood_fence_gate",
            new FenceGateBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOL)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));







    // Sakura Wood Logs
    public static final Block SAKURA_LOG = registerBlock("sakura_log",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block SAKURA_WOOD = registerBlock("sakura_wood",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block STRIPPED_SAKURA_LOG = registerBlock("stripped_sakura_log",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block STRIPPED_SAKURA_WOOD = registerBlock("stripped_sakura_wood",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Sakura Wood Planks
    public static final Block SAKURA_PLANKS = registerBlock("sakura_planks",
            new Block(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Sakura Wood Leaves & Sapling
    public static final Block SAKURA_LEAVES = registerBlock("sakura_leaves",
            new LeavesBlock(FabricBlockSettings.copy(Blocks.AZALEA_LEAVES)
                    .nonOpaque()),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));
    public static final Block SAKURA_SAPLING = registerBlock("sakura_sapling",
            new DreamSaplingBlock(new DreamwoodSaplingGenerator(), FabricBlockSettings.copy(Blocks.OAK_SAPLING)
                    .nonOpaque()),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));

    // Sakura Wood Stairs
    public static final Block SAKURA_STAIRS = registerBlock("sakura_stairs",
            new ModStairsBlock(DREAMWOOD_PLANKS.getDefaultState(), FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Sakura Wood Slab
    public static final Block SAKURA_SLAB = registerBlock("sakura_slab",
            new SlabBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Sakura Wood Fence
    public static final Block SAKURA_FENCE = registerBlock("sakura_fence",
            new FenceBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Sakura Wood Fence Gate
    public static final Block SAKURA_FENCE_GATE = registerBlock("sakura_fence_gate",
            new FenceGateBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));






    // Snowbell Crop
    public static final Block SNOWBELL_CROP = registerBlock("snowbell_crop",
            new SnowbellBlock(FabricBlockSettings.copy(Blocks.WHEAT).nonOpaque()));

    // Water Artichoke Crop
    public static final Block WATER_ARTICHOKE = registerBlock("water_artichoke",
            new WaterArtichokeBlock(FabricBlockSettings.copy(Blocks.WHEAT).nonOpaque()));


    // Ancient Portal
    public static final Block CHARGED_DEEPSLATE = registerBlock("charged_deepslate",
            new ChargedDeepslateBlock(FabricBlockSettings.of(Material.STONE).strength(-1.0F, 3600000.0F).dropsNothing().allowsSpawning(ModBlocks::never)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Ancient Portal
    public static final Block DEEPSLATE_CORE = registerBlock("deepslate_core",
            new DeepslateCoreBlock(FabricBlockSettings.of(Material.STONE)
                    .strength(-1.0F, 3600000.0F).dropsNothing().allowsSpawning(ModBlocks::never)
                    .nonOpaque()));

    public static final Block WEAK_PORTAL = registerBlock("weak_portal",
            new GenericPortalBlock(FabricBlockSettings.copy(Blocks.NETHER_PORTAL).nonOpaque(), CHARGED_DEEPSLATE));

    public static final Block HORDE_PORTAL = registerBlock("horde_portal",
            new GenericPortalBlock(FabricBlockSettings.copy(Blocks.NETHER_PORTAL).nonOpaque(), CHARGED_DEEPSLATE));

    public static final Block OVERHELL_PORTAL = registerBlock("overhell_portal",
            new GenericPortalBlock(FabricBlockSettings.copy(Blocks.NETHER_PORTAL).nonOpaque(), CHARGED_DEEPSLATE));

    public static final Block MORTAL_PORTAL = registerBlock("mortal_portal",
            new GenericPortalBlock(FabricBlockSettings.copy(Blocks.NETHER_PORTAL).nonOpaque(), CHARGED_DEEPSLATE));



    // Rituals
    public static final Block COSMIC_FOUNTAIN = registerBlock("cosmic_fountain",
            new CosmicFountainBlock(FabricBlockSettings.copy(Blocks.END_STONE)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    public static final Block COSMIC_FOUNTAIN_POLE = registerBlock("cosmic_fountain_pole",
            new CosmicFountainPoleBlock(FabricBlockSettings.copy(Blocks.END_STONE)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    public static final Block COSMIC_PORTAL = registerBlock("cosmic_portal",
            new CosmicPortalBlock(FabricBlockSettings.copy(Blocks.END_PORTAL)));


    // Attunement Chamber
    public static final Block ATTUNEMENT_CHAMBER = registerBlock("attunement_chamber",
            new AttunementChamberBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(3.0f, 6.0f)),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));






    public static Block registerBlock(String name, Block block, Item.Settings settings) {
        registerBlockItem(name, block, settings);
        return registerBlock(name, block);
    }

    public static Block registerDreamBlock(String name, Block block, Item.Settings settings) {
        registerDreamBlockItem(name, block, settings);
        return registerBlock(name, block);
    }

    public static Block registerBlock(String name, Block block) {
        return Registry.register(Registry.BLOCK, new Identifier(Initializer.MODID, name), block);
    }

    public static Item registerBlockItem(String name, Block block, Item.Settings settings) {
        return Registry.register(Registry.ITEM, new Identifier(Initializer.MODID, name),
                new BlockItem(block, settings));
    }

    public static Item registerDreamBlockItem(String name, Block block, Item.Settings settings) {
        return Registry.register(Registry.ITEM, new Identifier(Initializer.MODID, name),
                new DreamyBlockItem(block, settings));
    }

    public static void registerBlocks() {
         System.out.println("Registering blocks for " + Initializer.MODID);
    }

    private static Boolean never(BlockState state, BlockView world, BlockPos pos, EntityType<?> type) {
        return false;
    }
}
