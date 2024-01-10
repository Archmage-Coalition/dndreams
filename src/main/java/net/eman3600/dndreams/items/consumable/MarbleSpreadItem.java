package net.eman3600.dndreams.items.consumable;

import net.eman3600.dndreams.initializers.basics.ModBlocks;
import net.eman3600.dndreams.items.TooltipItem;
import net.eman3600.dndreams.util.ModTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class MarbleSpreadItem extends TooltipItem {

    private static final int MAX_TIMES = 6;

    public MarbleSpreadItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {

        BlockPos pos = context.getBlockPos();
        World world = context.getWorld();
        BlockState state = world.getBlockState(pos);

        if (state.isIn(ModTags.END_STONES) || state.isOf(ModBlocks.CELESTIUM_ORE)) {

            if (!world.isClient) {
                ItemStack stack = context.getStack();
                stack.decrement(1);
                spreadMarble(world, pos, state, MAX_TIMES);
            }
            return ActionResult.SUCCESS;
        }

        return super.useOnBlock(context);
    }

    private void spreadMarble(World world, BlockPos pos, BlockState state, int rounds) {

        if (world.random.nextInt(MAX_TIMES) < rounds--) {

            world.setBlockState(pos, state.isOf(ModBlocks.CELESTIUM_ORE) ? ModBlocks.CELESTIUM_MARBLE_ORE.getDefaultState() : ModBlocks.MARBLE.getDefaultState(), Block.NOTIFY_ALL);
            if (rounds > 0) for (Direction dir : Direction.values()) {

                BlockPos pos2 = pos.offset(dir);
                BlockState state2 = world.getBlockState(pos2);

                if (state2.isIn(ModTags.END_STONES) || state2.isOf(ModBlocks.CELESTIUM_ORE)) {
                    spreadMarble(world, pos2, state2, rounds);
                }
            }
        } else if (rounds > 0) {

            spreadMarble(world, pos, state, rounds);
        }

    }
}
