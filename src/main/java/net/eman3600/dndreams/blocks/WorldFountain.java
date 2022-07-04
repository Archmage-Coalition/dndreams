package net.eman3600.dndreams.blocks;

import net.eman3600.dndreams.initializers.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class WorldFountain extends Block {
    public static final BooleanProperty FUNCTIONAL = BooleanProperty.of("functional");
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

    public WorldFountain(Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)this.getDefaultState()
                .with(FUNCTIONAL, false).with(FACING, Direction.EAST));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState)this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FUNCTIONAL, FACING);
    }
}
