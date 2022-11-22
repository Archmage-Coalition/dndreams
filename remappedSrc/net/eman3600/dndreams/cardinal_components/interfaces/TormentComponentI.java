package net.eman3600.dndreams.cardinal_components.interfaces;

import dev.onyxstudios.cca.api.v3.component.Component;

public interface TormentComponentI extends Component {
    float getTorment();

    boolean isShielded();

    void setTorment(float value);
    void addTorment(float value);

    void addPerSecond(float value);
    void addPerMinute(float value);

    boolean terrorized();

    boolean isRushed();
}
