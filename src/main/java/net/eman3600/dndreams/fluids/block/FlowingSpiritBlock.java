package net.eman3600.dndreams.fluids.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.FlowableFluid;

public class FlowingSpiritBlock extends FluidBlock {
    public FlowingSpiritBlock(FlowableFluid fluid, Settings settings) {
        super(fluid, settings);
    }

    protected void appendProperties(net.minecraft.state.StateManager.Builder<Block, BlockState> builder) {
        builder.add(LEVEL);
    }
}
