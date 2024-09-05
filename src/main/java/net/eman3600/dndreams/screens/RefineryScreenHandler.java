package net.eman3600.dndreams.screens;

import net.eman3600.dndreams.initializers.event.ModScreenHandlerTypes;
import net.eman3600.dndreams.screens.slot.GenericOutputSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class RefineryScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    public final PropertyDelegate delegate;

    public RefineryScreenHandler(int syncId, PlayerInventory inventory) {
        this(syncId, inventory, new SimpleInventory(7), new ArrayPropertyDelegate(4));
    }

    public RefineryScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate delegate) {
        super(ModScreenHandlerTypes.REFINERY, syncId);
        checkSize(inventory, 6);
        this.inventory = inventory;
        this.delegate = delegate;


        addSlot(new Slot(inventory, 0, 32, 25));
        addSlot(new Slot(inventory, 1, 50, 25));
        addSlot(new Slot(inventory, 2, 32, 43));
        addSlot(new Slot(inventory, 3, 50, 43));
        addSlot(new GenericOutputSlot(inventory, 4, 120, 25));
        addSlot(new GenericOutputSlot(inventory, 5, 120, 43));

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        addProperties(delegate);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (index < 6) {
                if (!this.insertItem(itemStack2, 6, 41, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.insertItem(itemStack2, 0, 4, false)) {
                    return ItemStack.EMPTY;
                }
            }




            if (itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTakeItem(player, itemStack2);
        }
        return itemStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return inventory.canPlayerUse(player);
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}
