package net.eman3600.dndreams.items.consumable;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.TagKey;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.Registry;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MutandisItem extends Item {
    public static final List<Block> mutables = new ArrayList<>();

    public MutandisItem(Settings settings) {
        super(settings);
    }

    public static void registerMutable(Block block) {
        mutables.add(block);
    }


    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        Block block = context.getWorld().getBlockState(context.getBlockPos()).getBlock();

        if (mutables.contains(block)) {
            Random random = context.getWorld().random;
            if (context.getWorld() instanceof ServerWorld world) {
                Block result = mutables.get(random.nextInt(mutables.size()));

                world.setBlockState(context.getBlockPos(), result.getDefaultState());
            }

            if (context.getPlayer() != null && !context.getPlayer().isCreative()) context.getStack().decrement(1);
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }
}
