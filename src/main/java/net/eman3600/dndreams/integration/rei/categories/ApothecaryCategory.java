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
import net.eman3600.dndreams.initializers.basics.ModFluids;
import net.eman3600.dndreams.integration.rei.DnDreamsREIPlugin;
import net.eman3600.dndreams.integration.rei.display.ApothecaryDisplay;
import net.eman3600.dndreams.integration.rei.display.TransmutationDisplay;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ApothecaryCategory implements DisplayCategory<ApothecaryDisplay> {
    public static final Text TITLE = Text.translatable("rei.dndreams.apothecary");
    public static final EntryStack<ItemStack> ICON = EntryStacks.of(ModBlocks.REFINED_CAULDRON);

    @Override
    public CategoryIdentifier<? extends ApothecaryDisplay> getCategoryIdentifier() {
        return DnDreamsREIPlugin.APOTHECARY;
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
    public int getDisplayWidth(ApothecaryDisplay display) {
        return 130;
    }

    @Override
    public List<Widget> setupDisplay(ApothecaryDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(Widgets.createRecipeBase(bounds));

        List<EntryIngredient> inputs = display.getInputEntries();

        if (display.RECIPE.corrupted) {
            widgets.add(Widgets.createSlot(new Point(bounds.getCenterX() - 9, bounds.getCenterY() - 7 - 18)).entries(inputs.get(0)));
            widgets.add(Widgets.createSlot(new Point(bounds.getCenterX() - 9, bounds.getCenterY() - 7)).entries(EntryIngredients.of(Items.FERMENTED_SPIDER_EYE)));
        } else {
            widgets.add(Widgets.createSlot(new Point(bounds.getCenterX() - 9, bounds.getCenterY() - 7)).entries(inputs.get(0)));
        }
        widgets.add(Widgets.createLabel(new Point(bounds.getCenterX(), bounds.getCenterY() + 15), Text.translatable(display.RECIPE.effect.getTranslationKey())).color(0x373737).noShadow());
        widgets.add(Widgets.createLabel(new Point(bounds.getCenterX(), bounds.getCenterY() + 15 + 10), Text.translatable("rei.dndreams.apothecary.capacity", display.RECIPE.capacity)).color(0x373737).noShadow());
        widgets.add(Widgets.createLabel(new Point(bounds.getCenterX(), bounds.getCenterY() + 15 + 20), Text.translatable("rei.dndreams.apothecary.cost", display.RECIPE.power)).color(0x373737).noShadow());


        return widgets;
    }
}
