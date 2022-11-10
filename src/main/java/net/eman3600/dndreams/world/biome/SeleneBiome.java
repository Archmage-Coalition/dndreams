package net.eman3600.dndreams.world.biome;

import net.eman3600.dndreams.initializers.basics.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import org.betterx.bclib.api.v2.generator.BiomeType;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeBuilder;
import org.betterx.bclib.api.v2.levelgen.biomes.BiomeAPI;
import org.betterx.bclib.config.Configs;
import org.betterx.bclib.interfaces.SurfaceMaterialProvider;

import java.util.LinkedList;
import java.util.List;

public class SeleneBiome extends EndBiome.Config {

    public SeleneBiome() {
        super("selene");
    }

    @Override
    protected void addCustomBuildData(BCLBiomeBuilder builder) {
        builder
                .spawn(EntityType.ENDERMAN, 10, 1, 2);
    }

    @Override
    protected SurfaceMaterialProvider surfaceMaterial() {
        return new EndBiome.DefaultSurfaceMaterialProvider() {
            @Override
            public BlockState getTopMaterial() {
                return ModBlocks.SELENE_GRASS_BLOCK.getDefaultState();
            }
        };
    }
}
