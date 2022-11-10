package net.eman3600.dndreams.items;

import net.eman3600.dndreams.initializers.world.ModDimensions;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DreamyItem extends Item {
    public DreamyItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        dreamTooltip(world, tooltip);
    }

    public static void dreamTooltip(@Nullable World world, List<Text> tooltip) {
        if (world != null && world.getRegistryKey() == ModDimensions.DREAM_DIMENSION_KEY) return;
        tooltip.add(Text.translatable("tooltip.dndreams.dream_origin"));
        tooltip.add(Text.translatable("tooltip.dndreams.dream_origin_2"));
    }
}
