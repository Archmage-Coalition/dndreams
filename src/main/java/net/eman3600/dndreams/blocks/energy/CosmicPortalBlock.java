package net.eman3600.dndreams.blocks.energy;

import net.eman3600.dndreams.blocks.entities.CosmicPortalBlockEntity;
import net.eman3600.dndreams.initializers.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.EndPortalBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class CosmicPortalBlock extends EndPortalBlock {
    public CosmicPortalBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);

        if (stack.getItem() == Items.GLASS_BOTTLE) {
            player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(ModItems.LIQUID_VOID)));
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    public void open(WorldAccess world, BlockPos pos) {
        int remaining = 5;

        open(world, pos.north(), remaining);
        open(world, pos.east(), remaining);
        open(world, pos.south(), remaining);
        open(world, pos.west(), remaining);
    }

    private void open(WorldAccess world, BlockPos pos, int remaining) {
        if (remaining == 0) {
            return;
        }

        if (world.isAir(pos) && !world.isOutOfHeightLimit(pos)) {
            world.setBlockState(pos, this.getDefaultState(), NOTIFY_ALL);
            remaining--;

            open(world, pos.north(), remaining);
            open(world, pos.east(), remaining);
            open(world, pos.south(), remaining);
            open(world, pos.west(), remaining);
        }
    }

    public void scatterBreak(WorldAccess world, BlockPos pos) {
        int remaining = 5;

        scatterBreak(world, pos.north(), remaining);
        scatterBreak(world, pos.east(), remaining);
        scatterBreak(world, pos.south(), remaining);
        scatterBreak(world, pos.west(), remaining);
    }

    private void scatterBreak(WorldAccess world, BlockPos pos, int remaining) {
        if (remaining == 0) {
            return;
        }

        if (world.getBlockState(pos).getBlock() == this) {
            world.removeBlock(pos, false);
            remaining--;

            scatterBreak(world, pos.north(), remaining);
            scatterBreak(world, pos.east(), remaining);
            scatterBreak(world, pos.south(), remaining);
            scatterBreak(world, pos.west(), remaining);
        }
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CosmicPortalBlockEntity(pos, state);
    }
}
