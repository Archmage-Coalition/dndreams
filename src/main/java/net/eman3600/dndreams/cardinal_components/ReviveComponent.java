package net.eman3600.dndreams.cardinal_components;

import net.eman3600.dndreams.cardinal_components.interfaces.ReviveComponentI;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class ReviveComponent implements ReviveComponentI {

    /**
     * If the player can use revives
     */
    private boolean enabled = false;
    /**
     * The player's revive pool. Regenerates very slowly, used as a cooldown when reviving too often.
     */
    private float pool = 100;
    /**
     * Whether the player needs to let their revive pool refill before regaining revives.
     */
    private boolean canCharge = true;
    /**
     * The player's revive charge, used as a cooldown on singular revives.
     */
    private float charge = 100;

    private final PlayerEntity player;

    public ReviveComponent(PlayerEntity player) {
        this.player = player;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean canRevive() {
        return enabled && canCharge && charge >= 100f;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        enabled = tag.getBoolean("enabled");
        pool = tag.getFloat("pool");
        charge = tag.getFloat("charge");
        canCharge = tag.getBoolean("can_charge");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putBoolean("enabled", enabled);
        tag.putFloat("pool", pool);
        tag.putFloat("charge", charge);
        tag.putBoolean("can_charge", canCharge);
    }

    @Override
    public void serverTick() {

    }
}
