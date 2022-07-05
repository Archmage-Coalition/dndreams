package net.eman3600.dndreams.world.gen;

import net.eman3600.dndreams.initializers.ModDimensions;
import net.eman3600.dndreams.util.ModTags;
import net.eman3600.dndreams.world.feature.ModPlacedFeatures;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.gen.GenerationStep;

public class ModTreeGeneration {
    public static void generateTrees() {
        BiomeModifications.addFeature(ModDimensions.foundInDream(),
                GenerationStep.Feature.VEGETAL_DECORATION, ModPlacedFeatures.DREAMWOOD_PLACED.getKey().get());
    }
}
