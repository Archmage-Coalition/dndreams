package net.eman3600.dndreams.blocks.crop;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.eman3600.dndreams.initializers.basics.ModItems;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Optional;

public class EmberMossBlock extends PlantBlock {
    public static final VoxelShape SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 11.0, 16.0);
    public static final int REACH = 5;
    public static final int LIMIT = 8;
    public static final int CRAMMING_CAP = 3;

    public EmberMossBlock(Settings settings) {
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

        if (overloaded(state, world, pos) && random.nextInt(10) == 0) {
            world.setBlockState(pos, AbstractFireBlock.getState(world, pos));
            return;
        }

        int i = LIMIT;
        for (BlockPos blockPos : BlockPos.iterate(pos.add(-REACH, -1, -REACH), pos.add(REACH, 1, REACH))) {
            if (!world.getBlockState(blockPos).isOf(this) || --i > 0) continue;
            world.setBlockState(pos, AbstractFireBlock.getState(world, pos));
            return;
        }

        if (random.nextInt(LIMIT) > LIMIT - i) return;

        BlockPos blockPos2 = pos.add(random.nextInt(5) - 2, random.nextInt(2) - random.nextInt(2), random.nextInt(5) - 2);
        for (int k = 0; k < 7; ++k) {
            if (world.isAir(blockPos2) && state.canPlaceAt(world, blockPos2)) {
                break;
            }
            blockPos2 = pos.add(random.nextInt(5) - 2, random.nextInt(2) - random.nextInt(2), random.nextInt(5) - 2);
        }
        if (world.isAir(blockPos2) && state.canPlaceAt(world, blockPos2) && !overloaded(state, world, blockPos2)) {
            world.setBlockState(blockPos2, state, Block.NOTIFY_LISTENERS);
        }

    }

    public boolean overloaded(BlockState state, World world, BlockPos pos) {
        int count = 0;

        for (BlockPos blockPos : BlockPos.iterate(pos.add(-1, -1, -1), pos.add(1, 1, 1))) {
            if (!world.getBlockState(blockPos).isOf(this) || ++count < CRAMMING_CAP) continue;
            return true;
        }

        return false;
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity) {
            Optional<TrinketComponent> trinketOptional = TrinketsApi.getTrinketComponent((LivingEntity) entity);
            if (trinketOptional.isPresent() && trinketOptional.get().isEquipped(ModItems.FLAME_CAPE)) return;
            if (!entity.isFireImmune()) {
                entity.setFireTicks(entity.getFireTicks() + 2);
                if (entity.getFireTicks() <= 0) {
                    entity.setOnFireFor(3);
                }
            }
            entity.damage(DamageSource.IN_FIRE, 1);
        }
        super.onEntityCollision(state, world, pos, entity);
    }
}
