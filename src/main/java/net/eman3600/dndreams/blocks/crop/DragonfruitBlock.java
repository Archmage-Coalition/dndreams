package net.eman3600.dndreams.blocks.crop;

import net.eman3600.dndreams.initializers.basics.ModItems;
import net.minecraft.block.*;
import net.minecraft.item.ItemConvertible;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class DragonfruitBlock extends BeetrootsBlock {

    public static final BooleanProperty AGELESS = BooleanProperty.of("ageless");

    private static final VoxelShape[] AGE_TO_SHAPE = new VoxelShape[]{Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 6.0, 16.0), Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 8.0, 16.0), Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 10.0, 16.0), Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 12.0, 16.0)};

    public DragonfruitBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(AGELESS, false));
    }

    @Override
    protected int getGrowthAmount(World world) {
        return MathHelper.nextInt(world.random, 1, 2);
    }

    @Override
    protected ItemConvertible getSeedsItem() {
        return ModItems.DRAGONFRUIT_SEEDS;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return AGE_TO_SHAPE[state.get(getAgeProperty())];
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        float f;
        int i = this.getAge(state);
        boolean reverse = state.get(AGELESS);
        if (world.getBaseLightLevel(pos, 0) >= 9 && random.nextInt((int)(25.0f / (f = CropBlock.getAvailableMoisture(this, world, pos))) + 1) == 0) {
            int age = MathHelper.clamp(i + (reverse ? -1 : 1), 0, 3);
            if (age == 0) reverse = false;
            else if (age == 3) reverse = true;

            world.setBlockState(pos, this.withAge(age).with(AGELESS, reverse), Block.NOTIFY_LISTENERS);
        }
    }

    @Override
    public BlockState withAge(int age) {
        return super.withAge(age).with(AGELESS, age >= 3);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(AGELESS);
    }
}
