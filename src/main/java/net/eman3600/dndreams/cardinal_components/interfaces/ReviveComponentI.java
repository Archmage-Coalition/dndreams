package net.eman3600.dndreams.cardinal_components.interfaces;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;

public interface ReviveComponentI extends AutoSyncedComponent, ServerTickingComponent {

    boolean isEnabled();

    void setEnabled(boolean enabled);

    boolean canRevive();
}
