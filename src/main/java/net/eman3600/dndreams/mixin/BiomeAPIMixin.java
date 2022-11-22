package net.eman3600.dndreams.mixin;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiome;
import org.betterx.bclib.api.v2.levelgen.biomes.BiomeAPI;
import org.checkerframework.common.reflection.qual.Invoke;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BiomeAPI.class)
public interface BiomeAPIMixin {
    @Invoker("registerBiome")
    static BCLBiome invokeRegisterBiome(BCLBiome bclbiome, Registry<Biome> registryOrNull) {
        return null;
    }
}
