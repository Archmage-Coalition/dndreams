package net.eman3600.dndreams.blocks;

import net.eman3600.dndreams.cardinal_components.BloodMoonComponent;
import net.eman3600.dndreams.initializers.cca.WorldComponents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.OreBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class VitalOreBlock extends OreBlock {
    public static final BooleanProperty REVEALED = BooleanProperty.of("revealed");

    public VitalOreBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(REVEALED, false));
    }

    public VitalOreBlock(Settings settings, IntProvider experience) {
        super(settings, experience);
        setDefaultState(getDefaultState().with(REVEALED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(REVEALED);
    }

    @Override
    public void onBlockBreakStart(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        boolean revealed = state.get(REVEALED);
        if (!revealed) {
            BlockState newState = state.with(REVEALED, true);
            world.setBlockState(pos, newState);
            world.updateListeners(pos, state, newState, Block.NOTIFY_ALL);
        }

        super.onBlockBreakStart(state, world, pos, player);
    }

    @Override
    public void onStacksDropped(BlockState state, ServerWorld world, BlockPos pos, ItemStack stack, boolean dropExperience) {
        super.onStacksDropped(state, world, pos, stack, dropExperience && state.get(REVEALED));
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        BloodMoonComponent moon = WorldComponents.BLOOD_MOON.get(world);
        boolean revealed = state.get(REVEALED);

        if (revealed != moon.isBloodMoon()) {
            BlockState newState = state.with(REVEALED, !revealed);
            world.setBlockState(pos, newState);
            world.updateListeners(pos, state, newState, Block.NOTIFY_ALL);
        }
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(REVEALED, true);
    }
}
