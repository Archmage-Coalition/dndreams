package net.eman3600.dndreams.world.gen;

import net.eman3600.dndreams.util.ModTags;
import net.eman3600.dndreams.initializers.world.ModPlacedFeatures;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.gen.GenerationStep;

public class ModOreGeneration {
    public static void generateOres() {
        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.GEN_HELLSLATE),
                GenerationStep.Feature.UNDERGROUND_ORES, ModPlacedFeatures.HELLSLATE_ORE_PLACED.getKey().get());
        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.GEN_HELLSLATE_COMMON),
                GenerationStep.Feature.UNDERGROUND_ORES, ModPlacedFeatures.HELLSLATE_ORE_COMMON_PLACED.getKey().get());
        BiomeModifications.addFeature(BiomeSelectors.foundInTheEnd(),
                GenerationStep.Feature.UNDERGROUND_ORES, ModPlacedFeatures.CELESTIUM_ORE_PLACED.getKey().get());
    }
}
