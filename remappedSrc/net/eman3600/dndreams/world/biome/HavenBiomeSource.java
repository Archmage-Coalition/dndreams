package net.eman3600.dndreams.world.biome;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.eman3600.dndreams.initializers.bclib.ModBiomes;
import net.eman3600.dndreams.world.gen.noise.InterpolationCell;
import net.minecraft.util.dynamic.RegistryOps;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.profiler.Sampler;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import org.betterx.bclib.api.v2.generator.BiomePicker;
import org.betterx.bclib.api.v2.generator.map.hex.HexBiomeMap;
import org.betterx.bclib.interfaces.BiomeMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.eman3600.dndreams.Initializer.MODID;

public class HavenBiomeSource extends BiomeSource {
	public static final Codec<HavenBiomeSource> CODEC = RecordCodecBuilder.create(
		(instance) -> instance.group(
			RegistryOps.createRegistryCodec(Registry.BIOME_KEY).forGetter((source) -> null)
		).apply(instance, instance.stable(HavenBiomeSource::new))
	);

	private Map<ChunkPos, InterpolationCell> terrainCache = new ConcurrentHashMap<>();
	private BiomePicker pickerLand;
	private BiomePicker pickerVoid;
	private BiomePicker pickerCave;
	private BiomeMap mapLand;
	private BiomeMap mapVoid;
	private BiomeMap mapCave;

	public static List<RegistryEntry<Biome>> fromRegistry(Registry<Biome> registry) {
		List<RegistryEntry<Biome>> list = new ArrayList<>();
		Set<Map.Entry<RegistryKey<Biome>, Biome>> set = registry.getEntrySet();

		for (Map.Entry<RegistryKey<Biome>, Biome> pair: set) {
			if (pair.getKey().getValue().getNamespace().equals(MODID)) {
				list.add(registry.getOrCreateEntry(pair.getKey()));
			}
		}

		return list;
	}

	public HavenBiomeSource(Registry<Biome> biomeRegistry) {
		super(fromRegistry(biomeRegistry));
		
		if (pickerLand == null) {
			pickerLand = new BiomePicker(biomeRegistry);
			ModBiomes.BIOMES_LAND.forEach(biome -> pickerLand.addBiome(biome));
			pickerLand.rebuild();
			
			pickerVoid = new BiomePicker(biomeRegistry);
			ModBiomes.BIOMES_VOID.forEach(biome -> pickerVoid.addBiome(biome));
			pickerVoid.rebuild();
			
			pickerCave = new BiomePicker(biomeRegistry);
			ModBiomes.BIOMES_CAVE.forEach(biome -> pickerCave.addBiome(biome));
			pickerCave.rebuild();
		}
		
		mapLand = new HexBiomeMap(0, GeneratorOptions.biomeSizeLand, pickerLand);
		mapVoid = new HexBiomeMap(0, GeneratorOptions.biomeSizeVoid, pickerVoid);
		mapCave = new HexBiomeMap(0, GeneratorOptions.biomeSizeCave, pickerCave);
	}

	@Override
	protected Codec<? extends BiomeSource> getCodec() {
		return CODEC;
	}
	
	@Override
	public RegistryEntry<Biome> getBiome(int x, int y, int z, MultiNoiseUtil.MultiNoiseSampler sampler) {
		cleanCache(x, z);
		
		int px = (x << 2) | 2;
		int py = (y << 2) | 2;
		int pz = (z << 2) | 2;
		
		ChunkPos chunkPos = new ChunkPos(px >> 4, pz >> 4);
		InterpolationCell cell = terrainCache.get(chunkPos);
		if (cell == null) {
			TerrainGenerator generator = MultiThreadGenerator.getTerrainGenerator();
			cell = new InterpolationCell(generator, 3, 33, 8, 8, new BlockPos(chunkPos.getStartX(), 0, chunkPos.getStartZ()));
			terrainCache.put(chunkPos, cell);
		}
		BlockPos.Mutable pos = new BlockPos.Mutable(px, 0, pz);
		
		if (isLand(cell, pos)) {
			if (isCave(cell, pos.setY(py))) {
				return mapCave.getBiome(px, 0, pz).biome;
			}
			return mapLand.getBiome(px, 0, pz).biome;
		}
		return mapVoid.getBiome(px, 0, pz).biome;
	}
	
	public void setSeed(long seed) {
		mapLand = new HexBiomeMap(seed, GeneratorOptions.biomeSizeLand, pickerLand);
		mapVoid = new HexBiomeMap(seed, GeneratorOptions.biomeSizeVoid, pickerVoid);
		mapCave = new HexBiomeMap(seed, GeneratorOptions.biomeSizeCave, pickerCave);
	}
	
	private void cleanCache(int x, int z) {
		if ((x & 63) == 0 && (z & 63) == 0) {
			terrainCache.clear();
			mapLand.clearCache();
			mapVoid.clearCache();
			mapCave.clearCache();
		}
	}
	
	private boolean isLand(InterpolationCell cell, BlockPos.Mutable pos) {
		for (short py = 0; py < 256; py += 8) {
			if (cell.get(pos.setY(py), false) > -0.05F) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isCave(InterpolationCell cell, BlockPos.Mutable pos) {
		if (pos.getY() < 8 || pos.getY() > 240) return false;
		boolean v1 = cell.get(pos, false) > 0.0F;
		boolean v2 = cell.get(pos.setY(pos.getY() + 12), false) > 0.0F;
		boolean v3 = cell.get(pos.setY(pos.getY() - 16), false) > 0.0F;
		return v1 && v2 && v3;
	}
}
