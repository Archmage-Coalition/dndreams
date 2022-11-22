package net.eman3600.dndreams.world.biome;

import com.google.common.collect.Lists;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.betterx.bclib.util.MHelper;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class TerrainGenerator {
	private static final float[] COEF;
	private static final Point[] OFFS;
	
	private final IslandLayer largeIslands;
	private final IslandLayer mediumIslands;
	private final IslandLayer smallIslands;
	
	public TerrainGenerator(long seed) {
		Random random = new Random(seed);
		largeIslands = new IslandLayer(random.nextInt(), GeneratorOptions.bigOptions);
		mediumIslands = new IslandLayer(random.nextInt(), GeneratorOptions.mediumOptions);
		smallIslands = new IslandLayer(random.nextInt(), GeneratorOptions.smallOptions);
	}
	
	public void fillTerrainDensity(double[] buffer, BlockPos pos, double scaleXZ, double scaleY, float[] floatBuffer) {
		fillTerrainDensity(floatBuffer, pos, scaleXZ, scaleY);
		for (short i = 0; i < buffer.length; i++) {
			buffer[i] = floatBuffer[i];
		}
	}

	public void fillTerrainDensity(float[] buffer, BlockPos pos, double scaleXZ, double scaleY) {
		largeIslands.clearCache();
		mediumIslands.clearCache();
		smallIslands.clearCache();
		
		int x = MathHelper.floor(pos.getX() / scaleXZ);
		int z = MathHelper.floor(pos.getZ() / scaleXZ);
		double px = x * scaleXZ;
		double pz = z * scaleXZ;
		
		largeIslands.updatePositions(px, pz);
		mediumIslands.updatePositions(px, pz);
		smallIslands.updatePositions(px, pz);
		
		for (int y = 0; y < buffer.length; y++) {
			double py = y * scaleY + pos.getY();
			float dist = largeIslands.getDensity(px, py, pz);
			if (dist < 0.3F) {
				dist = MHelper.max(dist, mediumIslands.getDensity(px, py, pz));
			}
			if (dist < 0.3F) {
				dist = MHelper.max(dist, smallIslands.getDensity(px, py, pz));
			}
			buffer[y] = dist;
		}
	}
	
	public float getTerrainDensity(int x, int y, int z) {
		largeIslands.clearCache();
		mediumIslands.clearCache();
		smallIslands.clearCache();
		
		largeIslands.updatePositions(x, z);
		mediumIslands.updatePositions(x, z);
		smallIslands.updatePositions(x, z);
		
		float dist = largeIslands.getDensity(x, y, z);
		if (dist < 0.3F) {
			dist = MHelper.max(dist, mediumIslands.getDensity(x, y, z));
		}
		if (dist < 0.3F) {
			dist = MHelper.max(dist, smallIslands.getDensity(x, y, z));
		}
		return dist;
	}
	
	static {
		float sum = 0;
		List<Float> coef = Lists.newArrayList();
		List<Point> pos = Lists.newArrayList();
		for (int x = -3; x <= 3; x++) {
			for (int z = -3; z <= 3; z++) {
				float dist = MHelper.length(x, z) / 3F;
				if (dist <= 1) {
					sum += dist;
					coef.add(dist);
					pos.add(new Point(x, z));
				}
			}
		}
		OFFS = pos.toArray(new Point[] {});
		COEF = new float[coef.size()];
		for (int i = 0; i < COEF.length; i++) {
			COEF[i] = coef.get(i) / sum;
		}
	}
}
