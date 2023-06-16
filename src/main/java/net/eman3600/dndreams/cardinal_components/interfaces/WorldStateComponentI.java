package net.eman3600.dndreams.cardinal_components.interfaces;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;

public interface WorldStateComponentI extends AutoSyncedComponent, ServerTickingComponent {

    boolean isCustom();
    long getDayTime();
    float getRainGradient();
    float getThunderGradient();
}
