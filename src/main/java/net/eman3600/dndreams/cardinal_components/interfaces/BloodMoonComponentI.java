package net.eman3600.dndreams.cardinal_components.interfaces;

import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;

public interface BloodMoonComponentI extends ServerTickingComponent {
    int getChance();
    long getKnownDay();

    void setDamnedNight(boolean damnedNight);
    boolean damnedNight();
}
