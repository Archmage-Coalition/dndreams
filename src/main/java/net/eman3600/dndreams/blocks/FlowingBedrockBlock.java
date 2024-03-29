package net.eman3600.dndreams.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.math.Direction;

public class FlowingBedrockBlock extends Block {
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

    public FlowingBedrockBlock(Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)this.getDefaultState()
                .with(FACING, Direction.EAST));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState)this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
