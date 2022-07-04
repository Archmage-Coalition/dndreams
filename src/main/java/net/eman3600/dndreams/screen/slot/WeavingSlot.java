package net.eman3600.dndreams.screen.slot;

import net.eman3600.dndreams.initializers.ModRecipeTypes;
import net.eman3600.dndreams.util.ModTags;
import net.eman3600.dndreams.util.inventory.WeavingInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;

public class WeavingSlot extends Slot {
    private int amount;

    public WeavingSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    public boolean canInsert(ItemStack stack) {
        return stack.isIn(ModTags.WEAVING_ITEM);
    }
}
