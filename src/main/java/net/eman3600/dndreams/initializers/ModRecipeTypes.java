package net.eman3600.dndreams.initializers;

import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.recipe.*;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModRecipeTypes {
    // Types

    public static final RecipeType<WeavingRecipe> WEAVING = register("weaving");
    public static final RecipeType<TransmutationRecipe> TRANSMUTATION = register("transmutation");
    public static final RecipeType<RitualRecipe> RITUAL = register("ritual");
    public static final RecipeType<SmokestackRecipe> SMOKESTACK = register("smokestack");


    // Serializers

    public static final RecipeSerializer<WeavingShapedRecipe> WEAVING_SERIALIZER = Registry.register(Registry.RECIPE_SERIALIZER,
            new Identifier(Initializer.MODID, WeavingShapedRecipe.Serializer.ID), WeavingShapedRecipe.Serializer.INSTANCE);
    public static final RecipeSerializer<TransmutationRecipe> TRANSMUTATION_SERIALIZER = Registry.register(Registry.RECIPE_SERIALIZER,
            new Identifier(Initializer.MODID, TransmutationRecipe.Serializer.ID), TransmutationRecipe.Serializer.INSTANCE);
    public static final RecipeSerializer<WeavingShapelessRecipe> WEAVING_SHAPELESS_SERIALIZER = Registry.register(Registry.RECIPE_SERIALIZER,
            new Identifier(Initializer.MODID, WeavingShapelessRecipe.Serializer.ID), WeavingShapelessRecipe.Serializer.INSTANCE);
    public static final RecipeSerializer<RitualRecipe> RITUAL_SERIALIZER = Registry.register(Registry.RECIPE_SERIALIZER,
            new Identifier(Initializer.MODID, RitualRecipe.Serializer.ID), RitualRecipe.Serializer.INSTANCE);
    public static final RecipeSerializer<SmokestackRecipe> SMOKESTACK_SERIALIZER = Registry.register(Registry.RECIPE_SERIALIZER,
            new Identifier(Initializer.MODID, SmokestackRecipe.Serializer.ID), SmokestackRecipe.Serializer.INSTANCE);

    private static <T extends Recipe<?>> RecipeType<T> register(String id) {
        return Registry.register(Registry.RECIPE_TYPE, new Identifier(Initializer.MODID, id), new RecipeType<T>() {
            public String toString() {
                return (new Identifier(Initializer.MODID, id)).toString();
            }
        });
    }

    public static void registerTypes() {
        System.out.println("Registering recipe types for " + Initializer.MODID);
    }
}
