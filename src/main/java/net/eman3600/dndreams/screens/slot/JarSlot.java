package net.eman3600.dndreams.screens.slot;

import net.eman3600.dndreams.initializers.basics.ModItems;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class JarSlot extends Slot {
    public JarSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    public boolean canInsert(ItemStack stack) {
        return stack.isOf(ModItems.AMETHYST_JAR);
    }
}
