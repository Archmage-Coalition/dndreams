package net.eman3600.dndreams.recipe;

import net.eman3600.dndreams.util.inventory.WeavingInventory;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;

public interface WeavingRecipe extends Recipe<WeavingInventory> {
    Ingredient getWeavingIngredient();
}
