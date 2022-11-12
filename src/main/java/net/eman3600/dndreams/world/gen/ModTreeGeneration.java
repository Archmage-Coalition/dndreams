package net.eman3600.dndreams.world.gen;

import net.eman3600.dndreams.initializers.bclib.ModBiomes;
import net.eman3600.dndreams.initializers.world.ModPlacedFeatures;
import net.eman3600.dndreams.util.ModTags;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.gen.GenerationStep;

public class ModTreeGeneration {
    public static void generateTrees() {
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(),
                GenerationStep.Feature.VEGETAL_DECORATION, ModPlacedFeatures.DREAMWOOD_PLACED.getKey().get());
    }
}
