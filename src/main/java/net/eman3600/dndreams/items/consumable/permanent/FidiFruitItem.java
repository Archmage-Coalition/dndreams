package net.eman3600.dndreams.items.consumable.permanent;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class FidiFruitItem extends Item {
    public FidiFruitItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}
