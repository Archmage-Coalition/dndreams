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
import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.integration.rei.DnDreamsREIPlugin;
import net.eman3600.dndreams.integration.rei.display.SmokestackDisplay;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class SmokestackCategory implements DisplayCategory<SmokestackDisplay> {
    public static final Text TITLE = Text.translatable("rei.dndreams.smokestack");
    public static final EntryStack<ItemStack> ICON = EntryStacks.of(ModBlocks.SMOKESTACK);

    @Override
    public CategoryIdentifier<? extends SmokestackDisplay> getCategoryIdentifier() {
        return DnDreamsREIPlugin.SMOKESTACK;
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
    public int getDisplayWidth(SmokestackDisplay display) {
        return 130;
    }

    @Override
    public List<Widget> setupDisplay(SmokestackDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createArrow(new Point(bounds.getCenterX() - 12 + 5, bounds.getCenterY() - 8 - 10)).animationDurationTicks(100));

        List<EntryIngredient> inputs = display.getInputEntries();
        List<EntryIngredient> output = display.getOutputEntries();

        widgets.add(Widgets.createSlot(new Point(bounds.getCenterX() - 36 + 5, bounds.getCenterY() - 7 - 10)).entry(EntryStacks.of(ModItems.AMETHYST_JAR)));
        widgets.add(Widgets.createSlot(new Point(bounds.getCenterX() + 18 + 5, bounds.getCenterY() - 7 - 10)).entries(output.get(0)));
        widgets.add(Widgets.createSlot(new Point(bounds.getCenterX() - 36 + 5, bounds.getCenterY() + 11)).entries(inputs.get(0)));

        widgets.add(Widgets.createSlot(new Point(bounds.getCenterX() - 36 - 20 + 5, bounds.getCenterY() + 11)).entry(EntryStacks.of(new ItemStack(Items.SMOKER).setCustomName(Text.translatable("rei.dndreams.smokestack.smoker")))));

        widgets.add(Widgets.createBurningFire(new Point(bounds.getCenterX() - 36 + 20 + 5, bounds.getCenterY() + 11)).animationDurationTicks(100));

        return widgets;
    }
}
