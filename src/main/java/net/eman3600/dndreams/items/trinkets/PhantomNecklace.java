package net.eman3600.dndreams.items.trinkets;

import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.item.ItemStack;

public class PhantomNecklace extends TrinketItem {
    public PhantomNecklace(Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}
