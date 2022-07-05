package net.eman3600.dndreams.items.mindstring_bow;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class LightstringBow extends MindstringBow {
    public LightstringBow(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack getProjectile() {
        return new ItemStack(Items.SPECTRAL_ARROW);
    }

    @Override
    public int getManaCost() {
        return 9;
    }

    public float pullProgressDivisor() {
        return 10.0F;
    }
}
