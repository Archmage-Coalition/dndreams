package net.eman3600.dndreams.mixin;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;
import net.eman3600.dndreams.initializers.ModBlocks;
import net.eman3600.dndreams.util.ModTags;
import net.minecraft.block.*;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FluidBlock.class)
public abstract class FluidBlockMixin extends Block implements FluidDrainable {
    @Shadow @Final protected FlowableFluid fluid;

    @Shadow @Final public static ImmutableList<Direction> FLOW_DIRECTIONS;

    @Shadow protected abstract void playExtinguishSound(WorldAccess world, BlockPos pos);

    public FluidBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "receiveNeighborFluids", at = @At("HEAD"), cancellable = true)
    private void dndreams$receieveNeighborFluids(World world, BlockPos pos, BlockState state, CallbackInfoReturnable<Boolean> cir) {

        if (this.fluid.isIn(FluidTags.WATER)) {
            UnmodifiableIterator var5 = FLOW_DIRECTIONS.iterator();

            while(var5.hasNext()) {
                Direction direction = (Direction)var5.next();
                BlockPos blockPos = pos.offset(direction.getOpposite());
                if (world.getFluidState(blockPos).isIn(ModTags.FLOWING_SPIRIT)) {
                    Block block = ModBlocks.SHIMMERING_ICE;
                    world.setBlockState(pos, block.getDefaultState());
                    this.playExtinguishSound(world, pos);
                    cir.setReturnValue(false);
                } else if (world.getFluidState(blockPos).isIn(ModTags.SORROW)) {
                    Block block = Blocks.ICE;
                    world.setBlockState(pos, block.getDefaultState());
                    this.playExtinguishSound(world, pos);
                    cir.setReturnValue(false);
                }
            }
        } else if (this.fluid.isIn(FluidTags.LAVA)) {
            UnmodifiableIterator var5 = FLOW_DIRECTIONS.iterator();

            while(var5.hasNext()) {
                Direction direction = (Direction)var5.next();
                BlockPos blockPos = pos.offset(direction.getOpposite());
                if (world.getFluidState(blockPos).isIn(ModTags.FLOWING_SPIRIT)) {
                    Block block = ModBlocks.SHIMMERING_STONE;
                    world.setBlockState(pos, block.getDefaultState());
                    this.playExtinguishSound(world, pos);
                    cir.setReturnValue(false);
                } else if (world.getFluidState(blockPos).isIn(ModTags.SORROW)) {
                    Block block = Blocks.BLACKSTONE;
                    world.setBlockState(pos, block.getDefaultState());
                    this.playExtinguishSound(world, pos);
                    cir.setReturnValue(false);
                }
            }
        } else if (this.fluid.isIn(ModTags.FLOWING_SPIRIT)) {
            UnmodifiableIterator var5 = FLOW_DIRECTIONS.iterator();

            while(var5.hasNext()) {
                Direction direction = (Direction)var5.next();
                BlockPos blockPos = pos.offset(direction.getOpposite());
                if (world.getFluidState(blockPos).isIn(FluidTags.WATER)) {
                    Block block = ModBlocks.SHIMMERING_ICE;
                    world.setBlockState(pos, block.getDefaultState());
                    this.playExtinguishSound(world, pos);
                    cir.setReturnValue(false);
                } else if (world.getFluidState(blockPos).isIn(FluidTags.LAVA)) {
                    Block block = ModBlocks.SHIMMERING_STONE;
                    world.setBlockState(pos, block.getDefaultState());
                    this.playExtinguishSound(world, pos);
                    cir.setReturnValue(false);
                } else if (world.getFluidState(blockPos).isIn(ModTags.SORROW)) {
                    Block block = ModBlocks.SHIMMERING_ICE;
                    world.setBlockState(pos, block.getDefaultState());
                    this.playExtinguishSound(world, pos);
                    cir.setReturnValue(false);
                }
            }
        } else if (this.fluid.isIn(ModTags.SORROW)) {
            UnmodifiableIterator var5 = FLOW_DIRECTIONS.iterator();

            while(var5.hasNext()) {
                Direction direction = (Direction)var5.next();
                BlockPos blockPos = pos.offset(direction.getOpposite());
                if (world.getFluidState(blockPos).isIn(FluidTags.WATER)) {
                    Block block = Blocks.ICE;
                    world.setBlockState(pos, block.getDefaultState());
                    this.playExtinguishSound(world, pos);
                    cir.setReturnValue(false);
                } else if (world.getFluidState(blockPos).isIn(FluidTags.LAVA)) {
                    Block block = Blocks.BLACKSTONE;
                    world.setBlockState(pos, block.getDefaultState());
                    this.playExtinguishSound(world, pos);
                    cir.setReturnValue(false);
                } else if (world.getFluidState(blockPos).isIn(ModTags.FLOWING_SPIRIT)) {
                    Block block = ModBlocks.SHIMMERING_ICE;
                    world.setBlockState(pos, block.getDefaultState());
                    this.playExtinguishSound(world, pos);
                    cir.setReturnValue(false);
                }
            }
        }
    }
}
