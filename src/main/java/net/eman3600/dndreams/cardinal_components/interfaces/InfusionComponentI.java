package net.eman3600.dndreams.cardinal_components.interfaces;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.eman3600.dndreams.infusions.Infusion;

public interface InfusionComponentI extends AutoSyncedComponent, ServerTickingComponent {
    Infusion getInfusion();
    boolean infused();
    void setInfusion(Infusion change);
    float getPower();
    float getPowerMax();
    void setPower(float value);
    void chargePower(float charge);
    void usePower(float cost);
    float getRoundedPower();
}
