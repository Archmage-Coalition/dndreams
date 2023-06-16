package net.eman3600.dndreams.cardinal_components;

import net.eman3600.dndreams.cardinal_components.interfaces.WorldStateComponentI;
import net.eman3600.dndreams.initializers.cca.WorldComponents;
import net.eman3600.dndreams.initializers.world.ModDimensions;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public class WorldStateComponent implements WorldStateComponentI {

    private final World world;
    private float rainGradient = 0;
    private float thunderGradient = 0;
    private long dayTime = 0;
    private boolean dirty = false;

    public WorldStateComponent(World world) {
        this.world = world;
    }

    @Override
    public boolean isCustom() {
        return world.getRegistryKey() == ModDimensions.HAVEN_DIMENSION_KEY;
    }

    @Override
    public long getDayTime() {
        return dayTime;
    }

    @Override
    public float getRainGradient() {
        return rainGradient;
    }

    @Override
    public float getThunderGradient() {
        return thunderGradient;
    }

    private long getProperDayTime() {
        if (world instanceof ServerWorld serverWorld) {
            World overworld = serverWorld.getServer().getWorld(World.OVERWORLD);
            if (overworld == null) return 6000L;

            BloodMoonComponent bloodMoon = WorldComponents.BLOOD_MOON.get(overworld);

            if (bloodMoon.isBloodMoon()) return overworld.getTime() % 24000;
            else if (bloodMoon.damnedNight()) return 12200L;
            else return 6000L + (60L * bloodMoon.getChance());
        }

        return 6000L;
    }

    private float getProperRain() {
        Scoreboard board = world.getScoreboard();

        if (board != null) {
            return WorldComponents.BOSS_STATE.get(board).elrunezSlain() ? 0 : 1f;
        }

        return 0;
    }

    private float getProperThunder() {
        if (world instanceof ServerWorld serverWorld) {
            World overworld = serverWorld.getServer().getWorld(World.OVERWORLD);
            if (overworld == null) return 6000L;

            BloodMoonComponent bloodMoon = WorldComponents.BLOOD_MOON.get(overworld);

            return Math.min(getProperRain(), bloodMoon.damnedNight() ? 1f : 0f);
        }

        return 0;
    }

    @Override
    public void serverTick() {

        long properTime = getProperDayTime();

        if (dayTime < properTime) {

            dayTime = Math.min(properTime, dayTime + 6);
            markDirty();
        } else if (dayTime > properTime) {
            dayTime = Math.max(properTime, dayTime - 6);
            markDirty();
        }

        float properRain = getProperRain();

        if (rainGradient < properRain) {
            rainGradient = Math.min(properRain, rainGradient + 0.01f);
            markDirty();
        } else if (rainGradient > properRain) {
            rainGradient = Math.max(properRain, rainGradient - 0.01f);
            markDirty();
        }

        float properThunder = getProperThunder();

        if (thunderGradient < properThunder) {
            thunderGradient = Math.min(properThunder, thunderGradient + 0.01f);
            markDirty();
        } else if (thunderGradient > properThunder) {
            thunderGradient = Math.max(properThunder, thunderGradient - 0.01f);
            markDirty();
        }

        if (dirty) {
            dirty = false;

            if (isCustom()) {
                world.setRainGradient(rainGradient);
                world.setThunderGradient(thunderGradient);
            }

            WorldComponents.WORLD_STATE.sync(world);
        }
    }

    @Override
    public void readFromNbt(NbtCompound tag) {

        dayTime = tag.getLong("day_time");
        rainGradient = tag.getFloat("rain_gradient");
        thunderGradient = tag.getFloat("thunder_gradient");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {

        tag.putLong("day_time", dayTime);
        tag.putFloat("rain_gradient", rainGradient);
        tag.putFloat("thunder_gradient", thunderGradient);
    }

    public void markDirty() {
        dirty = true;
    }
}
