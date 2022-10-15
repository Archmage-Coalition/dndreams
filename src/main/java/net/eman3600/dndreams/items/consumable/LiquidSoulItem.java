package net.eman3600.dndreams.items.consumable;

import net.eman3600.dndreams.initializers.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

public class LiquidSoulItem extends Item {
    public LiquidSoulItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        ItemStack stack = context.getStack();
        BlockPos pos = context.getBlockPos();
        BlockState state = context.getWorld().getBlockState(pos);

        if (state.getBlock() == ModBlocks.CHARGED_DEEPSLATE && !context.getWorld().isClient) {
            context.getPlayer().setStackInHand(context.getHand(), ItemUsage.exchangeStack(stack, context.getPlayer(), new ItemStack(Items.GLASS_BOTTLE)));

            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }
}
