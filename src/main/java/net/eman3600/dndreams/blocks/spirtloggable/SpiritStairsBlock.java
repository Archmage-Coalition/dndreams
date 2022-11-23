package net.eman3600.dndreams.blocks.spirtloggable;

import net.eman3600.dndreams.blocks.properties.ModProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;

public class SpiritStairsBlock extends StairsBlock implements Spiritloggable {
    public SpiritStairsBlock(BlockState baseBlockState, Settings settings) {
        super(baseBlockState, settings);
        setDefaultState(Spiritloggable.unlogDefaultState(this));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(ModProperties.SPIRITLOGGED, ModProperties.SORROWLOGGED);
    }


    @Override
    public FluidState getFluidState(BlockState state) {
        return Spiritloggable.getFluidState(state);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return Spiritloggable.getFluidloggedState(super.getPlacementState(ctx), ctx);
    }
}
