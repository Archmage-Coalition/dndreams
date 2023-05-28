package net.eman3600.dndreams.staves.prongs;

import net.eman3600.dndreams.staves.setup.StaffProng;
import net.minecraft.item.Items;

public class AmethystStaffProng extends StaffProng {
    public AmethystStaffProng() {
        super(Items.AMETHYST_SHARD);
    }

    @Override
    public int getManaCost(int baseCost) {
        return baseCost;
    }

    @Override
    public float getSanityCost(int baseCost) {
        return 0;
    }
}
