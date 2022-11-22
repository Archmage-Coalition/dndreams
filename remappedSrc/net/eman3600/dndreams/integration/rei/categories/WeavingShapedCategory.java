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
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.eman3600.dndreams.initializers.basics.ModBlocks;
import net.eman3600.dndreams.integration.rei.DnDreamsREIPlugin;
import net.eman3600.dndreams.integration.rei.display.WeavingShapedDisplay;
import net.eman3600.dndreams.recipe.WeavingShapedRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class WeavingShapedCategory implements DisplayCategory<WeavingShapedDisplay> {
    public static final Text TITLE = Text.translatable("rei.dndreams.weaving");
    public static final EntryStack<ItemStack> ICON = EntryStacks.of(ModBlocks.DREAM_TABLE);

    @Override
    public CategoryIdentifier<? extends WeavingShapedDisplay> getCategoryIdentifier() {
        return DnDreamsREIPlugin.WEAVING_SHAPED;
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
    public List<Widget> setupDisplay(WeavingShapedDisplay display, Rectangle bounds) {
        WeavingShapedRecipe recipe = display.RECIPE;

        Point startPoint = new Point(bounds.getCenterX() - 60, bounds.getCenterY() - 27);
        Point outputPoint = new Point(bounds.getCenterX() + 34, bounds.getCenterY() - 9);

        List<Widget> widgets = new ArrayList<>();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createArrow(new Point(bounds.getCenterX() + 3, bounds.getCenterY() - 8)));

        List<EntryIngredient> inputs = display.getInputEntries();
        List<EntryIngredient> output = display.getOutputEntries();
        widgets.add(Widgets.createSlot(outputPoint).entries(output.get(0)));

        int currentEntry = 0;

        int i;
        int j;
        for (i = 0; i < 3; ++i) {
            for (j = 0; j < 3; ++j) {
                //this.addSlot(new Slot(this.input, j + i * 3, 30 + j * 18, 17 + i * 18));
                try {
                    if (currentEntry < inputs.size() - 1 && i < recipe.getHeight() && j < recipe.getWidth()) {
                        widgets.add(Widgets.createSlot(new Point(startPoint.x + j * 18, startPoint.y + i * 18)).entries(inputs.get(currentEntry)));
                        currentEntry++;
                        continue;
                    }
                } catch (IndexOutOfBoundsException ignored) {}
                widgets.add(Widgets.createSlot(new Point(startPoint.x + j * 18, startPoint.y + i * 18)).entries(EntryIngredients.of(ItemStack.EMPTY)));
            }
        }

        widgets.add(Widgets.createSlot(new Point(bounds.getCenterX() + 9, bounds.getCenterY() + 14)).entries(EntryIngredients.ofIngredient(display.RECIPE.getWeavingIngredient())));
        //this.addSlot(new WeavingSlot(this.input, 9, 93, 57));

        return widgets;
    }
}
