package net.eman3600.dndreams.items.hellsteel;

import net.eman3600.dndreams.blocks.energy.RitualCandleBlock;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.*;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CorruptAxe extends AxeItem {
    public CorruptAxe(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.setOnFireFor(4 * (EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT, stack) + 1));

        return super.postHit(stack, target, attacker);
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
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity player = context.getPlayer();
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);

        if (!CampfireBlock.canBeLit(blockState) && !CandleBlock.canBeLit(blockState) && !CandleCakeBlock.canBeLit(blockState) && !RitualCandleBlock.canBeLit(blockState)) {
            BlockPos blockPos2 = blockPos.offset(context.getSide());
            if (AbstractFireBlock.canPlaceAt(world, blockPos2, context.getPlayerFacing())) {
                world.playSound(player, blockPos2, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
                BlockState blockState2 = AbstractFireBlock.getState(world, blockPos2);
                world.setBlockState(blockPos2, blockState2, 11);
                world.emitGameEvent(player, GameEvent.BLOCK_PLACE, blockPos);
                ItemStack itemStack = context.getStack();
                if (player instanceof ServerPlayerEntity) {
                    Criteria.PLACED_BLOCK.trigger((ServerPlayerEntity) player, blockPos2, itemStack);
                    itemStack.damage(1, player, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
                }

                return ActionResult.success(world.isClient());
            } else {
                return ActionResult.FAIL;
            }
        } else {
            world.playSound(player, blockPos, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
            world.setBlockState(blockPos, blockState.with(Properties.LIT, true), 11);
            world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, blockPos);
            if (player != null) {
                context.getStack().damage(1, player, p -> p.sendToolBreakStatus(context.getHand()));
            }



            return ActionResult.success(world.isClient());
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable(getTranslationKey() + ".tooltip"));
    }
}
