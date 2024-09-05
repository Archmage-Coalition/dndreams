package net.eman3600.dndreams.mixin.client;

import net.eman3600.dndreams.mixin_interfaces.MusicTrackerAccess;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.MusicTracker;
import net.minecraft.item.MusicDiscItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Environment(EnvType.CLIENT)
@Mixin(MusicTracker.class)
public abstract class MusicTrackerMixin implements MusicTrackerAccess {
    @Shadow public abstract void stop();

    @Shadow private int timeUntilNextSong;

    @Override
    public void stopFor(MusicDiscItem disc) {
        stop();
        this.timeUntilNextSong = Math.max(100 + disc.getSongLengthInTicks(), timeUntilNextSong);
    }
}
