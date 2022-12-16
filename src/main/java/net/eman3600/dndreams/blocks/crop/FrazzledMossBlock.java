package net.eman3600.dndreams.blocks.crop;

import net.eman3600.dndreams.initializers.basics.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

import java.util.ArrayList;
import java.util.List;

public class FrazzledMossBlock extends PlantBlock {
    public static final VoxelShape SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 11.0, 16.0);

    public FrazzledMossBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.isOpaqueFullCube(world, pos);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        List<BlockPos> available = new ArrayList<>();

        for (int x = -5; x <= 5; x++) {
            for (int y = -3; y <= 3; y++) {
                for (int z = -5; z <= 5; z++) {
                    BlockPos pos2 = pos.add(x, y, z);

                    if (world.getBlockState(pos2).isOf(ModBlocks.EMBER_MOSS) || world.getBlockState(pos2).isIn(BlockTags.FIRE)) {
                        available.add(pos2);
                    }
                }
            }
        }

        if (available.size() > 0) {
            world.setBlockState(available.get(random.nextInt(available.size())), ModBlocks.FRAZZLED_MOSS.getDefaultState(), Block.NOTIFY_LISTENERS);
        } else {
            world.breakBlock(pos, false);
        }
    }
}
