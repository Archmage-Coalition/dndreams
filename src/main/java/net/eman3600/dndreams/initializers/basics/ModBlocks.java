package net.eman3600.dndreams.initializers.basics;

import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.blocks.*;
import net.eman3600.dndreams.blocks.crop.*;
import net.eman3600.dndreams.blocks.energy.*;
import net.eman3600.dndreams.blocks.portal.GenericPortalBlock;
import net.eman3600.dndreams.blocks.spirtloggable.SpiritFenceBlock;
import net.eman3600.dndreams.blocks.spirtloggable.SpiritSlabBlock;
import net.eman3600.dndreams.blocks.spirtloggable.SpiritStairsBlock;
import net.eman3600.dndreams.blocks.spreadable.*;
import net.eman3600.dndreams.initializers.event.ModParticles;
import net.eman3600.dndreams.initializers.world.ModConfiguredFeatures;
import net.eman3600.dndreams.items.block_item.DreamyBlockItem;
import net.eman3600.dndreams.util.ModFoodComponents;
import net.eman3600.dndreams.world.feature.tree.sapling_generator.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;

import java.util.function.Function;

public class ModBlocks {

    // Special Blocks
    public static final Block SHINE = registerBlock("shine",
            new ShineBlock(FabricBlockSettings.of(Material.AIR).strength(0, 0)
                    .sounds(BlockSoundGroup.AMETHYST_BLOCK)
                    .nonOpaque()
                    .breakInstantly()
                    .luminance(s -> (s.get(ShineBlock.LIGHT) * 3) + 6)
                    .ticksRandomly()));

