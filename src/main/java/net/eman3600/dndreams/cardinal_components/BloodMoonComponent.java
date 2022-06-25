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
    private boolean isNight = true;

    private World world;
    private Object provider;

    private final int CUMULATIVE_CHANCE = 3;
    private Random random = new Random();

    public BloodMoonComponent(World worldIn) {
        world = worldIn;
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
    public void serverTick() {
        boolean nighttime = world.isNight();
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
        }
        WorldComponents.BLOOD_MOON.sync(world);
    }

    @Override
    public boolean manualStart() {
        if (damnedNight) {
            return false;
        }
        damnedNight = true;
        return true;
    }

    public boolean isBloodMoon() {
        return damnedNight() && world.isNight();
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
