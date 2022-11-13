package net.eman3600.dndreams.blocks.crop;

import net.eman3600.dndreams.initializers.basics.ModItems;
import net.minecraft.block.*;
import net.minecraft.item.ItemConvertible;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class SnowbellBlock extends BeetrootsBlock {
    private static final VoxelShape[] AGE_TO_SHAPE = new VoxelShape[]{Block.createCuboidShape(6.0, 0.0, 6.0, 10.0, 5.0, 10.0), Block.createCuboidShape(4.0, 0.0, 4.0, 12.0, 7.0, 12.0), Block.createCuboidShape(2.0, 0.0, 2.0, 12.0, 11.0, 12.0), Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 13.0, 14.0)};


    public SnowbellBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected int getGrowthAmount(World world) {
        return MathHelper.nextInt(world.random, 1, 2);
    }

    @Override
    protected ItemConvertible getSeedsItem() {
        return ModItems.SNOWBELL_SEEDS;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return AGE_TO_SHAPE[state.get(getAgeProperty())];
    }
}
