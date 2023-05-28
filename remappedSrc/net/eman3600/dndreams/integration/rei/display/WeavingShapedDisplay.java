package net.eman3600.dndreams.integration.rei.display;

import com.google.common.collect.ImmutableList;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.eman3600.dndreams.integration.rei.DnDreamsREIPlugin;
import net.eman3600.dndreams.recipes.WeavingShapedRecipe;
import net.eman3600.dndreams.recipes.WeavingShapelessRecipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WeavingShapedDisplay implements Display {
    private List<EntryIngredient> input;
    private List<EntryIngredient> output;
    public final WeavingShapedRecipe RECIPE;

    public WeavingShapedDisplay(WeavingShapedRecipe recipe) {

        List<EntryIngredient> input = new ArrayList<>(EntryIngredients.ofIngredients(recipe.getIngredients()));

        try {
            input.add(EntryIngredients.ofIngredient(recipe.getWeavingIngredient()));
        } catch (NullPointerException ignored) {}

        this.input = ImmutableList.copyOf(input);
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
        return DnDreamsREIPlugin.WEAVING_SHAPED;
    }
}