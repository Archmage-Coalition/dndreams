package net.eman3600.dndreams.blocks.redstone;

import net.eman3600.dndreams.initializers.basics.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CosmicFocusBlock extends Block {

    public static final Property<Boolean> POWERED = Properties.POWERED;

    public CosmicFocusBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(POWERED, false));
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {

        return state.get(POWERED) ? 15 : 0;
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, notify);

        if (world.getBlockState(pos.up()).isOf(ModBlocks.COSMIC_EGG) != state.get(POWERED)) {
            world.setBlockState(pos, state.with(POWERED, !state.get(POWERED)), Block.NOTIFY_ALL);
        }
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }
}
