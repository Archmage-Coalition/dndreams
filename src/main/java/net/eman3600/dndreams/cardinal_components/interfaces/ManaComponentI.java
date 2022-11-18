package net.eman3600.dndreams.cardinal_components.interfaces;

import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;

public interface ManaComponentI extends ServerTickingComponent {
    int getMana();
    int getBaseManaMax();
    int getManaMax();
    int getXPBonus();
    int getRegenRate();

    void useMana(int cost);

    void setMana(int value);
}
