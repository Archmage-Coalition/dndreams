package net.eman3600.dndreams.items.magic_bow;

import net.minecraft.item.BowItem;

public abstract class MagicBow extends BowItem {
    public MagicBow(Settings settings) {
        super(settings);
    }

    public abstract float pullProgressDivisor();
}
