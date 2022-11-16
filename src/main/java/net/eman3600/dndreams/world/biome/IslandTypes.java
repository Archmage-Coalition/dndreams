package net.eman3600.dndreams.world.biome;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import org.betterx.bclib.noise.OpenSimplexNoise;
import org.betterx.bclib.sdf.SDF;
import org.betterx.bclib.sdf.operator.*;
import org.betterx.bclib.sdf.primitive.SDFCappedCone;
import org.betterx.bclib.sdf.primitive.SDFSphere;
import org.betterx.bclib.util.MHelper;
import org.betterx.bclib.util.WeightedList;

import java.util.function.BiFunction;

public class IslandTypes {
	private static final WeightedList<BiFunction<LayerOptions, Random, SDF>> ISLAND_FUNCTIONS;
	
	public static SDF getIsland(LayerOptions options, Random random) {
		return ISLAND_FUNCTIONS.get(random).apply(options, random);
	}
	
	private static SDF makeCone(float radiusBottom, float radiusTop, float height, float minY) {
		float hh = height * 0.5F;
		SDF sdf = new SDFCappedCone().setHeight(hh).setRadius1(radiusBottom).setRadius2(radiusTop);
		return new SDFTranslate().setTranslate(0, minY + hh, 0).setSource(sdf);
	}
	
	private static BiFunction<LayerOptions, Random, SDF> makeSimpleIsland() {
		SDF cone1 = makeCone(0, 0.4F, 0.2F, -0.3F);
		SDF cone2 = makeCone(0.4F, 0.5F, 0.1F, -0.1F);
		SDF cone3 = makeCone(0.5F, 0.45F, 0.03F, 0.0F);
		SDF cone4 = makeCone(0.45F, 0, 0.02F, 0.03F);
		
		SDF coneBottom = new SDFSmoothUnion().setRadius(0.02F).setSourceA(cone1).setSourceB(cone2);
		SDF coneTop = new SDFSmoothUnion().setRadius(0.02F).setSourceA(cone3).setSourceB(cone4);
		final SDF defaultIsland = new SDFSmoothUnion().setRadius(0.01F).setSourceA(coneTop).setSourceB(coneBottom);
		
		return (options, random) -> {
			final OpenSimplexNoise noise1 = new OpenSimplexNoise(random.nextInt());
			final OpenSimplexNoise noise2 = new OpenSimplexNoise(random.nextInt());
			final float scale1 = options.scale * 0.0125F;
			final float scale2 = options.scale * 0.025F;
			final float scale3 = options.scale * 0.05F;
			final float scale4 = MathHelper.clamp((options.scale - 50) / 50F, 0.3F, 1.0F) / options.scale;
			
			final float islandScale = random.nextFloat() + 0.5F;
			
			SDF island = new SDFScale().setScale(islandScale).setSource(defaultIsland);
			island = new SDFCoordModify().setFunction(pos -> {
				float x1 = pos.getX() * scale1;
				float z1 = pos.getZ() * scale1;
				float x2 = pos.getX() * scale2;
				float z2 = pos.getZ() * scale2;
				float x3 = pos.getX() * scale3;
				float z3 = pos.getZ() * scale3;
				
				float dx = (float) noise1.eval(x1, z1) * 20 + (float) noise2.eval(x2, z2) * 10;
				float dy = (float) noise1.eval(x3, z3) *  6 + (float) noise2.eval(x3, z3) *  3;
				float dz = (float) noise2.eval(x1, z1) * 20 + (float) noise1.eval(x2, z2) * 10;
				//float scaleY = 1.0F - MHelper.length(pos.x(), pos.z()) / islandScale;
				
				pos.set(pos.getX() + dx * scale4, pos.getY() + dy * scale4/* * scaleY*/, pos.getZ() + dz * scale4);
			}).setSource(island);
			
			return island;
		};
	}
	
	private static BiFunction<LayerOptions, Random, SDF> makeTallSphereIsland() {
		SDF sphere = new SDFSphere().setRadius(1.0F);
		sphere = new SDFScale3D().setScale(0.25F, 1.0F, 0.25F).setSource(sphere);
		final SDF defaultIsland = sphere;
		
		return (options, random) -> {
			float scale = random.nextFloat() + 0.5F;
			
			if (options.scale > 60) {
				scale = 60 * scale / options.scale;
			}
			else if (options.scale < 30) {
				scale = 30 * scale / options.scale;
			}
			
			SDF island = new SDFScale().setScale(scale).setSource(defaultIsland);
			
			if (options.scale > 35) {
				float distance = scale * 0.6F;
				float offset = random.nextFloat() * MHelper.PI2;
				byte count = (byte) MHelper.randRange(3, 5, random);
				for (byte i = 0; i < count; i++) {
					float angle = (float) i / count * MHelper.PI2 + offset;
					float px = (float) Math.sin(angle) * distance;
					float pz = (float) Math.cos(angle) * distance;
					SDF part = new SDFScale().setScale(scale * MHelper.randRange(0.3F, 0.6F, random)).setSource(defaultIsland);
					part = new SDFTranslate().setTranslate(px, MHelper.randRange(-0.25F, 0.25F, random) * distance, pz).setSource(part);
					island = new SDFUnion().setSourceA(island).setSourceB(part);
				}
			}
			
			final OpenSimplexNoise noise1 = new OpenSimplexNoise(random.nextInt());
			final float scale1 = 0.5F * options.scale;
			final float noiseScale = MathHelper.clamp((options.scale - 30) / 30F, 0.1F, 1.0F);
			final float scale2 = 20F / options.scale * noiseScale;
			
			island = new SDFDisplacement()
				.setFunction(pos -> (float) noise1.eval(pos.getX() * scale1, pos.getY() * scale1, pos.getZ() * scale1) * scale2)
				.setSource(island);
			
			return island;
		};
	}
	
