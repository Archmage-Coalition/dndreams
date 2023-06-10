package net.eman3600.dndreams.util.inventory;

import net.eman3600.dndreams.screens.WeavingScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.util.collection.DefaultedList;

import java.util.Iterator;

public class WeavingInventory implements Inventory, RecipeInputProvider {
    private final DefaultedList<ItemStack> stacks;
    private final WeavingScreenHandler handler;

    public WeavingInventory(WeavingScreenHandler handler) {
        this.stacks = DefaultedList.ofSize(3, ItemStack.EMPTY);
        this.handler = handler;
    }

    public int size() {
        return this.stacks.size();
    }

    public boolean isEmpty() {
        Iterator var1 = this.stacks.iterator();

        ItemStack itemStack;
        do {
            if (!var1.hasNext()) {
                return true;
            }

            itemStack = (ItemStack)var1.next();
        } while(itemStack.isEmpty());

        return false;
    }

    @Override
    public ItemStack getStack(int slot) {
        return slot >= this.size() ? ItemStack.EMPTY : this.stacks.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(this.stacks, slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack itemStack = Inventories.splitStack(this.stacks, slot, amount);
        if (!itemStack.isEmpty()) {
            this.handler.onContentChanged(this);
        }

        return itemStack;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        this.stacks.set(slot, stack);
        this.handler.onContentChanged(this);
    }

    @Override
    public void markDirty() {
        handler.getContentsChangedListener().run();
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        this.stacks.clear();
    }

    @Override
    public void provideRecipeInputs(RecipeMatcher finder) {
        Iterator var2 = this.stacks.iterator();

        while(var2.hasNext()) {
            ItemStack itemStack = (ItemStack)var2.next();
            finder.addUnenchantedInput(itemStack);
        }

    }

    public boolean canPlayerAffordSanity(float cost) {
        return handler.canPlayerAffordSanity(cost);
    }
}
