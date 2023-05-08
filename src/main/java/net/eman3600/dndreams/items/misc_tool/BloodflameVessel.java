package net.eman3600.dndreams.items.misc_tool;

import net.eman3600.dndreams.blocks.energy.RitualCandleBlock;
import net.eman3600.dndreams.items.interfaces.BloodlustItem;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.*;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.text.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BloodflameVessel extends Item implements BloodlustItem {

    public BloodflameVessel(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!hasBloodlust(user)) {
            ItemStack stack = user.getStackInHand(hand);
            inflictBloodlust(world, user, stack);

            return TypedActionResult.consume(stack);
        }

        return super.use(world, user, hand);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity player = context.getPlayer();
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);

        if (player == null || !hasBloodlust(player)) {
            return ActionResult.PASS;
        }

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
                }

                return ActionResult.success(world.isClient());
            } else {
                return ActionResult.FAIL;
            }
        } else {
            world.playSound(player, blockPos, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
            world.setBlockState(blockPos, blockState.with(Properties.LIT, true), 11);
            world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, blockPos);



            return ActionResult.success(world.isClient());
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(getTooltipBloodlust(world));
    }
}
