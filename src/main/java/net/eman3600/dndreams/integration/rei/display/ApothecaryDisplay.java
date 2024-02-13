package net.eman3600.dndreams.integration.rei.display;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.integration.rei.DnDreamsREIPlugin;
import net.eman3600.dndreams.recipes.ApothecaryRecipe;
import net.minecraft.recipe.Ingredient;

import java.util.Collections;
import java.util.List;

public class ApothecaryDisplay implements Display {
    private final List<EntryIngredient> input;
    private final List<EntryIngredient> output;
    public final ApothecaryRecipe RECIPE;

    public ApothecaryDisplay(ApothecaryRecipe recipe) {
        this.input = EntryIngredients.ofIngredients(recipe.getIngredients());
        this.output = Collections.singletonList(EntryIngredients.ofIngredient(Ingredient.ofItems(ModItems.BREW_INGESTED, ModItems.BREW_SPLASH, ModItems.BREW_LINGERING)));
        this.RECIPE = recipe;
    }


    @Override
    public List<EntryIngredient> getInputEntries() {
        return input;
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return output;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return DnDreamsREIPlugin.APOTHECARY;
    }
}
