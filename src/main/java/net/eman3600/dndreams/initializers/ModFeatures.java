package net.eman3600.dndreams.initializers;

import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.world.OreFeatures;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.*;

import java.util.List;

public class ModFeatures {
    // ORES DECLARATION
    public static final List<OreFeatureConfig.Target> HELLSLATE_ORES = List.of(
            OreFeatureConfig.createTarget(OreConfiguredFeatures.BASE_STONE_NETHER, ModBlocks.HELLSLATE.getDefaultState()),
            OreFeatureConfig.createTarget(OreFeatures.BASE_SAND_NETHER, ModBlocks.HELLSAND.getDefaultState()),
            OreFeatureConfig.createTarget(OreFeatures.BASE_SOIL_NETHER, ModBlocks.HELLSOIL.getDefaultState())
    );

    public static final List<OreFeatureConfig.Target> CELESTIUM_ORES = List.of(
            OreFeatureConfig.createTarget(OreFeatures.BASE_END_STONE, ModBlocks.CELESTIUM_ORE.getDefaultState())
    );

    // ORES REGISTRY
    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> HELLSLATE_ORE =
            ConfiguredFeatures.register("hellslate_ore",Feature.ORE, new OreFeatureConfig(HELLSLATE_ORES, 4));

    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> CELESTIUM_ORE =
            ConfiguredFeatures.register("celestium_ore",Feature.ORE, new OreFeatureConfig(CELESTIUM_ORES, 4));

    public static void registerFeatures() {
        System.out.println("Registering features for " + Initializer.MODID);
    }
}
