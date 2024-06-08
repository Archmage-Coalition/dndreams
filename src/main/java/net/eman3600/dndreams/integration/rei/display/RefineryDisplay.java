package net.eman3600.dndreams.integration.rei.display;

import com.google.common.collect.ImmutableList;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.eman3600.dndreams.integration.rei.DnDreamsREIPlugin;
import net.eman3600.dndreams.recipes.RefineryRecipe;

import java.util.ArrayList;
import java.util.List;

public class RefineryDisplay implements Display {
    private final List<EntryIngredient> input;
    private final List<EntryIngredient> output;
    public final EntryIngredient byproduct;
    public final RefineryRecipe RECIPE;

    public RefineryDisplay(RefineryRecipe recipe) {
        List<EntryIngredient> input = new ArrayList<>(EntryIngredients.ofIngredients(recipe.getIngredients()));
        List<EntryIngredient> output = new ArrayList<>();
        output.add(EntryIngredients.of(recipe.getOutput()));
        this.byproduct = EntryIngredients.of(recipe.byproduct);
        output.add(byproduct);
        this.RECIPE = recipe;

        while (input.size() < 4) {
            input.add(EntryIngredient.empty());
        }

        this.input = ImmutableList.copyOf(input);
        this.output = ImmutableList.copyOf(output);
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
        return DnDreamsREIPlugin.REFINERY;
    }
}
