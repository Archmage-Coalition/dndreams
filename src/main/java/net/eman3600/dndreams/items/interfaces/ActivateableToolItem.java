package net.eman3600.dndreams.items.interfaces;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;

public interface ActivateableToolItem {

    boolean isSuitableFor(ItemStack stack, BlockState state);

}
