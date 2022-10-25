package net.eman3600.dndreams.items.charge;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class AbstractChargeItem extends Item {
    public AbstractChargeItem(Settings settings) {
        super(settings);
    }

    public abstract ItemStack charge(ItemStack stack, int amount);
    public abstract boolean canAffordCharge(ItemStack stack, int amount);
    public abstract ItemStack discharge(ItemStack stack, int amount);
}
