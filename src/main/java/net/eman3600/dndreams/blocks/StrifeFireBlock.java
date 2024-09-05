package net.eman3600.dndreams.blocks;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.eman3600.dndreams.mob_effects.ModStatusEffect;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.Optional;

public class StrifeFireBlock extends AbstractFireBlock {

    public static final int DURATION = 60;

    public StrifeFireBlock(Settings settings) {
        super(settings, 4);
    }

    @Override
    protected boolean isFlammable(BlockState state) {
        return true;
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        world.createAndScheduleBlockTick(pos, this, DURATION);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        world.createAndScheduleBlockTick(pos, this, DURATION);
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.scheduledTick(state, world, pos, random);

        world.setBlockState(pos, Blocks.AIR.getDefaultState(), StrifeFireBlock.NOTIFY_ALL);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        super.onEntityCollision(state, world, pos, entity);

        if (!entity.isFireImmune() && entity instanceof LivingEntity e) {
            Optional<TrinketComponent> trinketOptional = TrinketsApi.getTrinketComponent((LivingEntity) entity);
            if (trinketOptional.isPresent() && trinketOptional.get().isEquipped(ModItems.FLAME_CAPE)) {
                return;
            }
            e.addStatusEffect(new StatusEffectInstance(ModStatusEffects.HEARTBLEED, 140));
        }
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {

    }
}
