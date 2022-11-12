package net.eman3600.dndreams.initializers.basics;

import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.blocks.*;
import net.eman3600.dndreams.blocks.crop.*;
import net.eman3600.dndreams.blocks.energy.*;
import net.eman3600.dndreams.blocks.portal.GenericPortalBlock;
import net.eman3600.dndreams.initializers.world.ModConfiguredFeatures;
import net.eman3600.dndreams.initializers.event.ModParticles;
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
                    .requiresTool(), UniformIntProvider.create(3, 6)),

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
                    .nonOpaque().suffocates((state, world, pos) -> false).blockVision((state, world, pos) -> false)),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));
    public static final Block DREAMWOOD_SAPLING = registerDreamBlock("dreamwood_sapling",
            new DreamSaplingBlock(new DreamwoodSaplingGenerator(), FabricBlockSettings.copy(Blocks.OAK_SAPLING)
                    .nonOpaque()),

            new FabricItemSettings().group(ItemGroup.DECORATIONS).food(ModFoodComponents.cakewood(2)));

    // Dreamwood Stairs
    public static final Block DREAMWOOD_STAIRS = registerDreamBlock("dreamwood_stairs",
            new ModStairsBlock(DREAMWOOD_PLANKS.getDefaultState(), FabricBlockSettings.of(Material.WOOD).strength(1.0f)
                    .sounds(BlockSoundGroup.WOOL)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS).food(ModFoodComponents.cakewood(1.5f)));

    // Dreamwood Slab
    public static final Block DREAMWOOD_SLAB = registerDreamBlock("dreamwood_slab",
            new SlabBlock(FabricBlockSettings.of(Material.WOOD).strength(1.0f)
                    .sounds(BlockSoundGroup.WOOL)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS).food(ModFoodComponents.cakewood(0.5f)));

    // Dreamwood Fence
    public static final Block DREAMWOOD_FENCE = registerDreamBlock("dreamwood_fence",
            new FenceBlock(FabricBlockSettings.of(Material.WOOD).strength(1.0f)
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
                    .nonOpaque().suffocates((state, world, pos) -> false).blockVision((state, world, pos) -> false)),

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



    // Sculk Wood Logs
    public static final Block SCULK_WOOD_LOG = registerBlock("sculk_wood_log",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(1.0f)
                    .sounds(BlockSoundGroup.SCULK)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block SCULK_WOOD = registerBlock("sculk_wood",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(1.0f)
                    .sounds(BlockSoundGroup.SCULK)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block STRIPPED_SCULK_WOOD_LOG = registerBlock("stripped_sculk_wood_log",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(1.0f)
                    .sounds(BlockSoundGroup.SCULK)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block STRIPPED_SCULK_WOOD = registerBlock("stripped_sculk_wood",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(1.0f)
                    .sounds(BlockSoundGroup.SCULK)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Sculk Wood Planks
    public static final Block SCULK_WOOD_PLANKS = registerBlock("sculk_wood_planks",
            new Block(FabricBlockSettings.of(Material.WOOD).strength(1.0f)
                    .sounds(BlockSoundGroup.SCULK)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Sculk Wood Leaves & Sapling
    public static final Block SCULK_WOOD_LEAVES = registerBlock("sculk_wood_leaves",
            new SculkLeavesBlock(FabricBlockSettings.copy(Blocks.AZALEA_LEAVES)
                    .sounds(BlockSoundGroup.SCULK_SHRIEKER)
                    .nonOpaque().suffocates((state, world, pos) -> false).blockVision((state, world, pos) -> false)),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));
    public static final Block SCULK_WOOD_SAPLING = registerBlock("sculk_wood_sapling",
            new SculkWoodSaplingBlock(new SculkWoodSaplingGenerator(), FabricBlockSettings.copy(Blocks.OAK_SAPLING)
                    .sounds(BlockSoundGroup.SCULK_SHRIEKER)
                    .nonOpaque()),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));

    public static final Block SCULK_LIGHT = registerBlock("sculk_light",
            new SculkLeavesBlock(AbstractBlock.Settings.of(Material.SOLID_ORGANIC, MapColor.CYAN).strength(0.2f).ticksRandomly().nonOpaque().allowsSpawning(ModBlocks::canSpawnOnLeaves).suffocates(ModBlocks::never).blockVision(ModBlocks::never).sounds(BlockSoundGroup.SCULK_CATALYST).luminance(state -> 15)),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));

    private static boolean never(BlockState blockState, BlockView blockView, BlockPos blockPos) {
        return false;
    }

    // Sculk Wood Stairs
    public static final Block SCULK_WOOD_STAIRS = registerBlock("sculk_wood_stairs",
            new ModStairsBlock(SCULK_WOOD_PLANKS.getDefaultState(), FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.SCULK)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Sculk Wood Slab
    public static final Block SCULK_WOOD_SLAB = registerBlock("sculk_wood_slab",
            new SlabBlock(FabricBlockSettings.of(Material.WOOD).strength(1.0f)
                    .sounds(BlockSoundGroup.SCULK)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Sculk Wood Fence
    public static final Block SCULK_WOOD_FENCE = registerBlock("sculk_wood_fence",
            new FenceBlock(FabricBlockSettings.of(Material.WOOD).strength(1.0f)
                    .sounds(BlockSoundGroup.SCULK)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Sculk Wood Fence Gate
    public static final Block SCULK_WOOD_FENCE_GATE = registerBlock("sculk_wood_fence_gate",
            new FenceGateBlock(FabricBlockSettings.of(Material.WOOD).strength(1.0f)
                    .sounds(BlockSoundGroup.SCULK)),

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
                    .nonOpaque().suffocates((state, world, pos) -> false).blockVision((state, world, pos) -> false)),

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






    // Selene Wood Logs
    public static final Block SELENE_LOG = registerBlock("selene_log",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block SELENE_WOOD = registerBlock("selene_wood",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block SELENE_STRIPPED_LOG = registerBlock("selene_stripped_log",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block SELENE_STRIPPED_WOOD = registerBlock("selene_stripped_wood",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Selene Wood Planks
    public static final Block SELENE_PLANKS = registerBlock("selene_planks",
            new Block(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Selene Wood Leaves & Sapling
    public static final Block SELENE_LEAVES = registerBlock("selene_leaves",
            new LeavesBlock(FabricBlockSettings.copy(Blocks.AZALEA_LEAVES)
                    .nonOpaque().suffocates((state, world, pos) -> false).blockVision((state, world, pos) -> false)),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));
    public static final Block SELENE_SAPLING = registerBlock("selene_sapling",
            new EndSaplingBlock(new GenericLargeSaplingGenerator(random -> ModConfiguredFeatures.SELENE_TREE), FabricBlockSettings.copy(Blocks.OAK_SAPLING)
                    .nonOpaque()),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));

    // Selene Wood Stairs
    public static final Block SELENE_STAIRS = registerBlock("selene_stairs",
            new ModStairsBlock(SELENE_PLANKS.getDefaultState(), FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Selene Wood Slab
    public static final Block SELENE_SLAB = registerBlock("selene_slab",
            new SlabBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Selene Wood Fence
    public static final Block SELENE_FENCE = registerBlock("selene_fence",
            new FenceBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Selene Wood Fence Gate
    public static final Block SELENE_FENCE_GATE = registerBlock("selene_fence_gate",
            new FenceGateBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));




    // Helios Wood Logs
    public static final Block HELIOS_LOG = registerBlock("helios_log",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block HELIOS_WOOD = registerBlock("helios_wood",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block HELIOS_STRIPPED_LOG = registerBlock("helios_stripped_log",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block HELIOS_STRIPPED_WOOD = registerBlock("helios_stripped_wood",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Helios Wood Planks
    public static final Block HELIOS_PLANKS = registerBlock("helios_planks",
            new Block(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Helios Wood Leaves & Sapling
    public static final Block HELIOS_LEAVES = registerBlock("helios_leaves",
            new LeavesBlock(FabricBlockSettings.copy(Blocks.AZALEA_LEAVES)
                    .nonOpaque().suffocates((state, world, pos) -> false).blockVision((state, world, pos) -> false)),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));
    public static final Block HELIOS_SAPLING = registerBlock("helios_sapling",
            new EndSaplingBlock(new GenericLargeSaplingGenerator(random -> ModConfiguredFeatures.HELIOS_TREE), FabricBlockSettings.copy(Blocks.OAK_SAPLING)
                    .nonOpaque()),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));

    // Helios Wood Stairs
    public static final Block HELIOS_STAIRS = registerBlock("helios_stairs",
            new ModStairsBlock(HELIOS_PLANKS.getDefaultState(), FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Helios Wood Slab
    public static final Block HELIOS_SLAB = registerBlock("helios_slab",
            new SlabBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Helios Wood Fence
    public static final Block HELIOS_FENCE = registerBlock("helios_fence",
            new FenceBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Helios Wood Fence Gate
    public static final Block HELIOS_FENCE_GATE = registerBlock("helios_fence_gate",
            new FenceGateBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));





    // Eos Wood Logs
    public static final Block EOS_LOG = registerBlock("eos_log",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block EOS_WOOD = registerBlock("eos_wood",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block EOS_STRIPPED_LOG = registerBlock("eos_stripped_log",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block EOS_STRIPPED_WOOD = registerBlock("eos_stripped_wood",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Eos Wood Planks
    public static final Block EOS_PLANKS = registerBlock("eos_planks",
            new Block(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Eos Wood Leaves & Sapling
    public static final Block EOS_LEAVES = registerBlock("eos_leaves",
            new LeavesBlock(FabricBlockSettings.copy(Blocks.AZALEA_LEAVES)
                    .nonOpaque().suffocates((state, world, pos) -> false).blockVision((state, world, pos) -> false)),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));
    public static final Block EOS_SAPLING = registerBlock("eos_sapling",
            new EndSaplingBlock(new GenericLargeSaplingGenerator(random -> ModConfiguredFeatures.EOS_TREE), FabricBlockSettings.copy(Blocks.OAK_SAPLING)
                    .nonOpaque()),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));

    // Eos Wood Stairs
    public static final Block EOS_STAIRS = registerBlock("eos_stairs",
            new ModStairsBlock(EOS_PLANKS.getDefaultState(), FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Eos Wood Slab
    public static final Block EOS_SLAB = registerBlock("eos_slab",
            new SlabBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Eos Wood Fence
    public static final Block EOS_FENCE = registerBlock("eos_fence",
            new FenceBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // Eos Wood Fence Gate
    public static final Block EOS_FENCE_GATE = registerBlock("eos_fence_gate",
            new FenceGateBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));






    // End Grass
    public static final Block SELENE_GRASS_BLOCK = registerBlock("selene_grass_block",
            new Block(FabricBlockSettings.copy(Blocks.END_STONE)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    public static final Block SELENE_GRASS = registerBlock("selene_grass",
            new EndFernBlock(FabricBlockSettings.copy(Blocks.GRASS)
                    .nonOpaque()),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));


    public static final Block HELIOS_GRASS_BLOCK = registerBlock("helios_grass_block",
            new Block(FabricBlockSettings.copy(Blocks.END_STONE)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    public static final Block HELIOS_GRASS = registerBlock("helios_grass",
            new EndFernBlock(FabricBlockSettings.copy(Blocks.GRASS)
                    .nonOpaque()),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));


    public static final Block EOS_GRASS_BLOCK = registerBlock("eos_grass_block",
            new Block(FabricBlockSettings.copy(Blocks.END_STONE)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    public static final Block EOS_GRASS = registerBlock("eos_grass",
            new EndFernBlock(FabricBlockSettings.copy(Blocks.GRASS)
                    .nonOpaque()),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));






    // Snowbell Crop
    public static final Block SNOWBELL = registerBlock("snowbell",
            new SnowbellBlock(FabricBlockSettings.copy(Blocks.WHEAT).nonOpaque()));

    // Water Artichoke Crop
    public static final Block WATER_ARTICHOKE = registerBlock("water_artichoke",
            new WaterArtichokeBlock(FabricBlockSettings.copy(Blocks.WHEAT).nonOpaque()));

    // Ember Moss
    public static final Block EMBER_MOSS = registerBlock("ember_moss",
            new EmberMossBlock(FabricBlockSettings.copy(Blocks.RED_MUSHROOM).nonOpaque().luminance(state -> 15)),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));

    // Applethorn Crop
    public static final Block APPLETHORN = registerBlock("applethorn",
            new ApplethornBlock(FabricBlockSettings.copy(Blocks.WHEAT).nonOpaque()));

    // Wither Blossom Crop
    public static final Block WITHER_BLOSSOM = registerBlock("wither_blossom",
            new WitherBlossomBlock(FabricBlockSettings.copy(Blocks.SWEET_BERRY_BUSH).nonOpaque()));


    // Ancient Portal
    public static final Block CHARGED_DEEPSLATE = registerBlock("charged_deepslate",
            new ChargedDeepslateBlock(FabricBlockSettings.of(Material.STONE).strength(-1.0F, 3600000.0F).dropsNothing().allowsSpawning(ModBlocks::never)),

            new FabricItemSettings().group(ItemGroup.DECORATIONS));

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

    private static Boolean canSpawnOnLeaves(BlockState state, BlockView world, BlockPos pos, EntityType<?> type) {
        return type == EntityType.OCELOT || type == EntityType.PARROT;
    }
}
