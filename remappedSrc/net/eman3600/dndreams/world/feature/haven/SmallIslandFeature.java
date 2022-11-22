package net.eman3600.dndreams.world.feature.haven;

import net.eman3600.dndreams.initializers.basics.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;
import org.betterx.bclib.api.v2.levelgen.features.features.DefaultFeature;
import org.betterx.bclib.util.BlocksHelper;
import org.betterx.bclib.util.MHelper;

public class SmallIslandFeature extends DefaultFeature {
	@Override
	public boolean generate(FeatureContext<DefaultFeatureConfig> featurePlaceContext) {
		StructureWorldAccess generator = featurePlaceContext.getWorld();
		BlockPos center = featurePlaceContext.getOrigin();
		Random random = featurePlaceContext.getRandom();
		
		if (getYOnSurface(generator, center.getX(), center.getZ()) > 0) {
			return false;
		}
		
		int size = MHelper.randRange(5, 9, random);
		
		BlockPos.Mutable pos = center.mutableCopy();
		if (center.getY() == 0) {
			pos.setY(MHelper.randRange(64, 192, random));
		}
		makeCircle((StructureWorldAccess) generator, pos, ModBlocks.HAVEN_GRASS_BLOCK.getDefaultState(), size - 1, random);
		
		pos.setY(pos.getY() - 1);
		makeCircle((StructureWorldAccess) generator, pos, ModBlocks.HAVEN_GRASS_BLOCK.getDefaultState(), size, random);
		
		pos.setY(pos.getY() - 1);
		makeCircle((StructureWorldAccess) generator, pos, ModBlocks.HAVEN_DIRT.getDefaultState(), size - 1, random);
		
		pos.setY(pos.getY() - 1);
		makeCircle((StructureWorldAccess) generator, pos, ModBlocks.HAVEN_STONE.getDefaultState(), size - 2, random);
		
		if (size > 5) {
			pos.setY(pos.getY() - 1);
			makeCircle((StructureWorldAccess) generator, pos, ModBlocks.HAVEN_STONE.getDefaultState(), size - 4, random);
		}
		
		return true;
	}
	
	private void makeCircle(StructureWorldAccess level, BlockPos pos, BlockState state, int radius, Random random) {
		BlockPos.Mutable mut = pos.mutableCopy();
		int r2 = radius * radius;
		for (int x = -radius; x <= radius; x++) {
			int x2 = x * x;
			mut.setX(pos.getX() + x);
			for (int z = -radius; z <= radius; z++) {
				int z2 = z * z;
				mut.setZ(pos.getZ() + z);
				BlockState worldState = level.getBlockState(mut);
				if (x2 + z2 <= r2 - random.nextInt(radius)) {
					if (worldState.isAir() || worldState.getMaterial().isReplaceable()) {
						BlockState setState = state;
						if (state.isOf(ModBlocks.HAVEN_GRASS_BLOCK) && !level.getBlockState(mut.up()).isAir()) {
							setState = ModBlocks.HAVEN_DIRT.getDefaultState();
						}
						BlocksHelper.setWithoutUpdate(level, mut, setState);
					}
				}
			}
		}
	}
}
