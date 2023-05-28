package net.eman3600.dndreams.integration.rei.display;

import com.google.common.collect.ImmutableList;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.integration.rei.DnDreamsREIPlugin;
import net.eman3600.dndreams.recipes.SmokestackRecipe;
import net.minecraft.recipe.Ingredient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SmokestackDisplay implements Display {
    private final List<EntryIngredient> input;
    private final List<EntryIngredient> output;
    public final SmokestackRecipe RECIPE;

    public SmokestackDisplay(SmokestackRecipe recipe) {
        List<EntryIngredient> input = new ArrayList<>(EntryIngredients.ofIngredients(recipe.getIngredients()));

        try {
            input.add(EntryIngredients.ofIngredient(Ingredient.ofItems(ModItems.AMETHYST_JAR)));
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
        return DnDreamsREIPlugin.SMOKESTACK;
    }
}
