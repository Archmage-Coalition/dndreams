package net.eman3600.dndreams.initializers;

import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.util.recipe.WeavingRecipe;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModRecipeTypes {
    public static final RecipeType<WeavingRecipe> WEAVING = register("weaving");

    private static <T extends Recipe<?>> RecipeType<T> register(String id) {
        return Registry.register(Registry.RECIPE_TYPE, new Identifier(Initializer.MODID, id), new RecipeType<T>() {
            public String toString() {
                return id;
            }
        });
    }

    public static void registerTypes() {
        System.out.println("Registering recipe types for " + Initializer.MODID);
    }
}
