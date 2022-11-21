package net.eman3600.dndreams.world.gen;

import net.eman3600.dndreams.initializers.world.ModPlacedFeatures;
import net.eman3600.dndreams.util.ModTags;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.gen.GenerationStep;

public class ModWorldGen {
    public static void generate() {
        generateOres();
        generateTrees();
    }




    private static void generateOres() {
        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.GEN_HELLSLATE),
                GenerationStep.Feature.UNDERGROUND_ORES, ModPlacedFeatures.HELLSLATE_ORE_PLACED.getKey().get());
        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.GEN_HELLSLATE_COMMON),
                GenerationStep.Feature.UNDERGROUND_ORES, ModPlacedFeatures.HELLSLATE_ORE_COMMON_PLACED.getKey().get());
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(),
                GenerationStep.Feature.UNDERGROUND_ORES, ModPlacedFeatures.VITAL_ORE_PLACED.getKey().get());
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(),
                GenerationStep.Feature.UNDERGROUND_ORES, ModPlacedFeatures.VITAL_ORE_SPARSE_PLACED.getKey().get());
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(),
                GenerationStep.Feature.UNDERGROUND_ORES, ModPlacedFeatures.VITAL_ORE_BURIED_PLACED.getKey().get());
        BiomeModifications.addFeature(BiomeSelectors.foundInTheEnd(),
                GenerationStep.Feature.UNDERGROUND_ORES, ModPlacedFeatures.CELESTIUM_ORE_PLACED.getKey().get());
    }


    private static void generateTrees() {
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(),
                GenerationStep.Feature.VEGETAL_DECORATION, ModPlacedFeatures.DREAMWOOD_PLACED.getKey().get());
    }
}
