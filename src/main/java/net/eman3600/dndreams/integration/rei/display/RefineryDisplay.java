package net.eman3600.dndreams.integration.rei.display;

import com.google.common.collect.ImmutableList;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.eman3600.dndreams.initializers.ModItems;
import net.eman3600.dndreams.integration.rei.DnDreamsREIPlugin;
import net.eman3600.dndreams.recipe.RefineryRecipe;
import net.eman3600.dndreams.recipe.TransmutationRecipe;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RefineryDisplay implements Display {
    private final List<EntryIngredient> input;
    private final List<EntryIngredient> output;
    public final EntryIngredient byproduct;
    private final int jars;
    public final RefineryRecipe RECIPE;

    public RefineryDisplay(RefineryRecipe recipe) {
        List<EntryIngredient> input = new ArrayList<>(EntryIngredients.ofIngredients(recipe.getIngredients()));
        this.jars = recipe.jarsRequired();
        this.output = Collections.singletonList(EntryIngredients.of(recipe.getOutput()));
        this.byproduct = EntryIngredients.of(recipe.byproduct);
        this.RECIPE = recipe;

        if (jars > 0) input.add(jars());

        this.input = ImmutableList.copyOf(input);
    }


    @Override
    public List<EntryIngredient> getInputEntries() {
        return input;
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return output;
    }

    public EntryIngredient jars() {
        if (jars < 1) return EntryIngredient.empty();

        return EntryIngredients.of(new ItemStack(ModItems.AMETHYST_JAR, jars));
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return DnDreamsREIPlugin.REFINERY;
    }
}
