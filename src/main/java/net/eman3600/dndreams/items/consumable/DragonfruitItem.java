package net.eman3600.dndreams.items.consumable;

import net.eman3600.dndreams.items.TooltipItem;
import net.minecraft.item.ItemStack;

public class DragonfruitItem extends TooltipItem {
    public DragonfruitItem(Settings settings) {
        super(settings);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return super.getMaxUseTime(stack) * 2;
    }
}
