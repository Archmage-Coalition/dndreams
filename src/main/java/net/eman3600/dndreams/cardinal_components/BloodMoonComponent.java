package net.eman3600.dndreams.cardinal_components;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.eman3600.dndreams.cardinal_components.interfaces.BloodMoonComponentI;
import net.eman3600.dndreams.initializers.WorldComponents;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

import java.util.Random;

public class BloodMoonComponent implements BloodMoonComponentI, AutoSyncedComponent {
    private int chance = 100;
    private long knownDay = -1;
    private boolean damnedNight = false;
    private boolean isNight = false;

    private final int CUMULATIVE_CHANCE = 4;
    private Random random = new Random();

    private final Object provider;

    public BloodMoonComponent(Object provider) {
        this.provider = provider;
    }

    @Override
    public int getChance() {
        return chance;
    }

    @Override
    public long getKnownDay() {
        return knownDay;
    }

    @Override
    public boolean damnedNight() {
        return damnedNight;
    }

    @Override
    public void tick(boolean nighttime) {
        if (!isNight && nighttime) {
            isNight = true;
        }
        if (isNight && !nighttime) {
            isNight = false;
            knownDay++;
            if (damnedNight) {
                chance = 0;
                damnedNight = false;
            } else if (random.nextInt(100) < chance) {
                damnedNight = true;
            } else {
                chance += CUMULATIVE_CHANCE;
            }
            WorldComponents.BLOOD_MOON.sync(provider);
        }
    }

    @Override
    public boolean manualStart() {
        if (damnedNight) {
            return false;
        }
        damnedNight = true;
        return true;
    }

    public static boolean isBloodMoon(World world) {
        BloodMoonComponent component = WorldComponents.BLOOD_MOON.get(world);

        return component.damnedNight() && world.isNight();
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        chance = tag.getInt("chance");
        knownDay = tag.getLong("known_day");
        damnedNight = tag.getBoolean("damned_night");
        isNight = tag.getBoolean("is_night");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putInt("chance", chance);
        tag.putLong("known_day", knownDay);
        tag.putBoolean("damned_night", damnedNight);
        tag.putBoolean("is_night", isNight);
    }
}
