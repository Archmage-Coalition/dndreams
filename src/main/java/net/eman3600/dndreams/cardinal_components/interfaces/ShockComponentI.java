package net.eman3600.dndreams.cardinal_components.interfaces;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;

import javax.annotation.Nonnegative;

public interface ShockComponentI extends AutoSyncedComponent, ServerTickingComponent {
    void markDirty();

    void chargeShock(@Nonnegative float amount);

    boolean hasShock();

    float dischargeShock();

    float dischargeShock(@Nonnegative float max);
}
