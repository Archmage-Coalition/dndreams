package net.eman3600.dndreams.fluids.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Property;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;

import java.util.List;

public class FlowingSpiritBlock extends FluidBlock {
    public FlowingSpiritBlock(FlowableFluid fluid, Settings settings) {
        super(fluid, settings);
    }

    protected void appendProperties(net.minecraft.state.StateManager.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{LEVEL});
    }
}
