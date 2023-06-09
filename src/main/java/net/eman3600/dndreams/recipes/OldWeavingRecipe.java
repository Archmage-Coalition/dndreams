package net.eman3600.dndreams.recipes;

import net.eman3600.dndreams.util.inventory.WeavingInventory;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;

@Deprecated(forRemoval = true)
public interface OldWeavingRecipe extends Recipe<WeavingInventory> {
    Ingredient getWeavingIngredient();
}
