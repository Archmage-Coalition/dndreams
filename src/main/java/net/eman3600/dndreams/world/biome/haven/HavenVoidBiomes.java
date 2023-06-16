package net.eman3600.dndreams.world.biome.haven;

import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiome;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeBuilder;

import static net.eman3600.dndreams.Initializer.MODID;

public class HavenVoidBiomes {

    public static BCLBiome havenSkyBiome() {
        BCLBiomeBuilder builder = BCLBiomeBuilder.start(new Identifier(MODID, "haven_sky"));
        BiomesCommonMethods.addDefaultVoidFeatures(builder);
        BiomesCommonMethods.addDefaultSurface(builder);
        BiomesCommonMethods.setDefaultColors(builder);
        BiomesCommonMethods.addDefaultSounds(builder);
        builder.precipitation(Biome.Precipitation.RAIN);

        return builder
                .feature(GenerationStep.Feature.VEGETAL_DECORATION, VegetationPlacedFeatures.TREES_MEADOW)
                .build();
    }

}
