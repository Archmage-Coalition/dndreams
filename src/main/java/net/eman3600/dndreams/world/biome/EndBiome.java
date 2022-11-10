package net.eman3600.dndreams.world.biome;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.KeyDispatchCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.eman3600.dndreams.initializers.basics.ModBlocks;
import net.eman3600.dndreams.initializers.bclib.EndFeatures;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.EndPlacedFeatures;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiome;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeBuilder;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeSettings;
import org.betterx.bclib.api.v2.levelgen.biomes.BiomeAPI;
import org.betterx.bclib.api.v2.levelgen.surface.SurfaceRuleBuilder;
import org.betterx.bclib.interfaces.SurfaceMaterialProvider;
import org.betterx.bclib.mixin.common.SurfaceRulesContextAccessor;
import org.betterx.bclib.util.WeightedList;

import java.util.List;
import java.util.Optional;

import static net.eman3600.dndreams.Initializer.MODID;

public class EndBiome extends BCLBiome implements SurfaceMaterialProvider {
    public static final Codec<EndBiome> CODEC = RecordCodecBuilder.create(instance ->
            codecWithSettings(
                    instance,
                    Codec.BOOL.fieldOf("has_caves").orElse(true).forGetter(o -> o.hasCaves),
                    SurfaceMaterialProvider.CODEC.fieldOf("surface")
                            .orElse(Config.DEFAULT_MATERIAL)
                            .forGetter(o -> o.surfaceProvider)
            ).apply(instance, EndBiome::new)
    );
    public static final CodecHolder<EndBiome> KEY_CODEC = CodecHolder.of(CODEC);

    public boolean hasCaves;
    public SurfaceMaterialProvider surfaceProvider;

    public EndBiome(float terrainHeight, float fogDensity, float genChance, int edgeSize, boolean vertical, Optional<Identifier> edge, Identifier biomeID, Optional<List<MultiNoiseUtil.NoiseHypercube>> parameterPoints, Optional<Identifier> biomeParent, Optional<WeightedList<Identifier>> subbiomes, Optional<String> intendedType, boolean hasCaves, SurfaceMaterialProvider surfaceProvider) {
        super(terrainHeight, fogDensity, genChance, edgeSize, vertical, edge, biomeID, parameterPoints, biomeParent, subbiomes, intendedType);
        this.hasCaves = hasCaves;
        this.surfaceProvider = surfaceProvider;
    }

    public EndBiome(Identifier identifier, Biome biome, BCLBiomeSettings bclBiomeSettings) {
        super(identifier, biome, bclBiomeSettings);
    }

    @Override
    public BlockState getTopMaterial() {
        return surfaceProvider.getTopMaterial();
    }

    @Override
    public BlockState getUnderMaterial() {
        return surfaceProvider.getUnderMaterial();
    }

    @Override
    public BlockState getAltTopMaterial() {
        return surfaceProvider.getAltTopMaterial();
    }

    @Override
    public boolean generateFloorRule() {
        return surfaceProvider.generateFloorRule();
    }

    @Override
    public SurfaceRuleBuilder surface() {
        return surfaceProvider.surface();
    }

    public static BlockState findTopMaterial(BCLBiome biome) {
        return BiomeAPI.findTopMaterial(biome).orElse(EndBiome.Config.DEFAULT_MATERIAL.getTopMaterial());
    }

    public static BlockState findTopMaterial(Biome biome) {
        return findTopMaterial(BiomeAPI.getBiome(biome));
    }

    public static BlockState findTopMaterial(WorldAccess world, BlockPos pos) {
        return findTopMaterial(BiomeAPI.getBiome(world.getBiome(pos)));
    }

    public static BlockState findUnderMaterial(BCLBiome biome) {
        return BiomeAPI.findUnderMaterial(biome).orElse(EndBiome.Config.DEFAULT_MATERIAL.getUnderMaterial());
    }

    public static BlockState findUnderMaterial(WorldAccess world, BlockPos pos) {
        return findUnderMaterial(BiomeAPI.getBiome(world.getBiome(pos)));
    }

    public static EndBiome create(Config biomeConfig) {
        BCLBiomeBuilder builder = BCLBiomeBuilder
                .start(biomeConfig.ID)
                .music(SoundEvents.MUSIC_END)
                .waterColor(4159204)
                .waterFogColor(329011)
                .fogColor(0xA080A0)
                .skyColor(0)
                .mood(SoundEvents.AMBIENT_WARPED_FOREST_MOOD)
                .temperature(0.5f)
                .wetness(0.5f)
                .precipitation(Biome.Precipitation.NONE)
                .surface(biomeConfig.surfaceMaterial().surface().build())
                .endLandBiome();

        biomeConfig.addCustomBuildData(builder);
        EndFeatures.addDefaultFeatures(biomeConfig.ID, builder, biomeConfig.hasCaves());

        if (biomeConfig.hasReturnGateway()) {
            builder.feature(GenerationStep.Feature.SURFACE_STRUCTURES, EndPlacedFeatures.END_GATEWAY_RETURN);
        }

        EndBiome biome = builder.build(biomeConfig.getSupplier());
        biome.hasCaves = biomeConfig.hasCaves();
        biome.surfaceProvider = biomeConfig.surfaceMaterial();

        return biome;
    }



    public static class DefaultSurfaceMaterialProvider implements SurfaceMaterialProvider {
        public static final BlockState END_STONE = Blocks.END_STONE.getDefaultState();

        @Override
        public BlockState getTopMaterial() {
            return getUnderMaterial();
        }

        @Override
        public BlockState getAltTopMaterial() {
            return getTopMaterial();
        }

        @Override
        public BlockState getUnderMaterial() {
            return END_STONE;
        }

        @Override
        public boolean generateFloorRule() {
            return true;
        }

        @Override
        public SurfaceRuleBuilder surface() {
            SurfaceRuleBuilder builder = SurfaceRuleBuilder.start();

            if (generateFloorRule() && getTopMaterial() != getUnderMaterial()) {
                if (getTopMaterial() != getAltTopMaterial()) {
                    builder.floor(getTopMaterial());
                } else {
                    builder.chancedFloor(getTopMaterial(), getAltTopMaterial());
                }
            }
            return builder.filler(getUnderMaterial());
        }
    }

    public abstract static class Config {
        public static final SurfaceMaterialProvider DEFAULT_MATERIAL = new DefaultSurfaceMaterialProvider();

        public static final MaterialRules.MaterialRule END_STONE = MaterialRules.block(DefaultSurfaceMaterialProvider.END_STONE);
        public static final MaterialRules.MaterialRule SELENE_GRASS = MaterialRules.block(ModBlocks.SELENE_GRASS_BLOCK.getDefaultState());
        public static final MaterialRules.MaterialRule EOS_GRASS = MaterialRules.block(ModBlocks.EOS_GRASS_BLOCK.getDefaultState());
        public static final MaterialRules.MaterialRule HELIOS_GRASS = MaterialRules.block(ModBlocks.HELIOS_GRASS_BLOCK.getDefaultState());

        public final Identifier ID;

        protected Config(String name) {
            this.ID = new Identifier(MODID, name);
        }

        protected abstract void addCustomBuildData(BCLBiomeBuilder builder);

        public BCLBiomeBuilder.BiomeSupplier<EndBiome> getSupplier() {
            return EndBiome::new;
        }

        protected boolean hasCaves() {
            return true;
        }

        protected boolean hasReturnGateway() {
            return true;
        }

        protected SurfaceMaterialProvider surfaceMaterial() {
            return DEFAULT_MATERIAL;
        }
    }
}
