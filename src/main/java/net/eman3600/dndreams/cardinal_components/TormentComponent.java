package net.eman3600.dndreams.cardinal_components;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.eman3600.dndreams.cardinal_components.interfaces.TormentComponentI;
import net.eman3600.dndreams.initializers.EntityComponents;
import net.eman3600.dndreams.initializers.ModStatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class TormentComponent implements TormentComponentI, AutoSyncedComponent {
    private float torment = 0;

    private PlayerEntity player;

    public static final float MAX_TORMENT = 100;

    public TormentComponent(PlayerEntity playerIn) {
        player = playerIn;
    }

    @Override
    public float getTorment() {
        if (isTormentForced()) {
            return getForcedTorment();
        }

        return torment;
    }

    @Override
    public float getTrueTorment() {
        return torment;
    }

    private int getForcedTorment() {
        if (player.hasStatusEffect(ModStatusEffects.LOOMING)) {
            return 100;
        } else if (player.hasStatusEffect(ModStatusEffects.SPIRIT_WARD)) {
            return 0;
        }

        return -1;
    }

    @Override
    public boolean isTormentForced() {
        return getForcedTorment() != -1;
    }

    @Override
    public void setTorment(float value) {
        torment = value;
        normalize();
    }

    @Override
    public void addTorment(float value) {
        torment += value;
        normalize();
    }

    @Override
    public void addPerSecond(float value) {
        addTorment(value/20);
    }

    @Override
    public void addPerMinute(float value) {
        addTorment(value/1200);
    }

    private void normalize() {
        torment = Math.max(torment, 0);
        torment = Math.min(torment, MAX_TORMENT);
        EntityComponents.TORMENT.sync(player);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        torment = tag.getFloat("torment");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putFloat("torment", torment);
    }
}
