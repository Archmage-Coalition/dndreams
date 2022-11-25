package net.eman3600.dndreams.cardinal_components;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.eman3600.dndreams.cardinal_components.interfaces.TormentComponentI;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.initializers.world.ModDimensions;
import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.eman3600.dndreams.initializers.cca.WorldComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;

public class TormentComponent implements TormentComponentI, AutoSyncedComponent, ServerTickingComponent {
    private float maxSanity = 100;
    private float sanity = 100;
    private int dragonFlashTicks = 0;
    private boolean shielded = false;

    private final PlayerEntity player;

    public static final float MAX_SANITY = 100f;
    public static final float MIN_SANITY = 0f;
    public static final float MIN_MAX_SANITY = 30f;

    public TormentComponent(PlayerEntity playerIn) {
        player = playerIn;
    }

    @Override
    public float getSanity() {
        return sanity;
    }

    @Override
    public float getMaxSanity() {
        return maxSanity;
    }

    private boolean sanityInflexible() {
        return !WorldComponents.BOSS_STATE.get(player.getWorld().getScoreboard()).dragonSlain()
                && player.getWorld().getDimensionKey() != ModDimensions.DREAM_TYPE_KEY;
    }

    @Override
    public boolean isShielded() {
        return shielded;
    }

    public void shield(boolean shielded) {
        this.shielded = shielded;
        EntityComponents.TORMENT.sync(player);
    }

    @Override
    public void setSanity(float value) {
        sanity = value;
        normalize();
    }

    @Override
    public void setMaxSanity(float value) {
        maxSanity = value;
        normalize();
    }

    @Override
    public void lowerSanity(float value) {
        sanity -= value;
        normalize();
    }

    @Override
    public void lowerMaxSanity(float value) {
        maxSanity -= value;
        normalize();
    }

    @Override
    public void lowerPerSecond(float value) {
        lowerSanity(value/20);
    }

    @Override
    public void lowerPerMinute(float value) {
        lowerSanity(value/1200);
    }

    private void normalize() {
        if (sanityInflexible()) {
            maxSanity = MAX_SANITY;
            sanity = getMaxSanity();
        } else {
            maxSanity = MathHelper.clamp(maxSanity, MIN_MAX_SANITY, MAX_SANITY);
            sanity = MathHelper.clamp(sanity, MIN_SANITY, getMaxSanity());
        }
        EntityComponents.TORMENT.sync(player);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        sanity = tag.getFloat("sanity");
        maxSanity = tag.getFloat("max_sanity");
        dragonFlashTicks = tag.getInt("dragon_flash_ticks");
        shielded = tag.getBoolean("shielded");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putFloat("sanity", sanity);
        tag.putFloat("max_sanity", maxSanity);
        tag.putInt("dragon_flash_ticks", dragonFlashTicks);
        tag.putBoolean("shielded", shielded);
    }

    @Override
    public void serverTick() {
        if (!terrorized() && sanity < maxSanity) {
            lowerPerMinute(-7.5f);
        }

        if (WorldComponents.BLOOD_MOON.get(player.world).isBloodMoon()) {
            lowerPerMinute(2f);
        }
    }

    @Override
    public boolean terrorized() {
        return player.hasStatusEffect(ModStatusEffects.LOOMING) || player.world.getRegistryKey() == ModDimensions.DREAM_DIMENSION_KEY || WorldComponents.BLOOD_MOON.get(player.world).isBloodMoon();
    }

    @Override
    public boolean shouldRender() {
        return !sanityInflexible();
    }
}
