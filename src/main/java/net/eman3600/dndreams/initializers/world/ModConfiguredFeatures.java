package net.eman3600.dndreams.initializers.world;

import com.google.common.collect.ImmutableList;
import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.initializers.basics.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.ThreeLayersFeatureSize;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.AcaciaFoliagePlacer;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.foliage.RandomSpreadFoliagePlacer;
import net.minecraft.world.gen.foliage.SpruceFoliagePlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.treedecorator.AlterGroundTreeDecorator;
import net.minecraft.world.gen.trunk.BendingTrunkPlacer;
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

    public static final RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> JAPANESE_MAPLE_TREE =
            ConfiguredFeatures.register("japanese_maple_tree", Feature.TREE, new TreeFeatureConfig.Builder(
                    BlockStateProvider.of(ModBlocks.JAPANESE_MAPLE_LOG),
                    new ForkingTrunkPlacer(5, 2, 2),
                    BlockStateProvider.of(ModBlocks.JAPANESE_MAPLE_LEAVES),
                    new AcaciaFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0)),
                    new TwoLayersFeatureSize(1, 0, 2)).ignoreVines().build());

    public static final RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> PRISTINE_TREE =
            ConfiguredFeatures.register("pristine_tree", Feature.TREE,
                    new TreeFeatureConfig.Builder(BlockStateProvider.of(ModBlocks.PRISTINE_LOG),
                            new StraightTrunkPlacer(5, 6, 3), BlockStateProvider.of(ModBlocks.PRISTINE_LEAVES),
                            new BlobFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0), 4),
                            new ThreeLayersFeatureSize(1, 1, 0, 1, 2, OptionalInt.empty()))
                            .dirtProvider(BlockStateProvider.of(ModBlocks.MARBLE)).ignoreVines().build());

    public static final RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> SHADE_TREE =
            ConfiguredFeatures.register("shade_tree", Feature.TREE,
                    new TreeFeatureConfig.Builder(BlockStateProvider.of(ModBlocks.SHADE_LOG),
                            new BendingTrunkPlacer(4, 2, 0, 3, UniformIntProvider.create(1, 2)),
                            new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(ModBlocks.SHADE_LEAVES.getDefaultState(), 1)),
                            new RandomSpreadFoliagePlacer(ConstantIntProvider.create(3), ConstantIntProvider.create(0), ConstantIntProvider.create(2), 50),
                            new TwoLayersFeatureSize(1, 0, 1))
                            .dirtProvider(BlockStateProvider.of(ModBlocks.SHADE_MOSS)).build());



    public static final RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> HAVEN_TREE =
            ConfiguredFeatures.register("haven_tree", Feature.TREE, new TreeFeatureConfig.Builder(
                    BlockStateProvider.of(ModBlocks.HAVEN_LOG),
                    new StraightTrunkPlacer(3, 4, 3),
                    BlockStateProvider.of(ModBlocks.HAVEN_LEAVES),
                    new BlobFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0), 4),
                    new TwoLayersFeatureSize(1, 0, 2)).dirtProvider(BlockStateProvider.of(ModBlocks.HAVEN_DIRT)).build());

    public static final RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> PINE_TREE =
            ConfiguredFeatures.register("pine_tree", Feature.TREE, new TreeFeatureConfig.Builder(
                    BlockStateProvider.of(ModBlocks.PINE_LOG),
                    new StraightTrunkPlacer(5, 6, 3),
                    BlockStateProvider.of(ModBlocks.PINE_LEAVES),
                    new SpruceFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0), ConstantIntProvider.create(4)),
                    new TwoLayersFeatureSize(1, 0, 2)).dirtProvider(BlockStateProvider.of(ModBlocks.HAVEN_DIRT)).build());

    public static final RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> STAR_TREE =
            ConfiguredFeatures.register("star_tree", Feature.TREE, new TreeFeatureConfig.Builder(
                    BlockStateProvider.of(ModBlocks.STAR_LOG),
                    new StraightTrunkPlacer(3, 4, 3),
                    BlockStateProvider.of(ModBlocks.STAR_LEAVES),
                    new BlobFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0), 4),
                    new TwoLayersFeatureSize(1, 0, 2)).dirtProvider(BlockStateProvider.of(ModBlocks.HAVEN_DIRT)).build());




    public static final RegistryEntry<ConfiguredFeature<RandomPatchFeatureConfig, ?>> GOLDEN_GRASS =
            ConfiguredFeatures.register("golden_grass", Feature.RANDOM_PATCH,
                    ConfiguredFeatures.createRandomPatchFeatureConfig(32, PlacedFeatures.createEntry(Feature.SIMPLE_BLOCK,
                            new SimpleBlockFeatureConfig(BlockStateProvider.of(ModBlocks.GOLDEN_GRASS)))));



    public static final RegistryEntry<ConfiguredFeature<RandomPatchFeatureConfig, ?>> HAVEN_GRASS =
            ConfiguredFeatures.register("haven_grass", Feature.RANDOM_PATCH,
                    ConfiguredFeatures.createRandomPatchFeatureConfig(32, PlacedFeatures.createEntry(Feature.SIMPLE_BLOCK,
                            new SimpleBlockFeatureConfig(BlockStateProvider.of(ModBlocks.HAVEN_GRASS)))));

    public static final RegistryEntry<ConfiguredFeature<RandomPatchFeatureConfig, ?>> STAR_GRASS =
            ConfiguredFeatures.register("star_grass", Feature.RANDOM_PATCH,
                    ConfiguredFeatures.createRandomPatchFeatureConfig(32, PlacedFeatures.createEntry(Feature.SIMPLE_BLOCK,
                            new SimpleBlockFeatureConfig(BlockStateProvider.of(ModBlocks.STAR_GRASS)))));







    public static final RegistryEntry<PlacedFeature> DREAMWOOD_CHECKED =
            PlacedFeatures.register("dreamwood_checked", DREAMWOOD_TREE,
                    PlacedFeatures.wouldSurvive(ModBlocks.DREAMWOOD_SAPLING));

    public static final RegistryEntry<PlacedFeature> PRISTINE_CHECKED =
            PlacedFeatures.register("pristine_checked", PRISTINE_TREE,
                    PlacedFeatures.wouldSurvive(ModBlocks.PRISTINE_SAPLING));






    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> DREAMWOOD_SPAWN =
            ConfiguredFeatures.register("dreamwood_spawn", Feature.RANDOM_SELECTOR,
                    new RandomFeatureConfig(List.of(new RandomFeatureEntry(DREAMWOOD_CHECKED, 0.5f)),
                            DREAMWOOD_CHECKED));

    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> PRISTINE_SPAWN =
            ConfiguredFeatures.register("pristine_spawn", Feature.RANDOM_SELECTOR,
                    new RandomFeatureConfig(List.of(new RandomFeatureEntry(PRISTINE_CHECKED, 0.5f)),
                            PRISTINE_CHECKED));



    public static void registerConfiguredFeatures() {
        System.out.println("Registering configured features for " + Initializer.MODID);
    }
}
