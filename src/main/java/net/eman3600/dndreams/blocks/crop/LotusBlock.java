package net.eman3600.dndreams.blocks.crop;

import net.eman3600.dndreams.blocks.properties.ModIntProperty;
import net.eman3600.dndreams.initializers.basics.ModFluids;
import net.eman3600.dndreams.initializers.basics.ModItems;
import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemConvertible;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class LotusBlock extends CropBlock {
    public static final IntProperty AGE = new ModIntProperty("age", 0, 6);
    private static final VoxelShape[] AGE_TO_SHAPE = new VoxelShape[]{
            Block.createCuboidShape(4.0, 0.0, 4.0, 12.0, 1.5, 12.0),
            Block.createCuboidShape(3.0, 0.0, 3.0, 13.0, 1.5, 13.0),
            Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 1.5, 14.0),
            Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 1.5, 15.0),
            Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 4.5, 15.0),
            Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 5.5, 15.0),
            Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 7.5, 15.0)};



    public LotusBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected ItemConvertible getSeedsItem() {
        return ModItems.LOTUS_SEEDS;
    }

    @Override
    public int getGrowthAmount(World world) {
        return MathHelper.nextInt(world.random, 2, 5);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return AGE_TO_SHAPE[state.get(getAgeProperty())];
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (state.get(getAgeProperty()) == 0) {
            return VoxelShapes.empty();
        } else if (state.get(getAgeProperty()) > 3) {
            return AGE_TO_SHAPE[3];
        }

        return super.getCollisionShape(state, world, pos, context);
    }

    @Override
    public IntProperty getAgeProperty() {
        return AGE;
    }

    public int getMaxAge() {
        return 6;
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        FluidState fluidState = world.getFluidState(pos);
        FluidState fluidState2 = world.getFluidState(pos.up());
        return (fluidState.getFluid() == Fluids.WATER || fluidState.getFluid() == ModFluids.STILL_FLOWING_SPIRIT || floor.getMaterial() == Material.ICE) && fluidState2.getFluid() == Fluids.EMPTY;
    }

    protected float getMoisture(BlockView world, BlockPos pos) {
        float f = 1.0F;
        BlockPos blockPos = pos.down();
        Block block = this;

        for(int i = -1; i <= 1; ++i) {
            for(int j = -1; j <= 1; ++j) {
                float g = 0.0F;
                BlockState blockState = world.getBlockState(blockPos.add(i, 0, j));
                if (blockState.isOf(Blocks.WATER) || blockState.isOf(ModFluids.FLOWING_SPIRIT_BLOCK)) {
                    g = 3.0F;
                }

                if (i != 0 || j != 0) {
                    g /= 4.0F;
                }

                f += g;
            }
        }

        BlockPos blockPos2 = pos.north();
        BlockPos blockPos3 = pos.south();
        BlockPos blockPos4 = pos.west();
        BlockPos blockPos5 = pos.east();
        boolean bl = world.getBlockState(blockPos4).isOf(block) || world.getBlockState(blockPos5).isOf(block);
        boolean bl2 = world.getBlockState(blockPos2).isOf(block) || world.getBlockState(blockPos3).isOf(block);
        if (bl && bl2) {
            f /= 2.0F;
        } else {
            boolean bl3 = world.getBlockState(blockPos4.north()).isOf(block) || world.getBlockState(blockPos5.north()).isOf(block) || world.getBlockState(blockPos5.south()).isOf(block) || world.getBlockState(blockPos4.south()).isOf(block);
            if (bl3) {
                f /= 2.0F;
            }
        }

        return f;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (world.getBaseLightLevel(pos, 0) >= 9) {
            int i = this.getAge(state);
            if (i < this.getMaxAge()) {
                float f = getMoisture(world, pos);
                if (random.nextInt((int)(25.0F / f) + 1) == 0) {
                    world.setBlockState(pos, this.withAge(i + 1), 2);
                }
            }
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }
}
