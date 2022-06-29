package net.eman3600.dndreams.initializers;

import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.blocks.DreamTable;
import net.eman3600.dndreams.blocks.Hellsand;
import net.eman3600.dndreams.blocks.ModStairsBlock;
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

    // World Fountain
    public static final Block WORLD_FOUNTAIN = registerBlock("world_fountain",
            new Block(FabricBlockSettings.of(Material.STONE).strength(-1.0F, 3600000.0F).dropsNothing().allowsSpawning(ModBlocks::never)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block FLOWING_BEDROCK = registerBlock("flowing_bedrock",
            new Block(FabricBlockSettings.of(Material.STONE).strength(-1.0F, 3600000.0F).dropsNothing().allowsSpawning(ModBlocks::never)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // DREAM TABLE
    public static final Block DREAM_TABLE = registerBlock("dream_table",
            new DreamTable(FabricBlockSettings.of(Material.WOOD).strength(2.5f)
                    .sounds(BlockSoundGroup.WOOD)),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // DREAMWOOD PLANKS
    public static final Block DREAMWOOD_PLANKS = registerBlock("dreamwood_planks",
            new Block(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .requiresTool()),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // DREAMWOOD STAIRS
    public static final Block DREAMWOOD_STAIRS = registerBlock("dreamwood_stairs",
            new ModStairsBlock(DREAMWOOD_PLANKS.getDefaultState(), FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .requiresTool()),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // DREAMWOOD SLAB
    public static final Block DREAMWOOD_SLAB = registerBlock("dreamwood_slab",
            new SlabBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .requiresTool()),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // DREAMWOOD FENCE
    public static final Block DREAMWOOD_FENCE = registerBlock("dreamwood_fence",
            new FenceBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .requiresTool()),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    // DREAMWOOD FENCE GATE
    public static final Block DREAMWOOD_FENCE_GATE = registerBlock("dreamwood_fence_gate",
            new FenceGateBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .requiresTool()),

            new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));



    private static Block registerBlock(String name, Block block, Item.Settings settings) {
        registerBlockItem(name, block, settings);
        return Registry.register(Registry.BLOCK, new Identifier(Initializer.MODID, name), block);
    }

    private static Item registerBlockItem(String name, Block block, Item.Settings settings) {
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
