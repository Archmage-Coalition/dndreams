package net.eman3600.dndreams.cardinal_components;

import net.eman3600.dndreams.cardinal_components.interfaces.InfusionComponentI;
import net.eman3600.dndreams.infusions.Infusion;
import net.eman3600.dndreams.initializers.EntityComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;

public class InfusionComponent implements InfusionComponentI {
    private static final float MAX_POWER = 100f;
    private static final float DIMENSIONAL_REGEN_RATE = 10f;

    private final PlayerEntity player;

    private Infusion infusion = Infusion.NONE;
    private float power = 0f;

    public InfusionComponent(PlayerEntity player) {
        this.player = player;
    }

    @Override
    public Infusion getInfusion() {
        return infusion;
    }

    @Override
    public boolean infused() {
        return infusion.isInfused();
    }

    @Override
    public void setInfusion(Infusion change) {
        infusion = change;
        setPower(getPowerMax());
    }

    @Override
    public float getPower() {
        return power;
    }

    @Override
    public float getPowerMax() {
        return infused() ? MAX_POWER : 0;
    }

    @Override
    public void setPower(float value) {
        power = value;
        normalize();
    }

    @Override
    public void chargePower(float charge) {
        power += charge;
        normalize();
    }

    @Override
    public void usePower(float cost) {
        power -= cost;
        normalize();
    }

    private void normalize() {
        power = MathHelper.clamp(power, 0, MAX_POWER);

        EntityComponents.INFUSION.sync(player);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        infusion = Infusion.getFromNum(tag.getInt("infusion"));
        power = tag.getFloat("power");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putInt("infusion", infusion.getNum());
        tag.putFloat("power", power);
    }

    @Override
    public void serverTick() {
        if (player.getWorld().getRegistryKey() == infusion.getWorld() && power < MAX_POWER) {
            chargePower(DIMENSIONAL_REGEN_RATE/1200);
        }
    }
}
