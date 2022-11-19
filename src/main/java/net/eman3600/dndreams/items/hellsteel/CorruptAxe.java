package net.eman3600.dndreams.items.hellsteel;

import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.eman3600.dndreams.items.interfaces.BloodlustItem;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CorruptAxe extends AxeItem implements BloodlustItem {
    public CorruptAxe(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return inflictBloodlust(world, user, user.getStackInHand(hand));
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (hasBloodlust(miner) && state.isIn(BlockTags.LOGS)) {
            tryBurn(stack, world, pos, miner);
            tryBurn(stack, world, pos.up(), miner);
            tryBurn(stack, world, pos.down(), miner);
        }

        return super.postMine(stack, world, state, pos, miner);
    }

    public void tryBurn(ItemStack stack, World world, BlockPos pos, LivingEntity miner) {
        if (world.isInBuildLimit(pos)) {
            BlockState state = world.getBlockState(pos);
            if (state.isIn(BlockTags.LOGS)) {
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

    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (hasBloodlust(attacker)) {
            target.addStatusEffect(new StatusEffectInstance(ModStatusEffects.AFFLICTION, 220), attacker);
            target.takeKnockback(0.7f, MathHelper.sin(attacker.getYaw() * ((float) Math.PI / 180)), -MathHelper.cos(attacker.getYaw() * ((float) Math.PI / 180)));
        }

        return super.postHit(stack, target, attacker);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(getTooltipBloodlust(world));
    }
}
