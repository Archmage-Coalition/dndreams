package net.eman3600.dndreams.util.recipe;

import net.eman3600.dndreams.initializers.ModRecipeTypes;
import net.eman3600.dndreams.util.inventory.WeavingInventory;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;

public interface WeavingRecipe extends Recipe<WeavingInventory> {
    default RecipeType<?> getType() {
        return ModRecipeTypes.WEAVING;
    }
}