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
import net.eman3600.dndreams.initializers.basics.ModBlocks;
import net.eman3600.dndreams.integration.rei.DnDreamsREIPlugin;
import net.eman3600.dndreams.integration.rei.display.AttunementDisplay;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class AttunementCategory implements DisplayCategory<AttunementDisplay> {
    public static final Text TITLE = Text.translatable("rei.dndreams.attunement");
    public static final EntryStack<ItemStack> ICON = EntryStacks.of(ModBlocks.ATTUNEMENT_CHAMBER);

    @Override
    public CategoryIdentifier<? extends AttunementDisplay> getCategoryIdentifier() {
        return DnDreamsREIPlugin.ATTUNEMENT;
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
    public int getDisplayWidth(AttunementDisplay display) {
        return 130;
    }

    @Override
    public List<Widget> setupDisplay(AttunementDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createArrow(new Point(bounds.getCenterX() - 12 + 5, bounds.getCenterY() - 8)));

        widgets.add(Widgets.createSlot(new Point(bounds.getCenterX() - 36 + 5, bounds.getCenterY() - 7)).entries(display.getIn()));
        widgets.add(Widgets.createSlot(new Point(bounds.getCenterX() + 18 + 5, bounds.getCenterY() - 7)).entries(display.getOut()));

        return widgets;
    }
}
