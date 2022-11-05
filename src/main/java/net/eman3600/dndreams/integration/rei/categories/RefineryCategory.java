package net.eman3600.dndreams.integration.rei.categories;

import dev.architectury.fluid.FluidStack;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.eman3600.dndreams.initializers.ModBlocks;
import net.eman3600.dndreams.initializers.ModFluids;
import net.eman3600.dndreams.integration.rei.DnDreamsREIPlugin;
import net.eman3600.dndreams.integration.rei.display.RefineryDisplay;
import net.eman3600.dndreams.integration.rei.display.TransmutationDisplay;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class RefineryCategory implements DisplayCategory<RefineryDisplay> {
    public static final Text TITLE = Text.translatable("rei.dndreams.refinery");
    public static final EntryStack<ItemStack> ICON = EntryStacks.of(ModBlocks.REFINERY);

    @Override
    public CategoryIdentifier<? extends RefineryDisplay> getCategoryIdentifier() {
        return DnDreamsREIPlugin.REFINERY;
    }

    @Override
    public Text getTitle() {
        return TITLE;
    }

    @Override
    public Renderer getIcon() {
        return ICON;
    }

    @Override
    public int getDisplayHeight() {
        return 80;
    }

    @Override
    public int getDisplayWidth(RefineryDisplay display) {
        return 130;
    }

    @Override
    public List<Widget> setupDisplay(RefineryDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createArrow(new Point(bounds.getCenterX() - 5, bounds.getCenterY() - 10)).animationDurationTicks(display.RECIPE.refineTime));

        List<EntryIngredient> output = display.getOutputEntries();
        List<EntryIngredient> inputs = new ArrayList<>(display.getInputEntries());

        while (inputs.size() < 4) {
            inputs.add(EntryIngredient.empty());
        }

        widgets.add(Widgets.createSlot(new Point(bounds.getCenterX() - 29 - 18, bounds.getCenterY() - 19)).entries(inputs.get(0)));
        widgets.add(Widgets.createSlot(new Point(bounds.getCenterX() - 29, bounds.getCenterY() - 19)).entries(inputs.get(1)));
        widgets.add(Widgets.createSlot(new Point(bounds.getCenterX() - 29 - 18, bounds.getCenterY() - 19 + 18)).entries(inputs.get(2)));
        widgets.add(Widgets.createSlot(new Point(bounds.getCenterX() - 29, bounds.getCenterY() - 19 + 18)).entries(inputs.get(3)));

        widgets.add(Widgets.createSlot(new Point(bounds.getCenterX() + 25, bounds.getCenterY() - 19)).entries(output.get(0)));
        widgets.add(Widgets.createSlot(new Point(bounds.getCenterX() + 25, bounds.getCenterY() - 19 + 18)).entries(display.byproduct));

        widgets.add(Widgets.createSlot(new Point(bounds.getCenterX() - 1, bounds.getCenterY() + 16)).entries(display.jars()));

        widgets.add(Widgets.createLabel(new Point(bounds.getCenterX() - 29 - 21, bounds.getCenterY() + 20), Text.translatable("rei.dndreams.refinery.cost", display.RECIPE.powerCost)).color(0x373737).noShadow().leftAligned());
        if (display.RECIPE.dreamOnly) {
            widgets.add(Widgets.createLabel(new Point(bounds.getCenterX() - 29 - 21, bounds.getCenterY() - 31), Text.translatable("rei.dndreams.refinery.dream_only")).color(0x373737).noShadow().leftAligned());
        }

        return widgets;
    }
}
