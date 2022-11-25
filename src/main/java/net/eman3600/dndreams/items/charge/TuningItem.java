package net.eman3600.dndreams.items.charge;

import net.eman3600.dndreams.rituals.setup.Ritual.CandleTuning;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class TuningItem extends Item {
    private final CandleTuning tuning;

    public TuningItem(CandleTuning tuning, Settings settings) {
        super(settings);
        this.tuning = tuning;
    }

    public CandleTuning getTuning() {
        return tuning;
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}
