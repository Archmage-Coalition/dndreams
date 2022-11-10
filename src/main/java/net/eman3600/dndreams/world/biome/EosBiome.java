package net.eman3600.dndreams.world.biome;

import net.eman3600.dndreams.initializers.basics.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeBuilder;
import org.betterx.bclib.interfaces.SurfaceMaterialProvider;

public class EosBiome extends EndBiome.Config {

    public EosBiome() {
        super("eos");
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
                return ModBlocks.EOS_GRASS_BLOCK.getDefaultState();
            }
        };
    }
}
