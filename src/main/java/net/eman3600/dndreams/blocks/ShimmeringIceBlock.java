package net.eman3600.dndreams.blocks;

import net.eman3600.dndreams.initializers.basics.ModFluids;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ShimmeringIceBlock extends TransparentBlock {
    public ShimmeringIceBlock(Settings settings) {
        super(settings);
    }

    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
        super.afterBreak(world, player, pos, state, blockEntity, stack);
        if (EnchantmentHelper.getLevel(Enchantments.SILK_TOUCH, stack) == 0 && !(world.getDimension().ultrawarm() && meltResult(pos, world) == ModFluids.FLOWING_SPIRIT_BLOCK)) {
            Material material = world.getBlockState(pos.down()).getMaterial();
            if (material.blocksMovement() || material.isLiquid()) {
                world.setBlockState(pos, meltResult(pos, world).getDefaultState());
            }
        }

    }

    private Block meltResult(BlockPos pos, World world) {
        return (world.getFluidState(pos.down()).isIn(FluidTags.WATER) || world.getFluidState(pos.north()).isIn(FluidTags.WATER) || world.getFluidState(pos.east()).isIn(FluidTags.WATER) || world.getFluidState(pos.south()).isIn(FluidTags.WATER) || world.getFluidState(pos.west()).isIn(FluidTags.WATER)) ? Blocks.WATER : ModFluids.FLOWING_SPIRIT_BLOCK;
    }

    public PistonBehavior getPistonBehavior(BlockState state) {
        return PistonBehavior.NORMAL;
    }
}