    // Marble
    public static final Block MARBLE = registerBlock("marble",
            new Block(FabricBlockSettings.of(Material.STONE, MapColor.TERRACOTTA_WHITE).strength(0.75f)
                    .sounds(BlockSoundGroup.CALCITE)
                    .requiresTool()),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block GOLDEN_GRASS_BLOCK = registerBlock("golden_grass_block",
            new MarbleSpreadableBlock(FabricBlockSettings.copy(MARBLE).ticksRandomly()),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    public static final Block GOLDEN_GRASS = registerBlock("golden_grass",
            new MarbleFernBlock(FabricBlockSettings.copy(Blocks.GRASS)
                    .nonOpaque()),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));

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
                    .requiresTool(), UniformIntProvider.create(3, 7)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS).fireproof());

    public static final Block HELLSAND = registerBlock("hellsand",
            new HellsandBlock(FabricBlockSettings.of(Material.AGGREGATE, MapColor.BRIGHT_RED).strength(2.0f).resistance(5.0f)
                    .sounds(BlockSoundGroup.SOUL_SAND)
                    .requiresTool(), UniformIntProvider.create(3, 7)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS).fireproof());

    public static final Block HELLSOIL = registerBlock("hellsoil",
            new OreBlock(FabricBlockSettings.of(Material.SOIL, MapColor.BRIGHT_RED).strength(2.0f).resistance(5.0f)
                    .sounds(BlockSoundGroup.SOUL_SOIL)
                    .requiresTool(), UniformIntProvider.create(3, 7)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS).fireproof());

    // Vital Ore
    public static final Block VITAL_ORE = registerBlock("vital_ore",
            new VitalOreBlock(FabricBlockSettings.copy(Blocks.DIAMOND_ORE)
                    .requiresTool().ticksRandomly(), UniformIntProvider.create(2, 6)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block DEEPSLATE_VITAL_ORE = registerBlock("deepslate_vital_ore",
            new VitalOreBlock(FabricBlockSettings.copy(Blocks.DEEPSLATE_DIAMOND_ORE)
                    .requiresTool().ticksRandomly(), UniformIntProvider.create(2, 6)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

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
                    .requiresTool(), UniformIntProvider.create(3, 6)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block CELESTIUM_MARBLE_ORE = registerBlock("celestium_marble_ore",
            new OreBlock(FabricBlockSettings.of(Material.STONE, MapColor.WHITE_GRAY).strength(4.0f).resistance(8.0f)
                    .sounds(BlockSoundGroup.STONE)
                    .requiresTool(), UniformIntProvider.create(3, 6)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Some of the Ore Blocks
    public static final Block MANAGOLD_BLOCK = registerBlock("managold_block",
            new Block(FabricBlockSettings.of(Material.METAL, MapColor.GOLD).strength(3.0f).resistance(6.0f)
                    .sounds(BlockSoundGroup.METAL)
                    .requiresTool()),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block TORMITE_BLOCK = registerBlock("tormite_block",
            new Block(FabricBlockSettings.of(Material.METAL, MapColor.PURPLE).strength(5.0f).resistance(6.0f)
                    .sounds(BlockSoundGroup.METAL)
                    .requiresTool()),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS).fireproof());
    public static final Block VITAL_BLOCK = registerBlock("vital_block",
            new Block(FabricBlockSettings.of(Material.METAL, MapColor.PINK).strength(5.0f).resistance(6.0f)
                    .sounds(BlockSoundGroup.METAL)
                    .requiresTool()),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block PERICHARITE_BLOCK = registerBlock("pericharite_block",
            new Block(FabricBlockSettings.of(Material.METAL, MapColor.LIGHT_BLUE).strength(5.0f).resistance(6.0f)
                    .sounds(BlockSoundGroup.METAL)
                    .requiresTool()),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block SILVER_BLOCK = registerBlock("silver_block",
            new Block(FabricBlockSettings.of(Material.METAL, MapColor.OFF_WHITE).strength(5.0f).resistance(6.0f)
                    .sounds(BlockSoundGroup.METAL)
                    .requiresTool()),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // World Fountain
    public static final Block WORLD_FOUNTAIN = registerBlock("world_fountain",
            new WorldFountainBlock(FabricBlockSettings.of(Material.STONE).strength(-1.0F, 3600000.0F).dropsNothing().allowsSpawning(ModBlocks::never)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block FLOWING_BEDROCK = registerBlock("flowing_bedrock",
            new FlowingBedrockBlock(FabricBlockSettings.of(Material.STONE).strength(-1.0F, 3600000.0F).dropsNothing().allowsSpawning(ModBlocks::never)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Bonfire
    public static final Block BONFIRE = registerBlock("bonfire",
            new BonfireBlock(FabricBlockSettings.copyOf(Blocks.CAMPFIRE)));

    // Spirit Slates
    public static final Block SPIRIT_STONE = registerBlock("spirit_stone",
            new Block(FabricBlockSettings.copy(Blocks.STONE)
                    .requiresTool().luminance(state -> 7)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block SPIRIT_STONE_SLAB = registerBlock("spirit_stone_slab",
            new SpiritSlabBlock(FabricBlockSettings.copy(Blocks.STONE_SLAB)
                    .luminance(state -> 7)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block SPIRIT_STONE_STAIRS = registerBlock("spirit_stone_stairs",
            new SpiritStairsBlock(SPIRIT_STONE.getDefaultState(), FabricBlockSettings.copy(Blocks.STONE_STAIRS)
                    .luminance(state -> 7)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block SPIRIT_STONE_WALL = registerBlock("spirit_stone_wall",
            new WallBlock(FabricBlockSettings.copy(Blocks.STONE_BRICK_WALL)
                    .luminance(state -> 7)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    public static final Block SPIRIT_STONE_BRICKS = registerBlock("spirit_stone_bricks",
            new Block(FabricBlockSettings.copy(Blocks.STONE_BRICKS)
                    .luminance(state -> 7)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block CHISELED_SPIRIT_STONE = registerBlock("chiseled_spirit_stone",
            new Block(FabricBlockSettings.copy(Blocks.CHISELED_STONE_BRICKS)
                    .luminance(state -> 7)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block SPIRIT_STONE_BRICK_SLAB = registerBlock("spirit_stone_brick_slab",
            new SpiritSlabBlock(FabricBlockSettings.copy(Blocks.STONE_SLAB)
                    .luminance(state -> 7)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block SPIRIT_STONE_BRICK_STAIRS = registerBlock("spirit_stone_brick_stairs",
            new SpiritStairsBlock(SPIRIT_STONE.getDefaultState(), FabricBlockSettings.copy(Blocks.STONE_STAIRS)
                    .luminance(state -> 7)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block SPIRIT_STONE_BRICK_WALL = registerBlock("spirit_stone_brick_wall",
            new WallBlock(FabricBlockSettings.copy(Blocks.STONE_BRICK_WALL)
                    .luminance(state -> 7)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Spirit Ice
    public static final Block SPIRIT_ICE = registerBlock("spirit_ice",
            new SpiritIceBlock(FabricBlockSettings.copy(Blocks.ICE).luminance(state -> 7)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Sorrow Shards
    public static final Block SORROW_STONE = registerBlock("sorrow_stone",
            new Block(FabricBlockSettings.copy(Blocks.OBSIDIAN)
                    .strength(2.5f)
                    .requiresTool()),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));



    // Sorrow Blocks
    public static final Block SORROW_SLUDGE = registerBlock("sorrow_sludge",
            new Block(FabricBlockSettings.copy(Blocks.MUD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block PACKED_SORROW = registerBlock("packed_sorrow",
            new Block(FabricBlockSettings.copy(Blocks.PACKED_MUD).sounds(BlockSoundGroup.DEEPSLATE_TILES)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Sorrow Bricks
    public static final Block SORROW_BRICKS = registerBlock("sorrow_bricks",
            new Block(FabricBlockSettings.copy(Blocks.MUD_BRICKS).sounds(BlockSoundGroup.DEEPSLATE_TILES)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block SORROW_BRICK_SLAB = registerBlock("sorrow_brick_slab",
            new SpiritSlabBlock(FabricBlockSettings.copy(Blocks.STONE_SLAB).sounds(BlockSoundGroup.DEEPSLATE_TILES)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block SORROW_BRICK_STAIRS = registerBlock("sorrow_brick_stairs",
            new SpiritStairsBlock(SORROW_BRICKS.getDefaultState(), FabricBlockSettings.copy(Blocks.MUD_BRICKS).sounds(BlockSoundGroup.DEEPSLATE_TILES)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block SORROW_BRICK_WALL = registerBlock("sorrow_brick_wall",
            new WallBlock(FabricBlockSettings.copy(Blocks.MUD_BRICKS).sounds(BlockSoundGroup.DEEPSLATE_TILES)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    public static final Block SORROW_TILES = registerBlock("sorrow_tiles",
            new PillarBlock(FabricBlockSettings.copy(Blocks.MUD_BRICKS).sounds(BlockSoundGroup.DEEPSLATE_TILES)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block CHISELED_SORROW_TILES = registerBlock("chiseled_sorrow_tiles",
            new PillarBlock(FabricBlockSettings.copy(Blocks.MUD_BRICKS).sounds(BlockSoundGroup.DEEPSLATE_TILES)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block SORROW_PILLAR = registerBlock("sorrow_pillar",
            new PillarBlock(FabricBlockSettings.copy(Blocks.MUD_BRICKS).sounds(BlockSoundGroup.DEEPSLATE_TILES)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));




    // Crystal Blocks
    public static final Block CRYSTAL_SPIRIT_BLOCK = registerBlock("crystal_spirit_block",
            new CrystalGlassBlock(FabricBlockSettings.copy(Blocks.AMETHYST_BLOCK)
                    .requiresTool().nonOpaque()),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Dream Weaver
    public static final Block DREAM_TABLE = registerDreamBlock("dream_table",
            new DreamTableBlock(FabricBlockSettings.of(Material.WOOD).strength(2.5f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));

    // Dreamwood Logs
    public static final Block DREAMWOOD_LOG = registerDreamBlock("dreamwood_log",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(1.0f)
                    .sounds(BlockSoundGroup.WOOL)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block DREAMWOOD = registerDreamBlock("dreamwood",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(1.0f)
                    .sounds(BlockSoundGroup.WOOL)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block STRIPPED_DREAMWOOD_LOG = registerDreamBlock("stripped_dreamwood_log",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(1.0f)
                    .sounds(BlockSoundGroup.WOOL)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS).food(ModFoodComponents.cakewood(4f)));
    public static final Block STRIPPED_DREAMWOOD = registerDreamBlock("stripped_dreamwood",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(1.0f)
                    .sounds(BlockSoundGroup.WOOL)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS).food(ModFoodComponents.cakewood(5.34f)));

    // Dreamwood Planks
    public static final Block DREAMWOOD_PLANKS = registerDreamBlock("dreamwood_planks",
            new Block(FabricBlockSettings.of(Material.WOOD).strength(1.0f)
                    .sounds(BlockSoundGroup.WOOL)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS).food(ModFoodComponents.cakewood(1)));

    // Dreamwood Leaves & Sapling
    public static final Block DREAMWOOD_LEAVES = registerDreamBlock("dreamwood_leaves",
            new LeavesBlock(FabricBlockSettings.copy(Blocks.AZALEA_LEAVES)
                    .nonOpaque().ticksRandomly().suffocates((state, world, pos) -> false).blockVision((state, world, pos) -> false)),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));
    public static final Block DREAMWOOD_SAPLING = registerDreamBlock("dreamwood_sapling",
            new DreamSaplingBlock(new DreamwoodSaplingGenerator(), FabricBlockSettings.copy(Blocks.OAK_SAPLING)
                    .nonOpaque()),

            new FabricItemSettings().group(ItemGroup.DECORATIONS).food(ModFoodComponents.cakewood(2)));

    // Dreamwood Stairs
    public static final Block DREAMWOOD_STAIRS = registerDreamBlock("dreamwood_stairs",
            new SpiritStairsBlock(DREAMWOOD_PLANKS.getDefaultState(), FabricBlockSettings.of(Material.WOOD).strength(1.0f)
                    .sounds(BlockSoundGroup.WOOL)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS).food(ModFoodComponents.cakewood(1.5f)));

    // Dreamwood Slab
    public static final Block DREAMWOOD_SLAB = registerDreamBlock("dreamwood_slab",
            new SpiritSlabBlock(FabricBlockSettings.of(Material.WOOD).strength(1.0f)
                    .sounds(BlockSoundGroup.WOOL)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS).food(ModFoodComponents.cakewood(0.5f)));

    // Dreamwood Fence
    public static final Block DREAMWOOD_FENCE = registerDreamBlock("dreamwood_fence",
            new SpiritFenceBlock(FabricBlockSettings.of(Material.WOOD).strength(1.0f)
                    .sounds(BlockSoundGroup.WOOL)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS).food(ModFoodComponents.cakewood(1.34f)));

    // Dreamwood Fence Gate
    public static final Block DREAMWOOD_FENCE_GATE = registerDreamBlock("dreamwood_fence_gate",
            new FenceGateBlock(FabricBlockSettings.of(Material.WOOD).strength(1.0f)
                    .sounds(BlockSoundGroup.WOOL)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS).food(ModFoodComponents.cakewood(2)));







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
                    .nonOpaque().ticksRandomly().suffocates((state, world, pos) -> false).blockVision((state, world, pos) -> false)),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));
    public static final Block SAKURA_SAPLING = registerBlock("sakura_sapling",
            new ModSaplingBlock(new SakuraSaplingGenerator(), FabricBlockSettings.copy(Blocks.OAK_SAPLING)
                    .nonOpaque()),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));

    // Sakura Wood Stairs
    public static final Block SAKURA_STAIRS = registerBlock("sakura_stairs",
            new ModStairsBlock(SAKURA_PLANKS.getDefaultState(), FabricBlockSettings.of(Material.WOOD).strength(2.0f)
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



    // Japanese Maple Wood Logs
    public static final Block JAPANESE_MAPLE_LOG = registerBlock("japanese_maple_log",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block JAPANESE_MAPLE_WOOD = registerBlock("japanese_maple_wood",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block STRIPPED_JAPANESE_MAPLE_LOG = registerBlock("stripped_japanese_maple_log",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block STRIPPED_JAPANESE_MAPLE_WOOD = registerBlock("stripped_japanese_maple_wood",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Japanese Maple Planks
    public static final Block JAPANESE_MAPLE_PLANKS = registerBlock("japanese_maple_planks",
            new Block(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Japanese Maple Leaves & Sapling
    public static final Block JAPANESE_MAPLE_LEAVES = registerBlock("japanese_maple_leaves",
            new LeavesBlock(FabricBlockSettings.copy(Blocks.AZALEA_LEAVES)
                    .nonOpaque().ticksRandomly().suffocates((state, world, pos) -> false).blockVision((state, world, pos) -> false)),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));
    public static final Block JAPANESE_MAPLE_SAPLING = registerBlock("japanese_maple_sapling",
            new ModSaplingBlock(new JapaneseMapleSaplingGenerator(), FabricBlockSettings.copy(Blocks.OAK_SAPLING)
                    .nonOpaque()),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));

    // Japanese Maple Stairs
    public static final Block JAPANESE_MAPLE_STAIRS = registerBlock("japanese_maple_stairs",
            new ModStairsBlock(JAPANESE_MAPLE_PLANKS.getDefaultState(), FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Japanese Maple Slab
    public static final Block JAPANESE_MAPLE_SLAB = registerBlock("japanese_maple_slab",
            new SlabBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Japanese Maple Fence
    public static final Block JAPANESE_MAPLE_FENCE = registerBlock("japanese_maple_fence",
            new FenceBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Japanese Maple Fence Gate
    public static final Block JAPANESE_MAPLE_FENCE_GATE = registerBlock("japanese_maple_fence_gate",
            new FenceGateBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));






    // Pristine Wood
    public static final Block PRISTINE_LOG = registerBlock("pristine_log",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block PRISTINE_WOOD = registerBlock("pristine_wood",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block STRIPPED_PRISTINE_LOG = registerBlock("pristine_stripped_log",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block STRIPPED_PRISTINE_WOOD = registerBlock("pristine_stripped_wood",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    public static final Block PRISTINE_PLANKS = registerBlock("pristine_planks",
            new Block(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    public static final Block PRISTINE_LEAVES = registerBlock("pristine_leaves",
            new LeavesBlock(FabricBlockSettings.copy(Blocks.AZALEA_LEAVES)
                    .nonOpaque().ticksRandomly().suffocates((state, world, pos) -> false).blockVision((state, world, pos) -> false)),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));
    public static final Block PRISTINE_SAPLING = registerBlock("pristine_sapling",
            new MarbleSaplingBlock(new PristineSaplingGenerator(), FabricBlockSettings.copy(Blocks.OAK_SAPLING)
                    .nonOpaque()),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));

    public static final Block PRISTINE_STAIRS = registerBlock("pristine_stairs",
            new ModStairsBlock(PRISTINE_PLANKS.getDefaultState(), FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block PRISTINE_SLAB = registerBlock("pristine_slab",
            new SlabBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block PRISTINE_FENCE = registerBlock("pristine_fence",
            new FenceBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block PRISTINE_FENCE_GATE = registerBlock("pristine_fence_gate",
            new FenceGateBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));




    // Nightshade Wood
    public static final Block SHADE_LOG = registerBlock("shade_log",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block SHADE_WOOD = registerBlock("shade_wood",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block STRIPPED_SHADE_LOG = registerBlock("shade_stripped_log",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block STRIPPED_SHADE_WOOD = registerBlock("shade_stripped_wood",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    public static final Block SHADE_PLANKS = registerBlock("shade_planks",
            new Block(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    public static final Block SHADE_LEAVES = registerBlock("shade_leaves",
            new LeavesBlock(FabricBlockSettings.copy(Blocks.AZALEA_LEAVES)
                    .nonOpaque().ticksRandomly().suffocates((state, world, pos) -> false).blockVision((state, world, pos) -> false)),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));
    public static final Block SHADE_BUSH = registerBlock("shade_bush",
            new ShadeSaplingBlock(FabricBlockSettings.copy(Blocks.AZALEA)
                    .breakInstantly().suffocates((state, world, pos) -> false).blockVision((state, world, pos) -> false)),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));

    public static final Block SHADE_STAIRS = registerBlock("shade_stairs",
            new ModStairsBlock(SHADE_PLANKS.getDefaultState(), FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block SHADE_SLAB = registerBlock("shade_slab",
            new SlabBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block SHADE_FENCE = registerBlock("shade_fence",
            new FenceBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block SHADE_FENCE_GATE = registerBlock("shade_fence_gate",
            new FenceGateBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));





    // Haven Wood Logs
    public static final Block HAVEN_LOG = registerBlock("haven_log",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block HAVEN_WOOD = registerBlock("haven_wood",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block HAVEN_STRIPPED_LOG = registerBlock("haven_stripped_log",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block HAVEN_STRIPPED_WOOD = registerBlock("haven_stripped_wood",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Haven Wood Planks
    public static final Block HAVEN_PLANKS = registerBlock("haven_planks",
            new Block(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // haven Wood Leaves & Sapling
    public static final Block HAVEN_LEAVES = registerBlock("haven_leaves",
            new LeavesBlock(FabricBlockSettings.copy(Blocks.AZALEA_LEAVES)
                    .nonOpaque().ticksRandomly().suffocates((state, world, pos) -> false).blockVision((state, world, pos) -> false)),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));
    public static final Block HAVEN_SAPLING = registerBlock("haven_sapling",
            new HavenSaplingBlock(new GenericSaplingGenerator(random -> ModConfiguredFeatures.HAVEN_TREE), FabricBlockSettings.copy(Blocks.OAK_SAPLING)
                    .nonOpaque()),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));

    // Haven Wood Stairs
    public static final Block HAVEN_STAIRS = registerBlock("haven_stairs",
            new ModStairsBlock(HAVEN_PLANKS.getDefaultState(), FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // haven Wood Slab
    public static final Block HAVEN_SLAB = registerBlock("haven_slab",
            new SlabBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // haven Wood Fence
    public static final Block HAVEN_FENCE = registerBlock("haven_fence",
            new FenceBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // haven Wood Fence Gate
    public static final Block HAVEN_FENCE_GATE = registerBlock("haven_fence_gate",
            new FenceGateBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));





    // Pine Wood Logs
    public static final Block PINE_LOG = registerBlock("pine_log",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block PINE_WOOD = registerBlock("pine_wood",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block PINE_STRIPPED_LOG = registerBlock("pine_stripped_log",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block PINE_STRIPPED_WOOD = registerBlock("pine_stripped_wood",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // pine Wood Planks
    public static final Block PINE_PLANKS = registerBlock("pine_planks",
            new Block(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // pine Wood Leaves & Sapling
    public static final Block PINE_LEAVES = registerBlock("pine_leaves",
            new LeavesBlock(FabricBlockSettings.copy(Blocks.AZALEA_LEAVES)
                    .nonOpaque().ticksRandomly().suffocates((state, world, pos) -> false).blockVision((state, world, pos) -> false)),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));
    public static final Block PINE_SAPLING = registerBlock("pine_sapling",
            new HavenSaplingBlock(new GenericSaplingGenerator(random -> ModConfiguredFeatures.PINE_TREE), FabricBlockSettings.copy(Blocks.OAK_SAPLING)
                    .nonOpaque()),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));

    // pine Wood Stairs
    public static final Block PINE_STAIRS = registerBlock("pine_stairs",
            new ModStairsBlock(PINE_PLANKS.getDefaultState(), FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // pine Wood Slab
    public static final Block PINE_SLAB = registerBlock("pine_slab",
            new SlabBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // pine Wood Fence
    public static final Block PINE_FENCE = registerBlock("pine_fence",
            new FenceBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // pine Wood Fence Gate
    public static final Block PINE_FENCE_GATE = registerBlock("pine_fence_gate",
            new FenceGateBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));








    // star Wood Logs
    public static final Block STAR_LOG = registerBlock("star_log",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block STAR_WOOD = registerBlock("star_wood",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block STAR_STRIPPED_LOG = registerBlock("star_stripped_log",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block STAR_STRIPPED_WOOD = registerBlock("star_stripped_wood",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // star Wood Planks
    public static final Block STAR_PLANKS = registerBlock("star_planks",
            new Block(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // star Wood Leaves & Sapling
    public static final Block STAR_LEAVES = registerBlock("star_leaves",
            new LeavesBlock(FabricBlockSettings.copy(Blocks.AZALEA_LEAVES)
                    .nonOpaque().ticksRandomly().suffocates((state, world, pos) -> false).blockVision((state, world, pos) -> false)),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));
    public static final Block STAR_SAPLING = registerBlock("star_sapling",
            new HavenSaplingBlock(new GenericSaplingGenerator(random -> ModConfiguredFeatures.STAR_TREE), FabricBlockSettings.copy(Blocks.OAK_SAPLING)
                    .nonOpaque()),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));

    // star Wood Stairs
    public static final Block STAR_STAIRS = registerBlock("star_stairs",
            new ModStairsBlock(STAR_PLANKS.getDefaultState(), FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // star Wood Slab
    public static final Block STAR_SLAB = registerBlock("star_slab",
            new SlabBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // star Wood Fence
    public static final Block STAR_FENCE = registerBlock("star_fence",
            new FenceBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // star Wood Fence Gate
    public static final Block STAR_FENCE_GATE = registerBlock("star_fence_gate",
            new FenceGateBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));





    public static final Block HAVEN_GRASS_BLOCK = registerBlock("haven_grass_block",
            new HavenSpreadableBlock(FabricBlockSettings.copy(Blocks.GRASS_BLOCK)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    public static final Block HAVEN_GRASS = registerBlock("haven_grass",
            new HavenFernBlock(FabricBlockSettings.copy(Blocks.GRASS)
                    .nonOpaque()),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));


    public static final Block STAR_GRASS_BLOCK = registerBlock("star_grass_block",
            new HavenSpreadableBlock(FabricBlockSettings.copy(Blocks.GRASS_BLOCK)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    public static final Block STAR_GRASS = registerBlock("star_grass",
            new HavenFernBlock(FabricBlockSettings.copy(Blocks.GRASS)
                    .nonOpaque()),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));


    public static final Block HAVEN_DIRT = registerBlock("haven_dirt",
            new Block(FabricBlockSettings.copy(Blocks.DIRT)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));


    public static final Block HAVEN_STONE = registerBlock("haven_stone",
            new Block(FabricBlockSettings.copy(Blocks.STONE)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block HAVEN_STONE_SLAB = registerBlock("haven_stone_slab",
            new SpiritSlabBlock(FabricBlockSettings.copy(Blocks.STONE_SLAB)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block HAVEN_STONE_STAIRS = registerBlock("haven_stone_stairs",
            new SpiritStairsBlock(HAVEN_STONE.getDefaultState(), FabricBlockSettings.copy(Blocks.STONE_STAIRS)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block HAVEN_STONE_WALL = registerBlock("haven_stone_wall",
            new WallBlock(FabricBlockSettings.copy(Blocks.STONE_BRICK_WALL)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block HAVEN_STONE_PRESSURE_PLATE = registerBlock("haven_stone_pressure_plate",
            new PressurePlateBlock(PressurePlateBlock.ActivationRule.MOBS, FabricBlockSettings.copy(Blocks.STONE_PRESSURE_PLATE)),

            new FabricItemSettings().group(ItemGroup.REDSTONE));
    public static final Block HAVEN_STONE_BUTTON = registerBlock("haven_stone_button",
            new StoneButtonBlock(FabricBlockSettings.copy(Blocks.STONE_BUTTON)),

            new FabricItemSettings().group(ItemGroup.REDSTONE));

    public static final Block HAVEN_STONE_BRICKS = registerBlock("haven_stone_bricks",
            new Block(FabricBlockSettings.copy(Blocks.STONE_BRICKS)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block CHISELED_HAVEN_STONE_BRICKS = registerBlock("chiseled_haven_stone_bricks",
            new Block(FabricBlockSettings.copy(Blocks.CHISELED_STONE_BRICKS)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block HAVEN_STONE_BRICK_SLAB = registerBlock("haven_stone_brick_slab",
            new SpiritSlabBlock(FabricBlockSettings.copy(Blocks.STONE_SLAB)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block HAVEN_STONE_BRICK_STAIRS = registerBlock("haven_stone_brick_stairs",
            new SpiritStairsBlock(HAVEN_STONE.getDefaultState(), FabricBlockSettings.copy(Blocks.STONE_STAIRS)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block HAVEN_STONE_BRICK_WALL = registerBlock("haven_stone_brick_wall",
            new WallBlock(FabricBlockSettings.copy(Blocks.STONE_BRICK_WALL)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));


    public static final Block PERICHARITE_ORE = registerBlock("pericharite_ore",
            new OreBlock(FabricBlockSettings.copy(Blocks.DIAMOND_ORE), UniformIntProvider.create(3, 6)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    public static final Block SILVER_ORE = registerBlock("silver_ore",
            new OreBlock(FabricBlockSettings.copy(Blocks.DIAMOND_ORE)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    public static final Block HAVEN_COAL_ORE = registerBlock("haven_coal_ore",
            new OreBlock(FabricBlockSettings.copy(Blocks.COAL_ORE), UniformIntProvider.create(0, 2)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    public static final Block MANAGOLD_ORE = registerBlock("managold_ore",
            new OreBlock(FabricBlockSettings.copy(Blocks.GOLD_ORE)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));


    public static final Block HAVEN_COBBLESTONE = registerBlock("haven_cobblestone",
            new Block(FabricBlockSettings.copy(Blocks.COBBLESTONE)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));





    // Snowbell Crop
    public static final Block SNOWBELL = registerBlock("snowbell",
            new SnowbellBlock(FabricBlockSettings.copy(Blocks.WHEAT).nonOpaque()));

    // Lotus Crop
    public static final Block LOTUS = registerBlock("lotus",
            new LotusBlock(FabricBlockSettings.copy(Blocks.LILY_PAD).ticksRandomly()
                    .suffocates(ModBlocks::never).blockVision(ModBlocks::never)));

    // Ember Moss
    public static final Block EMBER_MOSS = registerBlock("ember_moss",
            new EmberMossBlock(FabricBlockSettings.copy(Blocks.RED_MUSHROOM).nonOpaque().luminance(state -> 15)),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));

    public static final Block FRAZZLED_MOSS = registerBlock("frazzled_moss",
            new FrazzledMossBlock(FabricBlockSettings.copy(Blocks.RED_MUSHROOM).nonOpaque()));

    // Applethorn Crop
    public static final Block APPLETHORN = registerBlock("applethorn",
            new ApplethornBlock(FabricBlockSettings.copy(Blocks.WHEAT).nonOpaque()));

    // Wither Blossom Crop
    public static final Block WITHER_BLOSSOM = registerBlock("wither_blossom",
            new WitherBlossomBlock(FabricBlockSettings.copy(Blocks.SWEET_BERRY_BUSH).nonOpaque()));

    // Dragonfruit Crop
    public static final Block DRAGONFRUIT = registerBlock("dragonfruit",
            new DragonfruitBlock(FabricBlockSettings.copy(Blocks.BEETROOTS).nonOpaque()));


    // Ancient Portal
    public static final Block CHARGED_DEEPSLATE = registerBlock("charged_deepslate",
            new ChargedDeepslateBlock(FabricBlockSettings.of(Material.STONE).strength(-1.0F, 3600000.0F).dropsNothing().allowsSpawning(ModBlocks::never)),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));

    public static final Block DEEPSLATE_CORE = registerBlock("deepslate_core",
            new DeepslateCoreBlock(FabricBlockSettings.of(Material.STONE)
                    .strength(-1.0F, 3600000.0F).dropsNothing().allowsSpawning(ModBlocks::never)
                    .nonOpaque()));


    public static final Block WEAK_PORTAL = registerBlock("weak_portal",
            new GenericPortalBlock(FabricBlockSettings.copy(Blocks.NETHER_PORTAL).nonOpaque(), CHARGED_DEEPSLATE));

    public static final Block HORDE_PORTAL = registerBlock("horde_portal",
            new GenericPortalBlock(FabricBlockSettings.copy(Blocks.NETHER_PORTAL).nonOpaque(), CHARGED_DEEPSLATE));

    public static final Block HAVEN_PORTAL = registerBlock("haven_portal",
            new GenericPortalBlock(FabricBlockSettings.copy(Blocks.NETHER_PORTAL).nonOpaque(), CHARGED_DEEPSLATE));

    public static final Block MORTAL_PORTAL = registerBlock("mortal_portal",
            new GenericPortalBlock(FabricBlockSettings.copy(Blocks.NETHER_PORTAL).nonOpaque(), CHARGED_DEEPSLATE));


    // Nightshade Moss
    public static final Block SHADE_MOSS = registerDreamBlock("shade_moss",
            new ShadeMossBlock(FabricBlockSettings.copyOf(Blocks.MOSS_BLOCK)
                    .strength(4f, 1f)
                    .mapColor(MapColor.PALE_PURPLE)),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));

    public static final Block SHADE_WEED = registerBlock("shade_weed",
            new ShadeWeedBlock(FabricBlockSettings.copy(Blocks.GRASS)
                    .nonOpaque()),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));
    public static final Block SHADE_GRASS = registerBlock("shade_grass",
            new ShadePlantBlock(FabricBlockSettings.copy(Blocks.GRASS)
                    .nonOpaque()),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));
    public static final Block SHADE_FERN = registerBlock("shade_fern",
            new ShadePlantBlock(FabricBlockSettings.copy(Blocks.FERN)
                    .nonOpaque()),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));


    // Cosmic Fountain
    public static final Block COSMIC_FOUNTAIN = registerBlock("cosmic_fountain",
            new CosmicFountainBlock(FabricBlockSettings.copy(Blocks.END_STONE)),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));

    public static final Block COSMIC_FOUNTAIN_POLE = registerBlock("cosmic_fountain_pole",
            new CosmicFountainPoleBlock(FabricBlockSettings.copy(Blocks.END_STONE)),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));

    public static final Block COSMIC_PORTAL = registerBlock("cosmic_portal",
            new CosmicPortalBlock(FabricBlockSettings.copy(Blocks.END_PORTAL)));


    // Attunement Chamber
    public static final Block ATTUNEMENT_CHAMBER = registerBlock("attunement_chamber",
            new AttunementChamberBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(3.0f, 6.0f).luminance(state -> state.get(AttunementChamberBlock.FILLED) ? 8 : 0)),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));


    // Smokestack
    public static final Block SMOKESTACK = registerBlock("smokestack",
            new SmokestackBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(1.0f, 6.0f)
                    .nonOpaque().suffocates(ModBlocks::never).blockVision(ModBlocks::never)),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));

    // Refinery
    public static final Block REFINERY = registerBlock("refinery",
            new RefineryBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(3.0f, 6.0f)
                    .luminance(state -> state.get(Properties.LIT) ? 15 : 0)),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));

    // Refined Cauldron
    public static final Block REFINED_CAULDRON = registerBlock("refined_cauldron",
            new RefinedCauldronBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(3.0f, 6.0f)
                    .nonOpaque().suffocates(ModBlocks::never).blockVision(ModBlocks::never)),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));


    // Ritual Candles
    public static final Block ECHO_CANDLE = registerBlock("echo_candle",
            new EchoCandleBlock(ModParticles.ECHO_CANDLE_FLAME, FabricBlockSettings.of(Material.DECORATION, MapColor.PALE_YELLOW).strength(0.1f).sounds(BlockSoundGroup.CANDLE)
                    .nonOpaque().suffocates(ModBlocks::never).blockVision(ModBlocks::never)
                    .luminance(RitualCandleBlock::luminence).breakInstantly()),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));

    public static final Block SOUL_CANDLE = registerBlock("soul_candle",
            new SoulCandleBlock(ModParticles.SOUL_CANDLE_FLAME, FabricBlockSettings.of(Material.DECORATION, MapColor.PALE_YELLOW).strength(0.1f).sounds(BlockSoundGroup.CANDLE)
                    .nonOpaque().suffocates(ModBlocks::never).blockVision(ModBlocks::never)
                    .luminance(RitualCandleBlock::luminence).breakInstantly()),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));

    public static final Block COSMIC_CANDLE = registerBlock("cosmic_candle",
            new RitualCandleBlock(ModParticles.COSMIC_CANDLE_FLAME, FabricBlockSettings.of(Material.DECORATION, MapColor.PALE_YELLOW).strength(0.1f).sounds(BlockSoundGroup.CANDLE)
                    .nonOpaque().suffocates(ModBlocks::never).blockVision(ModBlocks::never)
                    .luminance(RitualCandleBlock::luminence).breakInstantly()),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));

    private static boolean never(BlockState blockState, BlockView blockView, BlockPos blockPos) {
        return false;
    }


    public static Block registerBlock(String name, Block block, Item.Settings settings) {
        registerBlockItem(name, block, settings);
        return registerBlock(name, block);
    }

    public static Block registerDreamBlock(String name, Block block, Item.Settings settings) {
        registerDreamBlockItem(name, block, settings);
        return registerBlock(name, block);
    }

    public static Block registerBlock(String name, Block block, Function<Block, BlockItem> item) {
        Registry.register(Registry.ITEM, new Identifier(Initializer.MODID, name),
                item.apply(block));
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

    private static Boolean canSpawnOnLeaves(BlockState state, BlockView world, BlockPos pos, EntityType<?> type) {
        return type == EntityType.OCELOT || type == EntityType.PARROT;
    }
}
