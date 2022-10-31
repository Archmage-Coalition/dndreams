package net.eman3600.dndreams.items.charge;

import net.eman3600.dndreams.rituals.setup.AbstractRitual;
import net.eman3600.dndreams.rituals.setup.AbstractRitual.CandleTuning;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;

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
