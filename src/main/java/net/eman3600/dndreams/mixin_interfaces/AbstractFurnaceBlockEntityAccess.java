package net.eman3600.dndreams.mixin_interfaces;

import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

public interface AbstractFurnaceBlockEntityAccess {
    DefaultedList<ItemStack> getInventory();
}
