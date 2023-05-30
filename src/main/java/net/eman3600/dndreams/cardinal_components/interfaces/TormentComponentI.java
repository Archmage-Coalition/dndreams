package net.eman3600.dndreams.cardinal_components.interfaces;

import dev.onyxstudios.cca.api.v3.component.Component;

public interface TormentComponentI extends Component {
    float getSanity();
    float getAttunedSanity();
    float getMaxSanity();

    boolean isShielded();

    void setSanity(float value);
    void setMaxSanity(float value);
    void lowerSanity(float value);
    void lowerMaxSanity(float value);

    float getSanityDamage();

    void damageSanity(int increment);

    boolean shearSanity(float value, boolean yield);

    void lowerPerSecond(float value);
    void lowerPerMinute(float value);

    boolean shouldOffsetRender();

    boolean isAttuned();

    boolean isAwakened();

    int getShroud();

    int getHaunt();

    boolean canAfford(float cost);
}
