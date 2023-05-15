package net.eman3600.dndreams.items;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class TooltipItem extends Item {
    @Nullable
    private final String tooltipKey;
    private final int lines;

    public TooltipItem(Settings settings) {
        this(settings, 1);
    }

    public TooltipItem(Settings settings, @Nullable String tooltipKey) {
        this(settings, 1, tooltipKey);
    }

    public TooltipItem(Settings settings, int lines) {
        this(settings, lines, null);
    }

    public TooltipItem(Settings settings, int lines, @Nullable String tooltipKey) {
        super(settings);
        this.lines = lines;
        this.tooltipKey = tooltipKey;
    }


    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        String key = Objects.requireNonNullElseGet(tooltipKey, () -> getTranslationKey() + ".tooltip");

        if (lines <= 1) {
            tooltip.add(Text.translatable(key));
        } else {
            for (int i = 0; i < lines; i++) {
                tooltip.add(Text.translatable(key + "." + i));
            }
        }
    }
}
