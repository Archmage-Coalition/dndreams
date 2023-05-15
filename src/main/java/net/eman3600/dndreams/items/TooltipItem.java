package net.eman3600.dndreams.items;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.checkerframework.checker.index.qual.Positive;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TooltipItem extends Item {
    @Nullable
    private final List<TooltipKey> keys;

    public TooltipItem(Settings settings) {
        super(settings);
        keys = new ArrayList<>();
    }

    public TooltipItem withTooltip(@Nullable String key, @Positive int lines) {
        keys.add(new TooltipKey(key, lines));
        return this;
    }


    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        for (TooltipKey key: keys) {
            String route = key.key == null ? (getTranslationKey() + ".tooltip") : key.key;

            if (key.lines <= 1) {
                tooltip.add(Text.translatable(route));
            } else {
                for (int i = 0; i < key.lines; i++) {
                    tooltip.add(Text.translatable(route + "." + i));
                }
            }
        }
    }


    private static class TooltipKey {
        final String key;
        final int lines;

        TooltipKey(@Nullable String key, @Positive int lines) {
            this.key = key;
            this.lines = lines;
        }
    }
}
