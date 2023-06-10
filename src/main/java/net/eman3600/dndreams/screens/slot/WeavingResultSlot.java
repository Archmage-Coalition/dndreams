package net.eman3600.dndreams.screens.slot;

import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.initializers.event.ModRecipeTypes;
import net.eman3600.dndreams.recipes.WeavingRecipe;
import net.eman3600.dndreams.screens.WeavingScreenHandler;
import net.eman3600.dndreams.util.inventory.WeavingInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeUnlocker;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;

import java.util.List;

public class WeavingResultSlot extends Slot {
    private final WeavingInventory input;
    private final WeavingScreenHandler handler;
    private final PlayerEntity player;
    private int amount;

    public WeavingResultSlot(PlayerEntity player, WeavingInventory input, Inventory inventory, WeavingScreenHandler handler, int index, int x, int y) {
        super(inventory, index, x, y);
        this.player = player;
        this.input = input;
        this.handler = handler;
    }

    public boolean canInsert(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canTakeItems(PlayerEntity playerEntity) {

        return handler.trulyMatches();
    }

    public ItemStack takeStack(int amount) {
        if (this.hasStack()) {
            this.amount += Math.min(amount, this.getStack().getCount());
        }

        return super.takeStack(amount);
    }

    protected void onCrafted(ItemStack stack, int amount) {
        this.amount += amount;
        this.onCrafted(stack);
    }

    protected void onTake(int amount) {
        this.amount += amount;
    }

    protected void onCrafted(ItemStack stack) {
        if (this.amount > 0) {
            stack.onCraft(this.player.world, this.player, this.amount);
        }

        if (this.inventory instanceof RecipeUnlocker) {
            ((RecipeUnlocker)this.inventory).unlockLastRecipe(this.player);
        }

        this.amount = 0;
    }

    public void onTakeItem(PlayerEntity player, ItemStack stack) {
        this.onCrafted(stack);
        List<WeavingRecipe> list = player.world.getRecipeManager().getAllMatches(ModRecipeTypes.WEAVING, this.input, player.world);

        list.stream().filter(recipe -> recipe.output.isOf(stack.getItem())).forEach(recipe -> EntityComponents.TORMENT.get(player).spendSanity(recipe.sanityCost));

        DefaultedList<ItemStack> defaultedList = player.world.getRecipeManager().getRemainingStacks(ModRecipeTypes.WEAVING, this.input, player.world);

        for(int i = 1; i < defaultedList.size(); ++i) {
            ItemStack itemStack = this.input.getStack(i);
            ItemStack itemStack2 = defaultedList.get(i);
            if (!itemStack.isEmpty()) {
                this.input.removeStack(i, 1);
                itemStack = this.input.getStack(i);
            }

            if (!itemStack2.isEmpty()) {
                if (itemStack.isEmpty()) {
                    this.input.setStack(i, itemStack2);
                } else if (ItemStack.areItemsEqualIgnoreDamage(itemStack, itemStack2) && ItemStack.areNbtEqual(itemStack, itemStack2)) {
                    itemStack2.increment(itemStack.getCount());
                    this.input.setStack(i, itemStack2);
                } else if (!this.player.getInventory().insertStack(itemStack2)) {
                    this.player.dropItem(itemStack2, false);
                }
            }
        }

        handler.updateAfterCraft();



    }


}
