package net.eman3600.dndreams.initializers.event;

import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.recipes.*;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModRecipeTypes {
    // Types

    public static final RecipeType<OldWeavingRecipe> OLD_WEAVING = register("old_weaving");
    public static final RecipeType<TransmutationRecipe> TRANSMUTATION = register("transmutation");
    public static final RecipeType<RitualRecipe> RITUAL = register("ritual");
    public static final RecipeType<SmokestackRecipe> SMOKESTACK = register("smokestack");
    public static final RecipeType<RefineryRecipe> REFINERY = register("refinery");
    public static final RecipeType<CauldronRecipe> CAULDRON = register("cauldron");
    public static final RecipeType<ApothecaryRecipe> APOTHECARY = register("apothecary");


    // Serializers

    public static final RecipeSerializer<TransmutationRecipe> TRANSMUTATION_SERIALIZER = Registry.register(Registry.RECIPE_SERIALIZER,
            new Identifier(Initializer.MODID, TransmutationRecipe.Serializer.ID), TransmutationRecipe.Serializer.INSTANCE);
    public static final RecipeSerializer<RitualRecipe> RITUAL_SERIALIZER = Registry.register(Registry.RECIPE_SERIALIZER,
            new Identifier(Initializer.MODID, RitualRecipe.Serializer.ID), RitualRecipe.Serializer.INSTANCE);
    public static final RecipeSerializer<SmokestackRecipe> SMOKESTACK_SERIALIZER = Registry.register(Registry.RECIPE_SERIALIZER,
            new Identifier(Initializer.MODID, SmokestackRecipe.Serializer.ID), SmokestackRecipe.Serializer.INSTANCE);
    public static final RecipeSerializer<RefineryRecipe> REFINERY_SERIALIZER = Registry.register(Registry.RECIPE_SERIALIZER,
            new Identifier(Initializer.MODID, RefineryRecipe.Serializer.ID), RefineryRecipe.Serializer.INSTANCE);
    public static final RecipeSerializer<CauldronRecipe> CAULDRON_SERIALIZER = Registry.register(Registry.RECIPE_SERIALIZER,
            new Identifier(Initializer.MODID, CauldronRecipe.Serializer.ID), CauldronRecipe.Serializer.INSTANCE);
    public static final RecipeSerializer<ApothecaryRecipe> APOTHECARY_SERIALIZER = Registry.register(Registry.RECIPE_SERIALIZER,
            new Identifier(Initializer.MODID, ApothecaryRecipe.Serializer.ID), ApothecaryRecipe.Serializer.INSTANCE);

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
