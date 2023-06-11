package net.eman3600.dndreams.integration.rei.categories;

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
import net.eman3600.dndreams.initializers.basics.ModBlocks;
import net.eman3600.dndreams.integration.rei.DnDreamsREIPlugin;
import net.eman3600.dndreams.integration.rei.display.WeavingDisplay;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class WeavingCategory implements DisplayCategory<WeavingDisplay> {
    public static final Text TITLE = Text.translatable("rei.dndreams.weaving");
    public static final EntryStack<ItemStack> ICON = EntryStacks.of(ModBlocks.DREAM_TABLE);

    @Override
    public CategoryIdentifier<? extends WeavingDisplay> getCategoryIdentifier() {
        return DnDreamsREIPlugin.WEAVING;
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
        return 116;
    }

    @Override
    public int getDisplayWidth(WeavingDisplay display) {
        return 110;
    }

    @Override
    public List<Widget> setupDisplay(WeavingDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createArrow(new Point(bounds.getCenterX() - 5, bounds.getCenterY() - 21)));

        List<EntryIngredient> output = display.getOutputEntries();
        List<EntryIngredient> inputs = new ArrayList<>(display.getInputEntries());
        int ingredients = display.ingredients;

        widgets.add(Widgets.createSlot(new Point(bounds.getCenterX() - 29, bounds.getCenterY() - 39)).entries(display.mold));
        widgets.add(Widgets.createSlot(new Point(bounds.getCenterX() - 29, bounds.getCenterY() - 19)).entries(ingredients >= 1 ? inputs.get(0) : EntryIngredient.empty()));
        widgets.add(Widgets.createSlot(new Point(bounds.getCenterX() - 29, bounds.getCenterY() - 1)).entries(ingredients >= 2 ? inputs.get(1) : EntryIngredient.empty()));

        widgets.add(Widgets.createSlot(new Point(bounds.getCenterX() + 25, bounds.getCenterY() - 21)).entries(output.get(0)));

        widgets.add(Widgets.createLabel(new Point(bounds.getCenterX() - 29 - 18, bounds.getCenterY() + 20), Text.translatable("rei.dndreams.weaving.cost", display.RECIPE.sanityCost)).color(0xFF404040, 0xFFBBBBBB).noShadow().leftAligned());

        return widgets;
    }
}
