package net.eman3600.dndreams.world.biome.haven;

import net.eman3600.dndreams.initializers.basics.ModBlocks;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiome;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeBuilder;

import static net.eman3600.dndreams.Initializer.MODID;

public class HavenCaveBiomes {
    public static BCLBiome havenEmptyCave() {
        BCLBiomeBuilder builder = BCLBiomeBuilder.start(new Identifier(MODID, "empty_cave"));
        BiomesCommonMethods.addDefaultLandFeatures(builder);
        BiomesCommonMethods.setDefaultColors(builder);
        BiomesCommonMethods.addDefaultSounds(builder);
        builder.precipitation(Biome.Precipitation.RAIN);

        return builder.surface(ModBlocks.HAVEN_STONE).plantsColor(0x707c47).build();
    }
}
