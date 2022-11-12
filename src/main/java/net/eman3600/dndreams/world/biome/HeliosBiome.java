package net.eman3600.dndreams.world.biome;

import net.eman3600.dndreams.initializers.basics.ModBlocks;
import net.eman3600.dndreams.initializers.world.ModPlacedFeatures;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.world.gen.GenerationStep;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeBuilder;
import org.betterx.bclib.interfaces.SurfaceMaterialProvider;

public class HeliosBiome extends EndBiome.Config {

    public HeliosBiome() {
        super("helios");
    }

    @Override
    protected void addCustomBuildData(BCLBiomeBuilder builder) {
        builder
                .feature(GenerationStep.Feature.VEGETAL_DECORATION, ModPlacedFeatures.HELIOS_PLACED)
                .feature(GenerationStep.Feature.VEGETAL_DECORATION, ModPlacedFeatures.HELIOS_GRASS)
                .spawn(EntityType.ENDERMAN, 10, 1, 2);
    }

    @Override
    protected SurfaceMaterialProvider surfaceMaterial() {
        return new EndBiome.DefaultSurfaceMaterialProvider() {
            @Override
            public BlockState getTopMaterial() {
                return ModBlocks.HELIOS_GRASS_BLOCK.getDefaultState();
            }
        };
    }
}
