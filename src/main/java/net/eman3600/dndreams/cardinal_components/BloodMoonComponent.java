package net.eman3600.dndreams.cardinal_components;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.eman3600.dndreams.cardinal_components.interfaces.BloodMoonComponentI;
import net.eman3600.dndreams.initializers.world.ModDimensions;
import net.eman3600.dndreams.initializers.cca.WorldComponents;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;

import java.util.Random;

public class BloodMoonComponent implements BloodMoonComponentI, AutoSyncedComponent {
    private int chance = 100;
    private long knownDay = -1;
    private boolean damnedNight = false;

    private World world;
    private Object provider;

    private final int CUMULATIVE_CHANCE = 3;
    private Random random = new Random();

    public BloodMoonComponent(World worldIn) {
        world = worldIn;
    }

    private RegistryKey<DimensionType> getDimensionKey() {
        return world.getDimensionKey();
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
        if (getDimensionKey() == DimensionTypes.OVERWORLD) {
            return damnedNight;
        } else if (getDimensionKey() == ModDimensions.DREAM_TYPE_KEY) {
            return world.getTimeOfDay() >= 18000;
        }
        return false;
    }

    @Override
    public void serverTick() {
        long day = world.getTimeOfDay() / 24000;
        if (knownDay != day) {
            knownDay = day;
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
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putInt("chance", chance);
        tag.putLong("known_day", knownDay);
        tag.putBoolean("damned_night", damnedNight);
    }
}
