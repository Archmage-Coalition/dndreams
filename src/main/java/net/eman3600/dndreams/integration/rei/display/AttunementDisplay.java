package net.eman3600.dndreams.integration.rei.display;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.integration.rei.DnDreamsREIPlugin;
import net.eman3600.dndreams.items.charge.AttunedShardItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.collection.DefaultedList;

import java.util.List;

public class AttunementDisplay extends BasicDisplay {

    public AttunementDisplay(EntryStack<?> in, int charge) {
        this(ofInput(in), charge);
    }

    public AttunementDisplay(List<EntryIngredient> inputs, int charge) {
        this(inputs, ofOutput(charge));
    }

    public AttunementDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
        super(inputs, outputs);
    }

    private static List<EntryIngredient> ofInput(EntryStack<?> in) {
        DefaultedList<EntryIngredient> list = DefaultedList.ofSize(3, EntryIngredient.of(in));

        list.set(1, EntryIngredient.of(EntryStacks.of(new ItemStack(Items.AMETHYST_SHARD))));
        list.set(2, EntryIngredient.of(EntryStacks.of(new ItemStack(ModItems.ATTUNED_SHARD))));

        return list;
    }

    private static List<EntryIngredient> ofOutput(int charge) {
        DefaultedList<EntryIngredient> list = DefaultedList.ofSize(2, EntryIngredient.of(EntryStacks.of(new ItemStack(ModItems.CHARGED_SHARD))));

        list.set(0, EntryIngredient.of(EntryIngredient.of(EntryStacks.of(AttunedShardItem.ofCharge(charge)))));

        return list;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return DnDreamsREIPlugin.ATTUNEMENT;
    }

    public final EntryIngredient getIn() {
        return getInputEntries().get(0);
    }

    public final EntryIngredient getOut() {
        return getOutputEntries().get(0);
    }

    public static Serializer<AttunementDisplay> serializer() {
        return Serializer.ofSimpleRecipeLess(AttunementDisplay::new);
    }
}
