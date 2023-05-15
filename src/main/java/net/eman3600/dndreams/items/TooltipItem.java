package net.eman3600.dndreams.items;

import net.minecraft.item.Item;

public class TooltipItem extends Item {
    private boolean defaultTooltip;

    public TooltipItem(Settings settings) {
        super(settings);
    }

    public TooltipItem(Settings settings, String tooltipKey) {
        super(settings);

    }
}
