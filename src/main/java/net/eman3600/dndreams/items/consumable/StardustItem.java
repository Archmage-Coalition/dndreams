package net.eman3600.dndreams.items.consumable;

import net.eman3600.dndreams.initializers.basics.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class StardustItem extends Item {
    public StardustItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        ItemStack stack = context.getStack();
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        Direction dir = context.getSide();

        if ((world.getBlockState(pos).isOf(ModBlocks.HAVEN_GRASS_BLOCK) || world.getBlockState(pos).isOf(ModBlocks.HAVEN_DIRT)) && dir == Direction.UP) {
            world.setBlockState(pos, ModBlocks.STAR_GRASS_BLOCK.getDefaultState(), Block.NOTIFY_ALL);
            if (context.getPlayer() != null && !context.getPlayer().isCreative()) stack.decrement(1);
            return ActionResult.SUCCESS;
        }

        return super.useOnBlock(context);
    }
}
