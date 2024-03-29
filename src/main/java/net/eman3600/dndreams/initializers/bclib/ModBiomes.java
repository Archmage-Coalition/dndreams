package net.eman3600.dndreams.initializers.bclib;

import com.google.common.collect.Lists;
import net.eman3600.dndreams.mixin.BiomeAPIMixin;
import net.eman3600.dndreams.world.biome.end.*;
import net.eman3600.dndreams.world.biome.haven.HavenCaveBiomes;
import net.eman3600.dndreams.world.biome.haven.HavenLandBiomes;
import net.eman3600.dndreams.world.biome.haven.HavenVoidBiomes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import org.betterx.bclib.api.v2.LifeCycleAPI;
import org.betterx.bclib.api.v2.generator.BiomeType;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiome;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeRegistry;
import org.betterx.bclib.api.v2.levelgen.biomes.BiomeAPI;
import org.betterx.bclib.config.Configs;
import org.betterx.bclib.config.EntryConfig;
import org.betterx.bclib.config.IdConfig;

import java.util.LinkedList;
import java.util.List;

import static net.eman3600.dndreams.Initializer.MODID;

/**
 * Credits to paulevs & Quiqueck for all the code for end generation
 * and most of the code for haven generation.
 * */
public class ModBiomes {
    private static final IdConfig CONFIG = new EntryConfig(MODID, "biomes");
    public static final List<EndBiome> ALL_BE_END_BIOMES = new LinkedList<>();
    public static final List<BCLBiome> BIOMES_LAND = Lists.newArrayList();
    public static final List<BCLBiome> BIOMES_VOID = Lists.newArrayList();
    public static final List<BCLBiome> BIOMES_CAVE = Lists.newArrayList();

    // End Biomes
    public static final EndBiome MARBLE_GARDEN_BIOME = registerEndBiome(new MarbleGardenBiome(), BiomeType.LAND);


    // Haven Land Biomes
    public static final BCLBiome HAVEN_FOREST = registerLand(HavenLandBiomes.havenForestBiome());


    // Haven Void Biomes
    public static final BCLBiome HAVEN_SKY = registerVoid(HavenVoidBiomes.havenSkyBiome());


    // Haven Cave Biomes
    public static final BCLBiome HAVEN_EMPTY_CAVE = registerCave(HavenCaveBiomes.havenEmptyCave());



    public static EndBiome registerEndBiome(EndBiome.Config biomeConfig, BiomeType type) {
        final EndBiome biome = EndBiome.create(biomeConfig);
        if (Configs.BIOMES_CONFIG.getBoolean(biome.getID().getPath(), "enabled", true)) {
            if (type == BiomeType.LAND) {
                BiomeAPI.registerEndLandBiome(biome);
            } else {
                BiomeAPI.registerEndVoidBiome(biome);
            }
            ALL_BE_END_BIOMES.add(biome);
        }
        return biome;
    }

    public static void registerBiomes() {
        BCLBiomeRegistry.registerBiomeCodec(new Identifier(MODID, "biome"), EndBiome.KEY_CODEC);

        LifeCycleAPI.onLevelLoad(ModBiomes::onWorldLoad);

        CONFIG.saveChanges();
    }

    private static void onWorldLoad(ServerWorld level, long seed, Registry<Biome> registry) {

    }

    private static BCLBiome registerLand(BCLBiome biome) {
        BIOMES_LAND.add(biome);
        return BiomeAPIMixin.invokeRegisterBiome(biome, BuiltinRegistries.BIOME);
    }

    private static BCLBiome registerVoid(BCLBiome biome) {
        BIOMES_VOID.add(biome);
        return BiomeAPIMixin.invokeRegisterBiome(biome, BuiltinRegistries.BIOME);
    }

    private static BCLBiome registerCave(BCLBiome biome) {
        BIOMES_CAVE.add(biome);
        return BiomeAPIMixin.invokeRegisterBiome(biome, BuiltinRegistries.BIOME);
    }

    private static BCLBiome registerSubLand(BCLBiome parent, BCLBiome biome) {
        return BiomeAPI.registerSubBiome(parent, biome);
    }
}
