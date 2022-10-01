package net.eman3600.dndreams.mixin_interfaces;

public interface ItemEntityInterface {

    boolean getFloating();
    void setFloating(boolean floating);
    int getWindupTicks();
    void setWindupTicks(int ticks);

    default void incrementWindupTicks(int amount) {
        setWindupTicks(getWindupTicks() + amount);
    }


}
