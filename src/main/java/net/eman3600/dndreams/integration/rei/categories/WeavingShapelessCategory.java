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
import net.eman3600.dndreams.initializers.ModBlocks;
import net.eman3600.dndreams.integration.rei.DnDreamsREIPlugin;
import net.eman3600.dndreams.integration.rei.display.WeavingShapelessDisplay;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class WeavingShapelessCategory implements DisplayCategory<WeavingShapelessDisplay> {
    public static final Text TITLE = Text.translatable("rei.dndreams.weaving_shapeless");
    public static final EntryStack<ItemStack> ICON = EntryStacks.of(ModBlocks.DREAM_TABLE);

    @Override
    public CategoryIdentifier<? extends WeavingShapelessDisplay> getCategoryIdentifier() {
        return DnDreamsREIPlugin.WEAVING_SHAPELESS;
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
        return 100;
    }

    @Override
    public List<Widget> setupDisplay(WeavingShapelessDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 60, bounds.getCenterY() - 27);
        Point outputPoint = new Point(bounds.getCenterX() + 34, bounds.getCenterY() - 9);

        List<Widget> widgets = new ArrayList<>();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createArrow(new Point(bounds.getCenterX() + 3, bounds.getCenterY() - 8)));

        //this.addSlot(new WeavingResultSlot(playerInventory.player, this.input, this.result, 0, 124, 35));
        List<EntryIngredient> inputs = display.getInputEntries();
        List<EntryIngredient> output = display.getOutputEntries();
        widgets.add(Widgets.createSlot(outputPoint).entries(output.get(0)));



        int i;
        int j;
        for (i = 0; i < 3; ++i) {
            for (j = 0; j < 3; ++j) {
                //this.addSlot(new Slot(this.input, j + i * 3, 30 + j * 18, 17 + i * 18));
                if (j + i * 3 < inputs.size() - 1) {
                    widgets.add(Widgets.createSlot(new Point(startPoint.x + j * 18, startPoint.y + i * 18)).entries(inputs.get(j + i * 3)));
                } else {
                    widgets.add(Widgets.createSlot(new Point(startPoint.x + j * 18, startPoint.y + i * 18)).entries(EntryIngredients.of(ItemStack.EMPTY)));
                }
            }
        }

        widgets.add(Widgets.createSlot(new Point(bounds.getCenterX() + 9, bounds.getCenterY() + 14)).entries(EntryIngredients.ofIngredient(display.RECIPE.getWeavingIngredient())));
        //this.addSlot(new WeavingSlot(this.input, 9, 93, 57));

        return widgets;
    }
}
