package net.eman3600.dndreams.cardinal_components;

import net.eman3600.dndreams.cardinal_components.interfaces.WorldStateComponentI;
import net.eman3600.dndreams.initializers.cca.WorldComponents;
import net.eman3600.dndreams.initializers.world.ModDimensions;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.world.World;

public class WorldStateComponent implements WorldStateComponentI {

    private final World world;
    private float rainGradient = 0;
    private float thunderGradient = 0;
    private boolean dirty = false;

    public WorldStateComponent(World world) {
        this.world = world;
    }

    @Override
    public boolean isCustom() {
        return world.getRegistryKey() == ModDimensions.HAVEN_DIMENSION_KEY;
    }

    @Override
    public float getRainGradient() {
        return rainGradient;
    }

    @Override
    public float getThunderGradient() {
        return thunderGradient;
    }

    private float getProperRain() {
        Scoreboard board = world.getScoreboard();

        if (board != null && !WorldComponents.BOSS_STATE.get(board).elrunezSlain()) {
            return 1f;
        }

        /*if (world instanceof ServerWorld serverWorld) {
            World overworld = serverWorld.getServer().getWorld(World.OVERWORLD);
            if (overworld == null) return 0;

            BloodMoonComponent bloodMoon = WorldComponents.BLOOD_MOON.get(overworld);

            return bloodMoon.damnedNight() ? 1f : 0f;
        }*/

        return 0;
    }

    private float getProperThunder() {
        Scoreboard board = world.getScoreboard();

        if (board != null && !WorldComponents.BOSS_STATE.get(board).elrunezSlain()) {
            return 1f;
        }

        /*if (world instanceof ServerWorld serverWorld) {
            World overworld = serverWorld.getServer().getWorld(World.OVERWORLD);
            if (overworld == null) return 0;

            BloodMoonComponent bloodMoon = WorldComponents.BLOOD_MOON.get(overworld);

            return Math.min(getProperRain(), bloodMoon.isBloodMoon() ? 1f : 0f);
        }*/

        return 0;
    }

    @Override
    public void serverTick() {

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

        rainGradient = tag.getFloat("rain_gradient");
        thunderGradient = tag.getFloat("thunder_gradient");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {

        tag.putFloat("rain_gradient", rainGradient);
        tag.putFloat("thunder_gradient", thunderGradient);
    }

    public void markDirty() {
        dirty = true;
    }
}
