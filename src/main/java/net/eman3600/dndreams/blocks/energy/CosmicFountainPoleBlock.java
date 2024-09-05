package net.eman3600.dndreams.blocks.energy;

import net.eman3600.dndreams.blocks.entities.CosmicFountainPoleBlockEntity;
import net.eman3600.dndreams.blocks.portal.CosmicPortalBlock;
import net.eman3600.dndreams.initializers.basics.ModBlockEntities;
import net.eman3600.dndreams.initializers.basics.ModBlocks;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class CosmicFountainPoleBlock extends BlockWithEntity {
    public static final BooleanProperty FUNCTIONAL = BooleanProperty.of("functional");
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;


    public CosmicFountainPoleBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite()).with(FUNCTIONAL, false);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FUNCTIONAL, FACING);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CosmicFountainPoleBlockEntity(pos, state);
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        if (state.get(FUNCTIONAL)) {
            ((CosmicPortalBlock) ModBlocks.COSMIC_PORTAL).scatterBreak(world, pos);
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : checkType(type, ModBlockEntities.COSMIC_FOUNTAIN_POLE_ENTITY, CosmicFountainPoleBlockEntity::tick);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
