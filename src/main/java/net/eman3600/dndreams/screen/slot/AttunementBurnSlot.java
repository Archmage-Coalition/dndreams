package net.eman3600.dndreams.screen.slot;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.eman3600.dndreams.initializers.ModFluids;
import net.eman3600.dndreams.initializers.ModItems;
import net.minecraft.inventory.Inventory;
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
        ITEM_TO_ENERGY.put(Items.REDSTONE, 40);
        ITEM_TO_ENERGY.put(Items.GLOWSTONE_DUST, 40);
        ITEM_TO_ENERGY.put(Items.SUGAR, 10);
        ITEM_TO_ENERGY.put(Items.GUNPOWDER, 30);

        ITEM_TO_ENERGY.put(ModItems.SCULK_POWDER, 100);
        ITEM_TO_ENERGY.put(ModItems.DREAM_POWDER, 100);
        ITEM_TO_ENERGY.put(ModItems.INFERNAL_RESIDUE, 130);
        ITEM_TO_ENERGY.put(ModItems.NIGHTMARE_FUEL, 200);

        ITEM_TO_ENERGY.put(ModItems.LIQUID_SOUL, 500);
    }
}
