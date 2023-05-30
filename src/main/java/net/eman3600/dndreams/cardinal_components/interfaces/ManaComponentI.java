package net.eman3600.dndreams.cardinal_components.interfaces;

import dev.onyxstudios.cca.api.v3.component.tick.ClientTickingComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;

public interface ManaComponentI extends ServerTickingComponent, ClientTickingComponent {
    int getMana();
    int getBaseManaMax();
    int getManaMax();
    int getXPBonus();
    int getRegenRate();

    boolean canAfford(int cost);

    boolean shouldRender();

    void useMana(int cost);

    void setMana(int value);

    int getManaFrame();
}
