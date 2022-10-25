package net.eman3600.dndreams.blocks.entities;

public interface AbstractPowerReceiver {
    boolean addPower(int amount);
    void setPower(int amount);
    int getPower();
    boolean canAfford(int amount);
    boolean usePower(int amount);

    boolean needsPower();
    int powerRequest();
}
