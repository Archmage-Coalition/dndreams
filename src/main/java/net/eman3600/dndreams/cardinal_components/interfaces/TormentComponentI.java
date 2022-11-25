package net.eman3600.dndreams.cardinal_components.interfaces;

import dev.onyxstudios.cca.api.v3.component.Component;

public interface TormentComponentI extends Component {
    float getSanity();
    float getMaxSanity();

    boolean isShielded();

    void setSanity(float value);
    void setMaxSanity(float value);
    void lowerSanity(float value);
    void lowerMaxSanity(float value);

    void lowerPerSecond(float value);
    void lowerPerMinute(float value);

    boolean terrorized();
    boolean shouldRender();
}
