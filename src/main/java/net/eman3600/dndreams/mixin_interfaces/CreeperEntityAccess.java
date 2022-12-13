package net.eman3600.dndreams.mixin_interfaces;

import net.minecraft.entity.data.TrackedData;

public interface CreeperEntityAccess {
    TrackedData<Boolean> getChargedTracker();
}
