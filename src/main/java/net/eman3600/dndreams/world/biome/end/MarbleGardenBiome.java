package net.eman3600.dndreams.world.biome.end;

import net.eman3600.dndreams.initializers.basics.ModBlocks;
import net.eman3600.dndreams.initializers.world.ModPlacedFeatures;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.world.gen.GenerationStep;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeBuilder;
import org.betterx.bclib.interfaces.SurfaceMaterialProvider;

public class MarbleGardenBiome extends EndBiome.Config {

    public MarbleGardenBiome() {
        super("marble_garden");
    }

    @Override
    protected void addCustomBuildData(BCLBiomeBuilder builder) {
        builder
                .genChance(.15f)
                .feature(GenerationStep.Feature.VEGETAL_DECORATION, ModPlacedFeatures.PRISTINE_PLACED)
                .feature(GenerationStep.Feature.VEGETAL_DECORATION, ModPlacedFeatures.GOLDEN_GRASS)
                .spawn(EntityType.ENDERMAN, 10, 1, 2);
    }

    @Override
    protected SurfaceMaterialProvider surfaceMaterial() {
        return new EndBiome.DefaultSurfaceMaterialProvider() {
            @Override
            public BlockState getTopMaterial() {
                return ModBlocks.GOLDEN_GRASS_BLOCK.getDefaultState();
            }

            @Override
            public BlockState getUnderMaterial() {
                return ModBlocks.MARBLE.getDefaultState();
            }
        };
    }
}
