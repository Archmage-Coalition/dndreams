package net.eman3600.dndreams.cardinal_components.interfaces;

import dev.onyxstudios.cca.api.v3.component.Component;

public interface TormentComponentI extends Component {
    int getTorment();
    int getForcedTorment();
    void setTorment(float value);
    void addTorment(float value);
}
