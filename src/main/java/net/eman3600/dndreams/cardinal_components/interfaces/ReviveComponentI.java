package net.eman3600.dndreams.cardinal_components.interfaces;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;

public interface ReviveComponentI extends AutoSyncedComponent, ServerTickingComponent {

    boolean isEnabled();
    void setEnabled(boolean enabled);

    int maxRevives();
    boolean canRevive();
    int remainingRevives();
    boolean shouldDisplay();

    boolean shouldOffsetRender();

    void revive();

    void deathReset();

    void addVitality(float amount);
    float getVitality();
    float getDisplayedVitality();
    boolean needsMoreVitality();

    boolean canRecharge();

    void setRecharging(boolean canRecharge);
}
