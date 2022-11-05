package net.eman3600.dndreams.items.consumable;

import net.minecraft.block.Block;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.random.Random;

import java.util.ArrayList;
import java.util.List;

public class MutandisExtremisItem extends MutandisItem {
    public static final List<Block> extremeMutables = new ArrayList<>();

    public MutandisExtremisItem(Settings settings) {
        super(settings);
    }

    public static void registerMutable(Block block) {
        MutandisOneirosItem.registerMutable(block);

        if (extremeMutables.contains(block)) return;
        extremeMutables.add(block);
    }


    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        Block block = context.getWorld().getBlockState(context.getBlockPos()).getBlock();

        if (extremeMutables.contains(block)) {
            Random random = context.getWorld().random;
            if (context.getWorld() instanceof ServerWorld world) {
                Block result = extremeMutables.get(random.nextInt(extremeMutables.size()));

                mutate(world, context.getBlockPos(), result.getDefaultState());
            } else {
                mutateClient(context.getWorld(), context.getBlockPos());
            }

            if (context.getPlayer() != null && !context.getPlayer().isCreative()) context.getStack().decrement(1);
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }
}
