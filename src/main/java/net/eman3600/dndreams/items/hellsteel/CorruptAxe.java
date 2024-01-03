package net.eman3600.dndreams.items.hellsteel;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CorruptAxe extends AxeItem {
    public CorruptAxe(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    /*@Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (hasBloodlust(miner) && state.isIn(BlockTags.LOGS)) {
            tryBurn(stack, world, pos, miner, true);
            tryBurn(stack, world, pos.up(), miner, false);
            tryBurn(stack, world, pos.down(), miner, false);
        }

        return super.postMine(stack, world, state, pos, miner);
    }*/

    /*public void tryBurn(ItemStack stack, World world, BlockPos pos, LivingEntity miner, boolean middle) {
        if (world.isInBuildLimit(pos)) {
            BlockState state = world.getBlockState(pos);
            if (state.isIn(BlockTags.LOGS) || middle) {
                if (middle) {

                    BlockState fire = FireBlock.getState(world, pos);
                    if (fire.canPlaceAt(world, pos) && world.isAir(pos)) {
                        world.setBlockState(pos, fire, FireBlock.NOTIFY_LISTENERS);
                    }
                }
                for (Direction dir: Direction.values()) {
                    if (dir == Direction.UP || dir == Direction.DOWN) continue;
                    BlockPos attempt = pos.offset(dir);

                    BlockState fire = FireBlock.getState(world, attempt);
                    if (fire.canPlaceAt(world, attempt) && world.isAir(attempt)) {
                        world.setBlockState(attempt, fire, FireBlock.NOTIFY_LISTENERS);
                    }
                }
            }
        }

    }*/

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable(getTranslationKey() + ".tooltip"));
    }
}
