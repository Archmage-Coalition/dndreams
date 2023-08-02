package net.eman3600.dndreams.blocks.crop;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class ShadeWeedBlock extends ShadePlantBlock {

    private static final VoxelShape SHAPE = Block.createCuboidShape(0, 0.0, 0, 16, 6, 16);

    public ShadeWeedBlock(Settings settings) {
        super(settings);
    }


    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }
}
