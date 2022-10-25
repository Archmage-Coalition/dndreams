package net.eman3600.dndreams.screen.slot;

import net.eman3600.dndreams.initializers.ModItems;
import net.eman3600.dndreams.util.ModTags;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;

public class AttunementSlot extends Slot {
    public AttunementSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    public boolean canInsert(ItemStack stack) {
        return stack.isOf(Items.AMETHYST_SHARD) || stack.isOf(ModItems.ATTUNED_SHARD) || stack.isOf(ModItems.CHARGED_SHARD);
    }

    @Override
    public int getMaxItemCount() {
        return 1;
    }
}
