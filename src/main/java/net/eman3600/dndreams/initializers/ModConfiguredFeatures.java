package net.eman3600.dndreams.initializers;

import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.blocks.DreamGrassBlock;
import net.eman3600.dndreams.initializers.ModBlocks;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;

import java.util.List;

public class ModConfiguredFeatures {
    public static final RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> DREAMWOOD_TREE =
            ConfiguredFeatures.register("dreamwood_tree", Feature.TREE, new TreeFeatureConfig.Builder(
                    BlockStateProvider.of(ModBlocks.DREAMWOOD_LOG),
                    new StraightTrunkPlacer(5, 6, 3),
                    BlockStateProvider.of(ModBlocks.DREAMWOOD_LEAVES),
                    new BlobFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0), 4),
                    new TwoLayersFeatureSize(1, 0, 2)).dirtProvider(BlockStateProvider.of(ModBlocks.DREAM_GRASS)).build());

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
