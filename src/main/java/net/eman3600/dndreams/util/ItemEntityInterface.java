package net.eman3600.dndreams.util;

public interface ItemEntityInterface {

    int getWindupTicks();
    void setWindupTicks(int ticks);

    default void incrementWindupTicks(int amount) {
        setWindupTicks(getWindupTicks() + amount);
    }


}
