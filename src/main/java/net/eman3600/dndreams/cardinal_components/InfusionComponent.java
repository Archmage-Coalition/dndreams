package net.eman3600.dndreams.cardinal_components;

import net.eman3600.dndreams.cardinal_components.interfaces.InfusionComponentI;
import net.eman3600.dndreams.infusions.setup.Infusion;
import net.eman3600.dndreams.infusions.setup.InfusionRegistry;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.initializers.entity.ModInfusions;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class InfusionComponent implements InfusionComponentI {
    private static final float MAX_POWER = 100f;

    private final PlayerEntity player;

    private Infusion infusion = ModInfusions.NONE;
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
        return infusion.hasPower;
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

    public float getRoundedPower() {
        return ((int)(power * 10)) / 10f;
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
    public boolean tryResist(DamageSource source, float amount) {
        float cost = amount * .2f;

        if (power >= cost && infusion.resistantTo(amount, source, player)) {
            usePower(cost);
            return true;
        }

        return false;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        infusion = Infusion.ofID(Identifier.tryParse(tag.getString("infusion")));
        power = tag.getFloat("power");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putString("infusion", InfusionRegistry.REGISTRY.getId(infusion).toString());
        tag.putFloat("power", power);
    }

    @Override
    public void serverTick() {
        if (player.world instanceof ServerWorld serverWorld) {
            infusion.serverTick(serverWorld, this, player);
        }
    }
}
