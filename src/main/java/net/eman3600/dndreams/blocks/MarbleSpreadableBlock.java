package net.eman3600.dndreams.blocks;

import net.eman3600.dndreams.initializers.basics.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldView;

public class MarbleSpreadableBlock extends Block {

    public MarbleSpreadableBlock(Settings settings) {
        super(settings);
    }

    private static boolean canSurvive(BlockState state, WorldView world, BlockPos pos) {
        BlockPos blockPos = pos.up();
        BlockState blockState = world.getBlockState(blockPos);
        return blockState.getFluidState().getLevel() != 8 && !blockState.isOpaque();
    }

    private static boolean canSpread(BlockState state, WorldView world, BlockPos pos) {
        BlockPos blockPos = pos.up();
        return canSurvive(state, world, pos) && !world.getFluidState(blockPos).isIn(FluidTags.WATER);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!canSurvive(state, world, pos)) {
            world.setBlockState(pos, ModBlocks.MARBLE.getDefaultState());
            return;
        }

        BlockState blockState = this.getDefaultState();
        for (int i = 0; i < 4; ++i) {
            BlockPos blockPos = pos.add(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
            if (!world.getBlockState(blockPos).isOf(ModBlocks.MARBLE) || !canSpread(blockState, world, blockPos)) continue;
            world.setBlockState(blockPos, blockState);
        }
    }
}
