package net.eman3600.dndreams.cardinal_components;

import net.eman3600.dndreams.cardinal_components.interfaces.WorldStateComponentI;
import net.eman3600.dndreams.initializers.cca.WorldComponents;
import net.eman3600.dndreams.initializers.world.ModDimensions;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.world.World;

import javax.annotation.Nullable;

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
        return world.getRegistryKey() == ModDimensions.HAVEN_DIMENSION_KEY || world.getRegistryKey() == ModDimensions.DREAM_DIMENSION_KEY;
    }

    @Override
    public float getRainGradient() {
        return rainGradient;
    }

    @Override
    public float getThunderGradient() {
        return thunderGradient;
    }

    @Override
    public void serverTick() {

        DarkStormComponent storm = getStorm();

        if (storm != null && storm.disabled() && !world.getPlayers().isEmpty()) {
            storm.startState(0);
        }

        float properRain = storm != null && storm.shouldRain() ? 1f : 0f;

        if (rainGradient < properRain) {
            rainGradient = Math.min(properRain, rainGradient + 0.01f);
            markDirty();
        } else if (rainGradient > properRain) {
            rainGradient = Math.max(properRain, rainGradient - 0.01f);
            markDirty();
        }

        float properThunder = storm != null && storm.shouldThunder() ? 1f : 0f;

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

    @Nullable
    private DarkStormComponent getStorm() {
        Scoreboard board = world.getScoreboard();

        if (board != null) {
            return WorldComponents.DARK_STORM.get(board);
        }
        return null;
    }
}
