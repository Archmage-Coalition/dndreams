package net.eman3600.dndreams.mixin_interfaces;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.MusicDiscItem;

@Environment(EnvType.CLIENT)
public interface MusicTrackerAccess {
    void stopFor(MusicDiscItem disc);
}
