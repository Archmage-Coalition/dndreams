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
import net.eman3600.dndreams.initializers.basics.ModFluids;
import net.eman3600.dndreams.integration.rei.DnDreamsREIPlugin;
import net.eman3600.dndreams.integration.rei.display.TransmutationDisplay;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class TransmutationCategory implements DisplayCategory<TransmutationDisplay> {
    public static final Text TITLE = Text.translatable("rei.dndreams.transmutation");
    public static final EntryStack<ItemStack> ICON = EntryStacks.of(ModFluids.FLOWING_SPIRIT_BUCKET);

    @Override
    public CategoryIdentifier<? extends TransmutationDisplay> getCategoryIdentifier() {
        return DnDreamsREIPlugin.TRANSMUTATION;
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
        return 40;
    }

    @Override
    public int getDisplayWidth(TransmutationDisplay display) {
        return 130;
    }

    @Override
    public List<Widget> setupDisplay(TransmutationDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createArrow(new Point(bounds.getCenterX() - 12, bounds.getCenterY() - 8)));

        List<EntryIngredient> inputs = display.getInputEntries();
        List<EntryIngredient> output = display.getOutputEntries();

        widgets.add(Widgets.createSlot(new Point(bounds.getCenterX() - 36, bounds.getCenterY() - 7)).entries(inputs.get(0)));
        widgets.add(Widgets.createSlot(new Point(bounds.getCenterX() + 18, bounds.getCenterY() - 7)).entries(output.get(0)));

        if (display.RECIPE.realOnly) {
            widgets.add(Widgets.createSlot(new Point(bounds.getCenterX() - 36 - 20, bounds.getCenterY() - 7)).entry(EntryStacks.of(new ItemStack(Items.GRASS_BLOCK).setCustomName(Text.translatable("rei.dndreams.transmutation.real_only")))));
        }

        return widgets;
    }
}
