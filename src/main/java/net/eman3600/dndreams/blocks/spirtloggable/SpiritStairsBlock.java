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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

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

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        Fluid fluid = getSpiritLogger(state);
        if (fluid != null) {
            world.createAndScheduleFluidTick(pos, fluid, fluid.getTickRate(world));
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }
}
