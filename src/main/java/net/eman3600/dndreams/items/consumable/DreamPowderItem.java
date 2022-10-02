package net.eman3600.dndreams.items.consumable;

import net.eman3600.dndreams.initializers.ModDimensions;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DreamPowderItem extends Item {
    public DreamPowderItem(Settings settings) {
        super(settings);
    }

    public int getMaxUseTime(ItemStack stack) {
        return 8;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.getDimensionKey() == ModDimensions.DREAM_TYPE_KEY) {
            return super.use(world, user, hand);
        }
        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (world != null & world.getDimensionKey() == ModDimensions.DREAM_TYPE_KEY) {
            tooltip.add(Text.translatable("item.dndreams.dream_powder.tooltip"));
        }
    }
}
