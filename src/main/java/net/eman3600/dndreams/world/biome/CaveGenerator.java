package net.eman3600.dndreams.world.biome;

import net.eman3600.dndreams.initializers.basics.ModBlocks;
import net.eman3600.dndreams.world.gen.noise.InterpolationCell;
import net.eman3600.dndreams.world.gen.noise.VoronoiNoise;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkProvider;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.storage.ChunkDataAccess;
import org.betterx.bclib.interfaces.ChunkGeneratorAccessor;
import org.betterx.bclib.noise.OpenSimplexNoise;
import org.betterx.bclib.util.BlocksHelper;
import org.betterx.bclib.util.MHelper;

import java.util.Random;

public class CaveGenerator {
	private static final BlockState CAVE_AIR = Blocks.CAVE_AIR.getDefaultState();
	private static final VoronoiNoise VORONOI_NOISE = new VoronoiNoise();
	private static OpenSimplexNoise simplexNoise;
	private static int seed;
	
	public static void init(long seed) {
		Random random = new Random(seed);
		simplexNoise = new OpenSimplexNoise(random.nextInt());
		CaveGenerator.seed = random.nextInt();
	}
	
	public static void carve(Chunk chunkAccess, InterpolationCell cellTerrain) {
		short minX = (short) chunkAccess.getPos().getStartX();
		short minZ = (short) chunkAccess.getPos().getStartZ();
		short minY = (short) chunkAccess.getBottomY();
		short maxY = (short) chunkAccess.getHeight();
		
		final float[] buffer9 = new float[9];
		final float[] buffer27 = new float[27];
		
		Random random = new Random();
		final int maxCell = (maxY - minY) / 8 + 1;
		final BlockPos origin = new BlockPos(minX, minY, minZ);
		TerrainGenerator generator = MultiThreadGenerator.getTerrainGenerator();
		InterpolationCell cellSparse = new InterpolationCell(generator, 4, (maxY - minY) / 16 + 1, 16, 16, new BlockPos(minX - 16, minY, minZ - 16));
		InterpolationCell cellVoronoi = new InterpolationCell(p -> getTunelNoise(p, buffer27, random), 5, (maxY - minY) / 4 + 1, 4, 4, origin);
		InterpolationCell cellBigCave = new InterpolationCell(p -> getBigCaveNoise(cellSparse, p), 3, maxCell, 8, 8, origin);
		InterpolationCell cellPillars = new InterpolationCell(p -> getPillars(p, seed, buffer9, random), 5, (maxY - minY) / 4 + 1, 4, 4, origin);
		
		BlockPos.Mutable pos = new BlockPos.Mutable();
		
		short newMinY = (short) MHelper.min(cellTerrain.getMinY(), cellSparse.getMinY());
		minY = (short) MHelper.max(minY, newMinY);
		int maxCheck = maxY - 16;
		
		for (byte x = 0; x < 16; x++) {
			pos.setX(x);
			for (byte z = 0; z < 16; z++) {
				pos.setZ(z);
				byte index = 0;
				float[] accumulation = new float[8];
				short max = (short) chunkAccess.getHeight();
				for (short y = max; y >= minY; y--) {
					if (y < maxCheck) {
						float heightNoise = cellTerrain.get(pos.setY(y + 10), true);
						if (heightNoise <= 0) {
							continue;
						}
						if (y > 8) {
							heightNoise = cellTerrain.get(pos.setY(y - 8), true);
							if (heightNoise <= 0) {
								continue;
							}
						}
					}
					
					ChunkSection section = chunkAccess.getSection(y >> 4);
					pos.setY(y);
					
					if (y < maxCheck) {
						float heightNoise = cellTerrain.get(pos.setY(y + 10), true);
						if (heightNoise <= 0) {
							continue;
						}
						if (y > 8) {
							heightNoise = cellTerrain.get(pos.setY(y - 8), true);
							if (heightNoise <= 0) {
								continue;
							}
						}
					}
					
					if (section.getBlockState(x, y & 15, z).isOf(ModBlocks.HAVEN_STONE)) {
						float noise = cellVoronoi.get(pos, true);
						accumulation[index] = noise;
						index = (byte) ((index + 1) & 7);
						
						float average = 0;
						for (byte i = 0; i < accumulation.length; i++) {
							noise = MHelper.max(noise, accumulation[i]);
							average += accumulation[i];
						}
						noise = (noise + (average / accumulation.length)) * 0.5F - 0.9F;
						
						float cellValue = cellTerrain.get(pos, true);
						noise = -smoothUnion(-noise, cellValue + 0.5F, 1.1F);
						
						float bigCave = 0;
						if (noise < 0.1F) {
							bigCave = cellBigCave.get(pos, true);
							noise = -smoothUnion(-noise, -bigCave, 0.1F);
						}
						
						if (noise > -0.1F) {
							float pillars = cellPillars.get(pos, true);
							noise = smoothUnion(noise, pillars, 0.1F);
						}
						
						if (noise > 0) {
							section.setBlockState(x, y & 15, z, CAVE_AIR, false);
							int py = pos.getY();
							pos.setY(py + 1);
						}
					}
				}
			}
		}
	}
	
	private static float getTunelNoise(BlockPos pos, float[] buffer, Random random) {
		VORONOI_NOISE.getDistances(seed, pos.getX() * 0.01, pos.getY() * 0.03, pos.getZ() * 0.01, buffer, random);
		return buffer[0] / buffer[2];
	}
	
	private static float smoothUnion(float a, float b, float radius) {
		float h = MathHelper.clamp(0.5F + 0.5F * (b - a) / radius, 0F, 1F);
		return MathHelper.lerp(h, b, a) - radius * h * (1F - h);
	}
	
	private static float getMinValue(InterpolationCell cell, BlockPos pos) {
		float value = 1;
		for (Direction dir : BlocksHelper.DIRECTIONS) {
			float side = cell.get(pos.offset(dir, 15), false);
			value = MHelper.min(value, side);
		}
		return value;
	}
	
	private static float getBigCaveNoise(InterpolationCell cell, BlockPos pos) {
		if (pos.getY() < 32 || pos.getY() > 224) {
			return 0;
		}
		
		float noise = (float) simplexNoise.eval(pos.getX() * 0.03, pos.getY() * 0.03, pos.getZ() * 0.03);
		
		float value = getMinValue(cell, pos);
		value = MHelper.max(value, getMinValue(cell, pos.up(8)));
		value = MHelper.max(value, getMinValue(cell, pos.down(8)));
		if (noise < 0) {
			value += noise;
		}
		
		noise = (float) simplexNoise.eval(pos.getX() * 0.1, pos.getY() * 0.1, pos.getZ() * 0.1) * 0.004F + 0.004F;
		noise += (float) simplexNoise.eval(pos.getX() * 0.03, pos.getY() * 0.03, pos.getZ() * 0.03) * 0.01F + 0.01F;
		
		return value - noise;
	}
	
	private static float getPillars(BlockPos pos, int seed, float[] buffer, Random random) {
		VORONOI_NOISE.getDistances(seed, pos.getX() * 0.02, pos.getZ() * 0.02, buffer, random);
		float value = VORONOI_NOISE.getValue(seed, pos.getX() * 0.02, pos.getZ() * 0.02, random);
		value = buffer[0] - 0.07F * (value * 0.5F + 0.5F);
		return value + (float) simplexNoise.eval(pos.getX() * 0.03, pos.getY() * 0.03, pos.getZ() * 0.03) * 0.01F;
	}
}