	private static BiFunction<LayerOptions, Random, SDF> makeDoubleConeIsland() {
		SDF cone1 = makeCone(0, 0.4F, 0.2F, -0.3F);
		SDF cone2 = makeCone(0.4F, 0.5F, 0.1F, -0.1F);
		SDF cone3 = makeCone(0.5F, 0.45F, 0.03F, 0.0F);
		SDF cone4 = makeCone(0.45F, 0, 0.02F, 0.03F);
		SDF cone5 = makeCone(0.35F, 0.12F, 0.15F, 0.03F);
		SDF cone6 = makeCone(0.12F, 0.0F, 0.15F, 0.18F);
		
		SDF coneBottom = new SDFSmoothUnion().setRadius(0.02F).setSourceA(cone1).setSourceB(cone2);
		SDF coneTop = new SDFSmoothUnion().setRadius(0.02F).setSourceA(cone3).setSourceB(cone4);
		final SDF merged = new SDFSmoothUnion().setRadius(0.02F).setSourceA(coneTop).setSourceB(coneBottom);
		final SDF mountain = new SDFSmoothUnion().setRadius(0.02F).setSourceA(cone5).setSourceB(cone6);
		
		return (options, random) -> {
			final OpenSimplexNoise noise1 = new OpenSimplexNoise(random.nextInt());
			final OpenSimplexNoise noise2 = new OpenSimplexNoise(random.nextInt());
			final float scale1 = options.scale * 0.0125F;
			final float scale2 = options.scale * 0.025F;
			final float scale3 = options.scale * 0.05F;
			final float scale4 = options.scale * 0.1F;
			final float islandScale = random.nextFloat() + 0.5F;
			
			SDF islandBottom = new SDFScale().setScale(islandScale).setSource(merged);
			islandBottom = new SDFCoordModify().setFunction(pos -> {
				float x1 = pos.getX() * scale1;
				float z1 = pos.getZ() * scale1;
				float x2 = pos.getX() * scale2;
				float z2 = pos.getZ() * scale2;
				float x3 = pos.getX() * scale3;
				float z3 = pos.getZ() * scale3;
				
				float dx = (float) noise1.eval(x1, z1) * 20 + (float) noise2.eval(x2, z2) * 10;
				float dy = (float) noise1.eval(x3, z3) *  6 + (float) noise2.eval(x3, z3) *  3;
				float dz = (float) noise2.eval(x1, z1) * 20 + (float) noise1.eval(x2, z2) * 10;
				//float scaleY = 1.0F - MHelper.length(pos.x(), pos.z()) / islandScale;
				
				pos.set(pos.getX() + dx / options.scale, pos.getY() + dy/* * scaleY*/ / options.scale, pos.getZ() + dz / options.scale);
			}).setSource(islandBottom);
			
			SDF islandTop = new SDFScale().setScale(islandScale).setSource(mountain);
			islandTop = new SDFCoordModify().setFunction(pos -> {
				float x1 = pos.getX() * scale1;
				float z1 = pos.getZ() * scale1;
				float x2 = pos.getX() * scale2;
				float z2 = pos.getZ() * scale2;
				float x3 = pos.getX() * scale3;
				float z3 = pos.getZ() * scale3;
				float x4 = pos.getX()* scale4;
				float z4 = pos.getZ() * scale4;
				
				float dx = (float) noise1.eval(x1, z1) * 20 + (float) noise2.eval(x2, z2) * 10;
				float dy = (float) noise1.eval(x3, z3) * 15 + (float) noise2.eval(x3, z3) * 8 + (float) noise1.eval(x4, z4) * 3;
				float dz = (float) noise2.eval(x1, z1) * 20 + (float) noise1.eval(x2, z2) * 10;
				
				pos.set(pos.getX() + dx / options.scale, pos.getY() + dy / options.scale, pos.getZ() + dz / options.scale);
			}).setSource(islandTop);
			
			return new SDFSmoothUnion().setRadius(0.02F).setSourceA(islandBottom).setSourceB(islandTop);
		};
	}
	
	static {
		ISLAND_FUNCTIONS = new WeightedList<>();
		ISLAND_FUNCTIONS.add(makeSimpleIsland(), 2.0F);
		ISLAND_FUNCTIONS.add(makeTallSphereIsland(), 1.0F);
		ISLAND_FUNCTIONS.add(makeDoubleConeIsland(), 1.5F);
	}
}
