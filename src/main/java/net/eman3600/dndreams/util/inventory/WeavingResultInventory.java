package net.eman3600.dndreams.util.inventory;

import net.eman3600.dndreams.cardinal_components.TormentComponent;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.recipes.WeavingRecipe;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeUnlocker;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

public class WeavingResultInventory implements Inventory, RecipeUnlocker {
    private final DefaultedList<ItemStack> stacks;
    @Nullable
    private Recipe<?> lastRecipe;

    public WeavingResultInventory() {
        this.stacks = DefaultedList.ofSize(1, ItemStack.EMPTY);
    }

    public int size() {
        return 1;
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

    public ItemStack getStack(int slot) {
        return this.stacks.get(0);
    }

    public ItemStack removeStack(int slot, int amount) {
        return Inventories.removeStack(this.stacks, 0);
    }

    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(this.stacks, 0);
    }

    public void setStack(int slot, ItemStack stack) {
        this.stacks.set(0, stack);
    }

    public void markDirty() {
    }

    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    public void clear() {
        this.stacks.clear();
    }

    public void setLastRecipe(@Nullable Recipe<?> recipe) {
        this.lastRecipe = recipe;
    }

    @Nullable
    public Recipe<?> getLastRecipe() {
        return this.lastRecipe;
    }

    @Override
    public boolean shouldCraftRecipe(World world, ServerPlayerEntity player, Recipe<?> recipe) {
        boolean should = RecipeUnlocker.super.shouldCraftRecipe(world, player, recipe);

        if (recipe instanceof WeavingRecipe weavingRecipe) {

            TormentComponent torment = EntityComponents.TORMENT.get(player);

            return should && torment.canAfford(weavingRecipe.sanityCost);
        }

        return should;
    }
}