package net.eman3600.dndreams.initializers.world;

import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.initializers.basics.ModBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.ThreeLayersFeatureSize;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.AcaciaFoliagePlacer;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.foliage.DarkOakFoliagePlacer;
import net.minecraft.world.gen.foliage.SpruceFoliagePlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.trunk.DarkOakTrunkPlacer;
import net.minecraft.world.gen.trunk.ForkingTrunkPlacer;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;

import java.util.List;
import java.util.OptionalInt;

public class ModConfiguredFeatures {
    public static final RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> DREAMWOOD_TREE =
            ConfiguredFeatures.register("dreamwood_tree", Feature.TREE, new TreeFeatureConfig.Builder(
                    BlockStateProvider.of(ModBlocks.DREAMWOOD_LOG),
                    new StraightTrunkPlacer(5, 6, 3),
                    BlockStateProvider.of(ModBlocks.DREAMWOOD_LEAVES),
                    new SpruceFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0), ConstantIntProvider.create(4)),
                    new TwoLayersFeatureSize(1, 0, 2)).dirtProvider(BlockStateProvider.of(Blocks.GRASS_BLOCK)).build());

    public static final RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> SAKURA_TREE =
            ConfiguredFeatures.register("sakura_tree", Feature.TREE, new TreeFeatureConfig.Builder(
                    BlockStateProvider.of(ModBlocks.SAKURA_LOG),
                    new StraightTrunkPlacer(3, 4, 3),
                    BlockStateProvider.of(ModBlocks.SAKURA_LEAVES),
                    new BlobFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0), 4),
                    new TwoLayersFeatureSize(1, 0, 2)).dirtProvider(BlockStateProvider.of(Blocks.GRASS_BLOCK)).build());

    public static final RegistryEntry<ConfiguredFeature<HugeFungusFeatureConfig, ?>> SCULK_WOOD_TREE =
            ConfiguredFeatures.register("sculk_wood_tree", Feature.HUGE_FUNGUS,
                    new HugeFungusFeatureConfig(Blocks.SCULK.getDefaultState(),
                            ModBlocks.SCULK_WOOD_LOG.getDefaultState(),
                            ModBlocks.SCULK_WOOD_LEAVES.getDefaultState(),
                            ModBlocks.SCULK_LIGHT.getDefaultState(),
                            true));

    public static final RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> JAPANESE_MAPLE_TREE =
            ConfiguredFeatures.register("japanese_maple_tree", Feature.TREE, new TreeFeatureConfig.Builder(
                    BlockStateProvider.of(ModBlocks.JAPANESE_MAPLE_LOG),
                    new ForkingTrunkPlacer(5, 2, 2),
                    BlockStateProvider.of(ModBlocks.JAPANESE_MAPLE_LEAVES),
                    new AcaciaFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0)),
                    new TwoLayersFeatureSize(1, 0, 2)).ignoreVines().build());


    public static final RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> HELIOS_TREE =
            ConfiguredFeatures.register("helios_tree", Feature.TREE,
                    new TreeFeatureConfig.Builder(BlockStateProvider.of(ModBlocks.HELIOS_LOG),
                            new DarkOakTrunkPlacer(6, 2, 1), BlockStateProvider.of(ModBlocks.HELIOS_LEAVES),
                            new DarkOakFoliagePlacer(ConstantIntProvider.create(0), ConstantIntProvider.create(0)),
                            new ThreeLayersFeatureSize(1, 1, 0, 1, 2, OptionalInt.empty()))
                            .dirtProvider(BlockStateProvider.of(Blocks.END_STONE)).ignoreVines().build());

    public static final RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> EOS_TREE =
            ConfiguredFeatures.register("eos_tree", Feature.TREE,
                    new TreeFeatureConfig.Builder(BlockStateProvider.of(ModBlocks.EOS_LOG),
                            new DarkOakTrunkPlacer(6, 2, 1), BlockStateProvider.of(ModBlocks.EOS_LEAVES),
                            new DarkOakFoliagePlacer(ConstantIntProvider.create(0), ConstantIntProvider.create(0)),
                            new ThreeLayersFeatureSize(1, 1, 0, 1, 2, OptionalInt.empty()))
                            .dirtProvider(BlockStateProvider.of(Blocks.END_STONE)).ignoreVines().build());

    public static final RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> SELENE_TREE =
            ConfiguredFeatures.register("selene_tree", Feature.TREE,
                    new TreeFeatureConfig.Builder(BlockStateProvider.of(ModBlocks.SELENE_LOG),
                            new DarkOakTrunkPlacer(6, 2, 1), BlockStateProvider.of(ModBlocks.SELENE_LEAVES),
                            new DarkOakFoliagePlacer(ConstantIntProvider.create(0), ConstantIntProvider.create(0)),
                            new ThreeLayersFeatureSize(1, 1, 0, 1, 2, OptionalInt.empty()))
                            .dirtProvider(BlockStateProvider.of(Blocks.END_STONE)).ignoreVines().build());










    public static final RegistryEntry<PlacedFeature> DREAMWOOD_CHECKED =
            PlacedFeatures.register("dreamwood_checked", DREAMWOOD_TREE,
                    PlacedFeatures.wouldSurvive(ModBlocks.DREAMWOOD_SAPLING));

    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> DREAMWOOD_SPAWN =
            ConfiguredFeatures.register("dreamwood_spawn", Feature.RANDOM_SELECTOR,
                    new RandomFeatureConfig(List.of(new RandomFeatureEntry(DREAMWOOD_CHECKED, 0.5f)),
                            DREAMWOOD_CHECKED));



    public static void registerConfiguredFeatures() {
        System.out.println("Registering configured features for " + Initializer.MODID);
    }
}