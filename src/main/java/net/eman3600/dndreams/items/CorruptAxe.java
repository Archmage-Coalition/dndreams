package net.eman3600.dndreams.items;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;

public class CorruptAxe extends AxeItem {
    public CorruptAxe(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (state.isIn(BlockTags.LOGS)) {
            tryFell(stack, world, pos.up(1), miner, true);
            tryFell(stack, world, pos.down(1), miner, false);
        }

        return super.postMine(stack, world, state, pos, miner);
    }

    public void tryFell(ItemStack stack, World world, BlockPos pos, LivingEntity miner, boolean isUp) {
        if (world.isInBuildLimit(pos)) {
            BlockState state = world.getBlockState(pos);
            if (state.isIn(BlockTags.LOGS)) {
                world.breakBlock(pos, true, miner);
                if (isUp) {
                    tryFell(stack, world, pos.up(1), miner, true);
                } else {
                    tryFell(stack, world, pos.down(1), miner, false);
                }
            }
        }

    }
}
