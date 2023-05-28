package net.eman3600.dndreams.screens.slot;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class AttunementBurnSlot extends Slot {
    public static final Object2IntMap<ItemConvertible> ITEM_TO_ENERGY = new Object2IntOpenHashMap<>();


    public AttunementBurnSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    public boolean canInsert(ItemStack stack) {
        return ITEM_TO_ENERGY.containsKey(stack.getItem());
    }

    public static void putFuel(Item item, int energy) {
        ITEM_TO_ENERGY.put(item, energy);
    }
}
