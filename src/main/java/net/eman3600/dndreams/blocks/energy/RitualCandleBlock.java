package net.eman3600.dndreams.blocks.energy;

import net.eman3600.dndreams.initializers.ModBlocks;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class RitualCandleBlock extends Block {
    private static final VoxelShape CANDLE_SHAPE = Block.createCuboidShape(4.5, 0.0, 4.5, 11.5, 13.0, 11.5);
    private final ParticleEffect particle;
    private static final Vec3d OFFSET = new Vec3d(0.5, .87f, 0.5);

    public static final BooleanProperty LIT = Properties.LIT;

    public RitualCandleBlock(ParticleEffect particle, Settings settings) {
        super(settings);
        this.particle = particle;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return CANDLE_SHAPE;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (!state.get(LIT)) {
            return;
        }

        spawnCandleParticles(world, OFFSET.add(pos.getX(), pos.getY(), pos.getZ()), random);
    }

    /*@Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);

        if (!state.get(LIT) && !world.isClient && (stack.getItem() instanceof FlintAndSteelItem || stack.getItem() instanceof FlintAndHellsteelItem)) {
            world.setBlockState(pos, state.with(LIT, true));
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }*/

    public static boolean canBeLit(BlockState state) {
        if (state.getBlock() instanceof RitualCandleBlock block && block != ModBlocks.ECHO_CANDLE) {
            return !state.get(LIT);
        }
        return false;
    }

    public static int luminence(BlockState state) {
        return state.get(LIT) ? 12 : 0;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(LIT, false);
    }

    private void spawnCandleParticles(World world, Vec3d vec3d, Random random) {
        vec3d = vec3d.add(random.nextTriangular(0, 0.05f),0, random.nextTriangular(0, 0.05f));

        float f = random.nextFloat();
        if (f < 0.3f) {
            world.addParticle(ParticleTypes.SMOKE, vec3d.x, vec3d.y, vec3d.z, 0.0, 0.0, 0.0);
            if (f < 0.17f) {
                world.playSound(vec3d.x + 0.5, vec3d.y + 0.5, vec3d.z + 0.5, SoundEvents.BLOCK_CANDLE_AMBIENT, SoundCategory.BLOCKS, 1.0f + random.nextFloat(), random.nextFloat() * 0.7f + 0.3f, false);
            }
        }
        world.addParticle(particle, vec3d.x, vec3d.y, vec3d.z, 0.0, 0.0, 0.0);
    }
}
