package net.eman3600.dndreams.world.biome.haven;

import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.TreePlacedFeatures;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiome;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeBuilder;

import static net.eman3600.dndreams.Initializer.MODID;

public class HavenLandBiomes {

    public static BCLBiome havenForestBiome() {
        BCLBiomeBuilder builder = BCLBiomeBuilder.start(new Identifier(MODID, "haven_forest"));
        BiomesCommonMethods.addDefaultLandFeatures(builder);
        BiomesCommonMethods.addDefaultSurface(builder);
        BiomesCommonMethods.setDefaultColors(builder);
        BiomesCommonMethods.addDefaultSounds(builder);

        return builder
                .feature(GenerationStep.Feature.VEGETAL_DECORATION, VegetationPlacedFeatures.TREES_MEADOW)
                .build();
    }

}
