package net.eman3600.dndreams.cardinal_components.interfaces;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.Vec3d;

public interface DreamingComponentI extends ServerTickingComponent, AutoSyncedComponent {
    void changeDimension(boolean toDream);
    void flagTransference();
    boolean isDreaming();
    PlayerInventory storedInv();
    Vec3d returnPos();
}
