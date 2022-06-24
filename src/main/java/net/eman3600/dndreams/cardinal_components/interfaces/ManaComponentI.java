package net.eman3600.dndreams.cardinal_components.interfaces;

import dev.onyxstudios.cca.api.v3.component.Component;

public interface ManaComponentI extends Component {
    int getMana();
    int getBaseManaMax();
    int getInfusion();
    void setInfusion(int change);

    boolean useMana(int cost);
}
