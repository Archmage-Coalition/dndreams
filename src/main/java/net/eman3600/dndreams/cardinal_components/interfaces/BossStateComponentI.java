package net.eman3600.dndreams.cardinal_components.interfaces;

import dev.onyxstudios.cca.api.v3.component.Component;

public interface BossStateComponentI extends Component {
    boolean dragonSlain();
    void flagDragonSlain(boolean flag);

    boolean witherSlain();
    void flagWitherSlain(boolean flag);


    boolean gatewaysSlain();
}
