package net.eman3600.dndreams.screen.slot;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.eman3600.dndreams.initializers.basics.ModItems;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;

public class AttunementBurnSlot extends Slot {
    public static final Object2IntMap<ItemConvertible> ITEM_TO_ENERGY = new Object2IntOpenHashMap<>();


    public AttunementBurnSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    public boolean canInsert(ItemStack stack) {
        return ITEM_TO_ENERGY.containsKey(stack.getItem());
    }

    public static void registerEnergyFuels() {
        ITEM_TO_ENERGY.put(Items.REDSTONE, 5);
        ITEM_TO_ENERGY.put(Items.GLOWSTONE_DUST, 8);
        ITEM_TO_ENERGY.put(Items.SUGAR, 2);
        ITEM_TO_ENERGY.put(Items.GUNPOWDER, 5);

        ITEM_TO_ENERGY.put(ModItems.SCULK_POWDER, 25);
        ITEM_TO_ENERGY.put(ModItems.DREAM_POWDER, 25);
        ITEM_TO_ENERGY.put(ModItems.INFERNAL_RESIDUE, 20);
        ITEM_TO_ENERGY.put(ModItems.NIGHTMARE_FUEL, 40);
    }

    public static void putFuel(Item item, int energy) {
        ITEM_TO_ENERGY.put(item, energy);
    }
}
