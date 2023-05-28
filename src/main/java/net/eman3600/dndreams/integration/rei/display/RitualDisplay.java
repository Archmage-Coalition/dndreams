package net.eman3600.dndreams.integration.rei.display;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.eman3600.dndreams.integration.rei.DnDreamsREIPlugin;
import net.eman3600.dndreams.recipes.RitualRecipe;

import java.util.Collections;
import java.util.List;

public class RitualDisplay implements Display {
    private final List<EntryIngredient> input;
    private final List<EntryIngredient> output;
    public final RitualRecipe RECIPE;

    public RitualDisplay(RitualRecipe recipe) {
        this.input = EntryIngredients.ofIngredients(recipe.getIngredients());
        this.output = Collections.singletonList(EntryIngredients.of(recipe.getOutput()));
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
        return DnDreamsREIPlugin.RITUAL;
    }
}
