package net.eman3600.dndreams.cardinal_components;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.eman3600.dndreams.cardinal_components.interfaces.TormentComponentI;
import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.initializers.world.ModDimensions;
import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.eman3600.dndreams.initializers.cca.WorldComponents;
import net.eman3600.dndreams.util.ModArmorMaterials;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class TormentComponent implements TormentComponentI, AutoSyncedComponent, ServerTickingComponent {
    private float maxSanity = 100;
    private float sanity = 100;
    public static final float THREAD_VALUE = 3.5f;
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
    public boolean shearSanity(float value, boolean yield) {
        boolean bl = false;
        while (value >= THREAD_VALUE) {
            ItemStack stack;
            if (getMaxSanity() - THREAD_VALUE >= MIN_MAX_SANITY && player.world.getRegistryKey() != ModDimensions.DREAM_DIMENSION_KEY) {
                bl = true;
                lowerMaxSanity(THREAD_VALUE);
                stack = ModItems.SANITY_THREAD.getDefaultStack();
            } else if (getSanity() - THREAD_VALUE >= MIN_SANITY) {
                bl = true;
                lowerSanity(THREAD_VALUE);
                stack = ModItems.DREAM_POWDER.getDefaultStack();
            } else {
                break;
            }

            if (yield) {
                Vec3d vec = player.getPos().add(0, 1.5, 0);

                ItemEntity item = new ItemEntity(player.world, vec.x, vec.y, vec.z, stack);
                item.addVelocity(0, 0.15D, 0);
                item.setPickupDelay(20);

                player.world.spawnEntity(item);
            }

            value -= THREAD_VALUE;
        }


        normalize();
        return bl;
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
        if (sanity < maxSanity) {
            int i = ModArmorMaterials.getEquipCount(player, ModArmorMaterials.CELESTIUM);
            if (i > 0) {
                lowerPerMinute(-1.25f * i);
            }
        }

        if (WorldComponents.BLOOD_MOON.get(player.world).isBloodMoon()) {
            lowerPerMinute(2f);
        }

        int i = equippedTormite();
        if (i > 0) {
            lowerPerMinute(.15f * i);
        }
    }

    private int equippedTormite() {
        return ModArmorMaterials.getEquipCount(player, ModArmorMaterials.TORMITE);
    }

    @Override
    public boolean shouldRender() {
        return !sanityInflexible();
    }
}
