package net.eman3600.dndreams.blocks.redstone;

import net.eman3600.dndreams.initializers.basics.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CosmicFocusBlock extends Block {

    public CosmicFocusBlock(Settings settings) {
        super(settings);
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {

        return world.getBlockState(pos.up()).isOf(ModBlocks.COSMIC_EGG) ? 15 : 0;
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, notify);

        world.updateNeighbors(pos, this);
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }
}
