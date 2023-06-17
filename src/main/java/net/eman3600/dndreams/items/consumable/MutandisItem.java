package net.eman3600.dndreams.items.consumable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.StemBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MutandisItem extends Item {
    public static final HashMap<String, List<Block>> mutables = new HashMap<>();

    public MutandisItem(Settings settings) {
        super(settings);
    }

    public static void registerMutable(String category, Block block) {
        if (!mutables.containsKey(category)) {
            mutables.put(category, new ArrayList<>());
        }

        MutandisExtremisItem.registerMutable(block);

        if (mutables.get(category).contains(block)) return;
        mutables.get(category).add(block);
    }


    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        Block block = context.getWorld().getBlockState(context.getBlockPos()).getBlock();

        List<String> categories = new ArrayList<>();

        for (String category: mutables.keySet()) {
            List<Block> mutables = MutandisItem.mutables.get(category);

            if (mutables.contains(block)) {
                categories.add(category);
            }
        }

        if (categories.size() > 0) {
            Random random = context.getWorld().random;

            if (context.getWorld() instanceof ServerWorld world) {
                List<Block> mutables = MutandisItem.mutables.get(categories.get(random.nextInt(categories.size())));

                Block result = mutables.get(random.nextInt(mutables.size()));

                mutate(world, context.getBlockPos(), result.getDefaultState());
            } else {
                mutateClient(context.getWorld(), context.getBlockPos());
            }

            if (context.getPlayer() != null && !context.getPlayer().isCreative()) context.getStack().decrement(1);
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    protected void mutate(ServerWorld world, BlockPos pos, BlockState state) {
        if (state.isOf(Blocks.MELON_STEM) || state.isOf(Blocks.PUMPKIN_STEM)) {
            state = state.with(StemBlock.AGE, 3);
        }

        world.setBlockState(pos, state);
    }

    @Environment(EnvType.CLIENT)
    protected void mutateClient(World world, BlockPos pos) {

    }
}
