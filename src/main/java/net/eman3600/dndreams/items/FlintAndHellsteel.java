package net.eman3600.dndreams.items;

import com.mojang.brigadier.Message;
import net.eman3600.dndreams.cardinal_components.ManaComponent;
import net.eman3600.dndreams.initializers.EntityComponents;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.*;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.text.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FlintAndHellsteel extends Item {
    private static int MANA_USE = 4;

    public FlintAndHellsteel(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity playerEntity = context.getPlayer();
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);

        ManaComponent component = EntityComponents.MANA.get(playerEntity);

        if (component.getMana() < MANA_USE) {
            return ActionResult.FAIL;
        }

        if (!CampfireBlock.canBeLit(blockState) && !CandleBlock.canBeLit(blockState) && !CandleCakeBlock.canBeLit(blockState)) {
            BlockPos blockPos2 = blockPos.offset(context.getSide());
            if (AbstractFireBlock.canPlaceAt(world, blockPos2, context.getPlayerFacing())) {
                world.playSound(playerEntity, blockPos2, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
                BlockState blockState2 = AbstractFireBlock.getState(world, blockPos2);
                world.setBlockState(blockPos2, blockState2, 11);
                world.emitGameEvent(playerEntity, GameEvent.BLOCK_PLACE, blockPos);
                ItemStack itemStack = context.getStack();
                if (playerEntity instanceof ServerPlayerEntity) {
                    Criteria.PLACED_BLOCK.trigger((ServerPlayerEntity)playerEntity, blockPos2, itemStack);
                    component.useMana(MANA_USE);
                }

                return ActionResult.success(world.isClient());
            } else {
                return ActionResult.FAIL;
            }
        } else {
            world.playSound(playerEntity, blockPos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
            world.setBlockState(blockPos, (BlockState)blockState.with(Properties.LIT, true), 11);
            world.emitGameEvent(playerEntity, GameEvent.BLOCK_CHANGE, blockPos);
            if (playerEntity != null) {
                component.useMana(MANA_USE);
            }



            return ActionResult.success(world.isClient());
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Texts.toText(new Message() {
            @Override
            public String getString() {
                return "§dMana Cost: " + MANA_USE;
            }
        }));
    }
}
