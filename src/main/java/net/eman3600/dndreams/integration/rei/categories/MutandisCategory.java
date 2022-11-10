package net.eman3600.dndreams.integration.rei.categories;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.integration.rei.DnDreamsREIPlugin;
import net.eman3600.dndreams.integration.rei.display.MutandisDisplay;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class MutandisCategory implements DisplayCategory<MutandisDisplay> {
    public static final Text TITLE = Text.translatable("rei.dndreams.mutandis");
    public static final EntryStack<ItemStack> ICON = EntryStacks.of(ModItems.MUTANDIS);

    @Override
    public CategoryIdentifier<? extends MutandisDisplay> getCategoryIdentifier() {
        return DnDreamsREIPlugin.MUTANDIS;
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
    public int getDisplayWidth(MutandisDisplay display) {
        return 130;
    }

    @Override
    public List<Widget> setupDisplay(MutandisDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createArrow(new Point(bounds.getCenterX() - 12 + 5, bounds.getCenterY() - 8)));

        widgets.add(Widgets.createSlot(new Point(bounds.getCenterX() - 36 + 5, bounds.getCenterY() - 7)).entries(display.getIn()));
        widgets.add(Widgets.createSlot(new Point(bounds.getCenterX() + 18 + 5, bounds.getCenterY() - 7)).entries(display.getOut()));

        return widgets;
    }
}
