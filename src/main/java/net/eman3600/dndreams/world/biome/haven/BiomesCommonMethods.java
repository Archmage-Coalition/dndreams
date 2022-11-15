package net.eman3600.dndreams.world.biome.haven;

import net.eman3600.dndreams.initializers.world.ModPlacedFeatures;
import net.minecraft.block.Blocks;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Decoration;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.OrePlacedFeatures;
import net.minecraft.world.gen.feature.UndergroundPlacedFeatures;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeBuilder;
import org.betterx.bclib.api.v2.levelgen.surface.SurfaceRuleBuilder;

public class BiomesCommonMethods {
    public static void addDefaultLandFeatures(BCLBiomeBuilder builder) {
        builder
                //.carver(Carving.AIR, EdenCarvers.CAVE_CONFIGURED)
                .feature(GenerationStep.Feature.UNDERGROUND_DECORATION, UndergroundPlacedFeatures.AMETHYST_GEODE)
                .feature(GenerationStep.Feature.UNDERGROUND_DECORATION, OrePlacedFeatures.ORE_GRANITE_UPPER)
                .feature(GenerationStep.Feature.UNDERGROUND_DECORATION, OrePlacedFeatures.ORE_ANDESITE_UPPER)
                .feature(GenerationStep.Feature.UNDERGROUND_DECORATION, OrePlacedFeatures.ORE_DIORITE_UPPER)
                .feature(ModPlacedFeatures.SMALL_ISLAND);
    }

    public static void addDefaultVoidFeatures(BCLBiomeBuilder builder) {
        builder
                .feature(ModPlacedFeatures.SMALL_ISLAND);
    }

    public static void addDefaultSurface(BCLBiomeBuilder builder) {
        builder.surface(SurfaceRuleBuilder
                .start()
                .surface(Blocks.GRASS_BLOCK.getDefaultState())
                .subsurface(Blocks.DIRT.getDefaultState(), 3)
                .build()
        );
    }

    public static void addDefaultSounds(BCLBiomeBuilder builder) {
        builder.music(SoundEvents.MUSIC_NETHER_SOUL_SAND_VALLEY);
    }

    public static void setDefaultColors(BCLBiomeBuilder builder) {
        builder.skyColor(113, 178, 255).fogColor(183, 212, 255).waterFogColor(329011).waterColor(4159204);
    }
}
