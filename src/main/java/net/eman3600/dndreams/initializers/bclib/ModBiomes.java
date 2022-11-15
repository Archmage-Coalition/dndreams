package net.eman3600.dndreams.initializers.bclib;

import net.eman3600.dndreams.world.biome.end.EndBiome;
import net.eman3600.dndreams.world.biome.end.EosBiome;
import net.eman3600.dndreams.world.biome.end.HeliosBiome;
import net.eman3600.dndreams.world.biome.end.SeleneBiome;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import org.betterx.bclib.api.v2.LifeCycleAPI;
import org.betterx.bclib.api.v2.generator.BiomeType;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeRegistry;
import org.betterx.bclib.api.v2.levelgen.biomes.BiomeAPI;
import org.betterx.bclib.config.Configs;

import java.util.LinkedList;
import java.util.List;

import static net.eman3600.dndreams.Initializer.MODID;

public class ModBiomes {
    public static final List<EndBiome> ALL_BE_END_BIOMES = new LinkedList<>();

    public static final EndBiome SELENE_BIOME = registerBiome(new SeleneBiome(), BiomeType.LAND);
    public static final EndBiome EOS_BIOME = registerBiome(new EosBiome(), BiomeType.LAND);
    public static final EndBiome HELIOS_BIOME = registerBiome(new HeliosBiome(), BiomeType.LAND);


    public static EndBiome registerBiome(EndBiome.Config biomeConfig, BiomeType type) {
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
    }

    private static void onWorldLoad(ServerWorld level, long seed, Registry<Biome> registry) {

    }
}
