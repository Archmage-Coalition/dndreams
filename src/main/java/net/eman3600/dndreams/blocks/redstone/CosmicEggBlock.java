package net.eman3600.dndreams.blocks.redstone;

import net.minecraft.block.*;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;

public class CosmicEggBlock extends FallingBlock {

    protected static final VoxelShape SHAPE = Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 16.0, 15.0);

    public CosmicEggBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) this.teleport(state, world, pos);
        return ActionResult.success(world.isClient);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (world.isClient) {
            return;
        }

        if (world.isReceivingRedstonePower(pos)) {

            teleport(state, world, pos);
        }
    }

    private void teleport(BlockState state, World world, BlockPos pos) {
        WorldBorder worldBorder = world.getWorldBorder();
        for (int i = 0; i < 1000; ++i) {
            BlockPos blockPos = pos.add(world.random.nextInt(16) - world.random.nextInt(16), world.random.nextInt(8) - world.random.nextInt(8), world.random.nextInt(16) - world.random.nextInt(16));
            if (!world.getBlockState(blockPos).isAir() || !worldBorder.contains(blockPos)) continue;
            world.setBlockState(blockPos, state, Block.NOTIFY_LISTENERS);
            world.removeBlock(pos, false);
            world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_SHULKER_TELEPORT, SoundCategory.BLOCKS, 1, .8f);
            return;
        }
    }

    @Override
    protected int getFallDelay() {
        return 5;
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }
}
