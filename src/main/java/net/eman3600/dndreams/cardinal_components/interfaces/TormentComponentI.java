package net.eman3600.dndreams.cardinal_components.interfaces;

import dev.onyxstudios.cca.api.v3.component.Component;

public interface TormentComponentI extends Component {
    float getSanity();

    /**
     * Returns the effective sanity of the player.
     * Sanity is divided into 6 tiers:
     * <p>
     * 1. sanity >= 85
     * <p>
     * 2. 85 > sanity >= 65
     * <p>
     * 3. 65 > sanity >= 45
     * <p>
     * 4. 45 > sanity >= 25
     * <p>
     * 5. 25 > sanity >= 5
     * <p>
     * 6. sanity < 5
     *
     * @return effective sanity
     */
    float getAttunedSanity();
    float getTrueMaxSanity();

    float getMaxSanity();

    void setSanity(float value);
    void setMaxSanity(float value);

    void lowerSanity(float value);
    void lowerMaxSanity(float value);

    float getNightmareHaze();

    void inflictHaze(int increment);

    boolean shearSanity(float value, boolean yield);

    void lowerPerSecond(float value);
    void lowerPerMinute(float value);

    boolean shouldOffsetRender();

    boolean isAttuned();

    boolean isAwakened();

    int getShroud();

    int getHaunt();

    boolean canAfford(float cost);

    void spendSanity(float cost);

    int getTension();

    int getEffectiveTension();

    void addTension(int tension);

    float getFacelessPrevalence();
}
