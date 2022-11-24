package net.eman3600.dndreams.blocks.spirtloggable;

import net.eman3600.dndreams.blocks.properties.ModProperties;
import net.eman3600.dndreams.initializers.basics.ModFluids;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Waterloggable;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

import java.util.HashMap;
import java.util.Map;

public interface Spiritloggable extends Waterloggable {
    Map<Fluid, BooleanProperty> FLUID_PROPERTIES = new HashMap<>();

    static void registerFluidProperties() {
        FLUID_PROPERTIES.put(Fluids.WATER, Properties.WATERLOGGED);
        FLUID_PROPERTIES.put(ModFluids.STILL_FLOWING_SPIRIT, ModProperties.SPIRITLOGGED);
        FLUID_PROPERTIES.put(ModFluids.STILL_SORROW, ModProperties.SORROWLOGGED);
    }

    @Override
    default boolean canFillWithFluid(BlockView world, BlockPos pos, BlockState state, Fluid fluid) {
        return !isSpiritLogged(state) && FLUID_PROPERTIES.containsKey(fluid);
    }


    @Override
    default boolean tryFillWithFluid(WorldAccess world, BlockPos pos, BlockState state, FluidState fluidState) {
        Fluid fluid = fluidState.getFluid();
        BooleanProperty property = FLUID_PROPERTIES.get(fluid);
        if (property == null) return false;

        if (canFillWithFluid(world, pos, state, fluid) && !state.get(property)) {
            if (!world.isClient()) {
                world.setBlockState(pos, state.with(property, true), Block.NOTIFY_ALL);
                world.createAndScheduleFluidTick(pos, fluid, fluid.getTickRate(world));
            }
            return true;
        }
        return false;
    }

    @Override
    default ItemStack tryDrainFluid(WorldAccess world, BlockPos pos, BlockState state) {
        for (Fluid fluid: FLUID_PROPERTIES.keySet()) {
            BooleanProperty property = FLUID_PROPERTIES.get(fluid);
            if (state.get(property)) {
                world.setBlockState(pos, state.with(property, false), Block.NOTIFY_ALL);
                if (!state.canPlaceAt(world, pos)) {
                    world.breakBlock(pos, true);
                }
                return new ItemStack(fluid.getBucketItem());
            }
        }
        return ItemStack.EMPTY;
    }

    static FluidState getFluidState(BlockState state) {
        if (state.get(Properties.WATERLOGGED)) {
            return Fluids.WATER.getStill(false);
        } else if (state.get(ModProperties.SPIRITLOGGED)) {
            return ModFluids.STILL_FLOWING_SPIRIT.getStill(false);
        } else if (state.get(ModProperties.SORROWLOGGED)) {
            return ModFluids.STILL_SORROW.getStill(false);
        }
        return Fluids.EMPTY.getDefaultState();
    }

    static BlockState getFluidloggedState(BlockState state, ItemPlacementContext ctx) {
        BlockPos blockPos = ctx.getBlockPos();
        FluidState fluidState = ctx.getWorld().getFluidState(blockPos);
        Fluid fluid = fluidState.getFluid();
        if (FLUID_PROPERTIES.containsKey(fluid)) {
            return state.with(FLUID_PROPERTIES.get(fluid), true);
        }

        return state;
    }

    static BlockState unlogDefaultState(Block block) {
        BlockState state = block.getDefaultState();
        return state.with(Properties.WATERLOGGED, false).with(ModProperties.SORROWLOGGED, false).with(ModProperties.SPIRITLOGGED, false);
    }

    default boolean isSpiritLogged(BlockState state) {
        for (BooleanProperty property: FLUID_PROPERTIES.values()) {
            if (state.get(property)) return true;
        }
        return false;
    }

    default Fluid getSpiritLogger(BlockState state) {
        for (Fluid fluid: FLUID_PROPERTIES.keySet()) {
            if (state.get(FLUID_PROPERTIES.get(fluid))) {
                return fluid;
            }
        }
        return null;
    }

}
