package net.eman3600.dndreams.items.consumable;

import net.eman3600.dndreams.blocks.energy.BonfireBlock;
import net.eman3600.dndreams.initializers.basics.ModBlocks;
import net.eman3600.dndreams.items.TooltipItem;
import net.eman3600.dndreams.util.ModTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class NightmareFuelItem extends TooltipItem {

    public NightmareFuelItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        BlockState state = world.getBlockState(pos);
        ItemStack stack = context.getStack();

        if (state.getBlock() instanceof CampfireBlock && !state.get(Properties.WATERLOGGED)) {
            Direction dir = state.get(Properties.HORIZONTAL_FACING);

            world.setBlockState(pos, ModBlocks.BONFIRE.getDefaultState().with(Properties.HORIZONTAL_FACING, dir).with(BonfireBlock.SOUL, state.isIn(ModTags.CURSE_CAMPFIRES)), Block.NOTIFY_ALL);

            if (!context.getPlayer().isCreative()) stack.decrement(1);
            return ActionResult.SUCCESS;
        }


        return ActionResult.PASS;
    }
}
