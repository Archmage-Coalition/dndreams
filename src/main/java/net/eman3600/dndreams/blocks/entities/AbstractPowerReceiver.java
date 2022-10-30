package net.eman3600.dndreams.blocks.entities;

public interface AbstractPowerReceiver {
    boolean addPower(int amount);
    void setPower(int amount);
    int getPower();
    int getMaxPower();
    default boolean canAfford(int amount) {
        return getPower() >= amount;
    };
    boolean usePower(int amount);

    boolean needsPower();
    int powerRequest();
}
