package net.eman3600.dndreams.blocks.spirtloggable;

import net.eman3600.dndreams.blocks.properties.ModProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.enums.SlabType;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

public class SpiritSlabBlock extends SlabBlock implements Spiritloggable {
    public SpiritSlabBlock(Settings settings) {
        super(settings);
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
    public boolean tryFillWithFluid(WorldAccess world, BlockPos pos, BlockState state, FluidState fluidState) {
        if (state.get(TYPE) != SlabType.DOUBLE) {
            return Spiritloggable.super.tryFillWithFluid(world, pos, state, fluidState);
        }
        return false;
    }

    @Override
    public boolean canFillWithFluid(BlockView world, BlockPos pos, BlockState state, Fluid fluid) {
        if (state.get(TYPE) != SlabType.DOUBLE) {
            return Spiritloggable.super.canFillWithFluid(world, pos, state, fluid);
        }
        return false;
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
