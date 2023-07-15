package net.eman3600.dndreams.blocks.spreadable;

import net.eman3600.dndreams.blocks.entities.AbstractReplacementBlockEntity;
import net.eman3600.dndreams.blocks.entities.MadMossSourceBlockEntity;
import net.eman3600.dndreams.initializers.basics.ModBlockEntities;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class MadMossSourceBlock extends BlockWithEntity {

    public MadMossSourceBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity entity = world.getBlockEntity(pos);

            if (entity instanceof AbstractReplacementBlockEntity replacementBlockEntity && newState.isAir()) {
                Vec3d vec = Vec3d.ofCenter(pos);

                ItemScatterer.spawn(world, vec.x, vec.y, vec.z, replacementBlockEntity.getReplacedState().getBlock().getPickStack(world, pos, state));
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new MadMossSourceBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : checkType(type, ModBlockEntities.MAD_MOSS_SOURCE_ENTITY, MadMossSourceBlockEntity::tick);
    }
}
