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
import net.eman3600.dndreams.integration.rei.display.RitualDisplay;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;

public class RitualCategory implements DisplayCategory<RitualDisplay> {
    public static final Text TITLE = Text.translatable("rei.dndreams.ritual");
    public static final EntryStack<ItemStack> ICON = EntryStacks.of(ModBlocks.BONFIRE);

    @Override
    public CategoryIdentifier<? extends RitualDisplay> getCategoryIdentifier() {
        return DnDreamsREIPlugin.RITUAL;
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
    public int getDisplayWidth(RitualDisplay display) {
        return 170;
    }

    @Override
    public List<Widget> setupDisplay(RitualDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(Widgets.createRecipeBase(bounds));

        List<EntryIngredient> inputs = display.getInputEntries();
        List<EntryIngredient> output = display.getOutputEntries();

        int columns = MathHelper.ceil(inputs.size() / 3f);
        int i = 0;

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < columns; col++) {
                EntryIngredient ingredient;
                try {
                    ingredient = inputs.get(i);
                } catch (IndexOutOfBoundsException e) {
                    ingredient = EntryIngredient.empty();
                }
                i++;

                widgets.add(Widgets.createSlot(new Point(bounds.getCenterX() + 3 - 24 - (18 * (columns - col - 1)), bounds.getCenterY() - 25 + (row * 18))).entries(ingredient));
            }
        }

        if (!display.RECIPE.getOutput().isEmpty()) {
            widgets.add(Widgets.createArrow(new Point(bounds.getCenterX() + 3, bounds.getCenterY() - 8)));
            widgets.add(Widgets.createSlot(new Point(bounds.getCenterX() + 3 + 30, bounds.getCenterY() - 7)).entries(output.get(0)));
        }

        widgets.add(Widgets.createLabel(new Point(bounds.getCenterX(), bounds.getCenterY() - 38), Text.translatable(display.RECIPE.getRitual().getTranslationKey())).color(0xFF404040, 0xFFBBBBBB).noShadow());

        return widgets;
    }
}
