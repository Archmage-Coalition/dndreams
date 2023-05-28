package net.eman3600.dndreams.staves.setup;

import net.minecraft.item.Item;
import net.minecraft.util.Util;
import net.minecraft.util.registry.RegistryEntry;

public class StaffHandle {
    private final RegistryEntry.Reference<StaffHandle> entry;

    public final Item material;
    public final int addedCost;
    public final float durabilityMult;

    public StaffHandle(Item material, int addedCost, float durabilityMult) {
        this.material = material;
        this.addedCost = addedCost;
        this.durabilityMult = durabilityMult;

        this.entry = StaffHandleRegistry.REGISTRY.createEntry(this);
    }

    public RegistryEntry.Reference<StaffHandle> reference() {
        return entry;
    }

    public String toString() {
        return StaffHandleRegistry.REGISTRY.getId(this).getPath();
    }

    public String getTranslationKey() {
        return Util.createTranslationKey("item", StaffHandleRegistry.REGISTRY.getId(this));
    }
}
