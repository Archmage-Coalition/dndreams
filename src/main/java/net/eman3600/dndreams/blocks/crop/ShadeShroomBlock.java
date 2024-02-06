package net.eman3600.dndreams.blocks.crop;

import net.eman3600.dndreams.initializers.basics.ModBlocks;
import net.eman3600.dndreams.world.feature.tree.sapling_generator.ShadeSaplingGenerator;
import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class ShadeShroomBlock extends PlantBlock implements Fertilizable {

    private static final ShadeSaplingGenerator GENERATOR = new ShadeSaplingGenerator();
    private static final VoxelShape SHAPE = Block.createCuboidShape(1, 0.0, 1, 15.0, 14.0, 15.0);

    public ShadeShroomBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.isOf(ModBlocks.SHADE_MOSS) || super.canPlantOnTop(floor, world, pos);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.empty();
    }

    @Override
    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        return world.getFluidState(pos.up()).isEmpty();
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return (double)world.random.nextFloat() < 0.45;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        GENERATOR.generate(world, world.getChunkManager().getChunkGenerator(), pos, state, random);
    }
}
