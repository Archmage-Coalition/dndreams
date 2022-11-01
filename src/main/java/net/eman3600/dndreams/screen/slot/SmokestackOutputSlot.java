package net.eman3600.dndreams.screen.slot;

import net.eman3600.dndreams.initializers.ModItems;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class SmokestackOutputSlot extends Slot {
    public SmokestackOutputSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    public boolean canInsert(ItemStack stack) {
        return false;
    }
}
