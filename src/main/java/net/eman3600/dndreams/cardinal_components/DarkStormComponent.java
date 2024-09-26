package net.eman3600.dndreams.cardinal_components;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.eman3600.dndreams.initializers.cca.WorldComponents;
import net.eman3600.dndreams.initializers.world.ModDimensions;
import net.eman3600.dndreams.items.interfaces.AirSwingItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class DarkStormComponent implements AutoSyncedComponent, ServerTickingComponent {

    private int state = -1;
    private int stateTicks = 0;
    private boolean dirty = false;
    private double windX = 0;
    private double windY = 0;

    private final Scoreboard scoreboard;
    @Nullable
    private final MinecraftServer server;

    public DarkStormComponent(Scoreboard scoreboard, @Nullable MinecraftServer server) {
        this.scoreboard = scoreboard;
        this.server = server;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        state = tag.getInt("state");
        stateTicks = tag.getInt("state_ticks");
        windX = tag.getDouble("wind_x");
        windY = tag.getDouble("wind_y");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putInt("state", state);
        tag.putInt("state_ticks", stateTicks);
        tag.putDouble("wind_x", windX);
        tag.putDouble("wind_y", windY);
    }

    @Override
    public void serverTick() {

        if (state >= 0) {
            stateTicks--;

            if (stateTicks <= 0) {
                startState(state + 1);
            }

            markDirty();
        }

        if (dirty) {
            dirty = false;
            WorldComponents.DARK_STORM.sync(scoreboard);
        }
    }

    private void markDirty() {
        dirty = true;
    }

    public boolean disabled() {
        return state == -1;
    }

    public void startState(int state) {
        if (server == null) return;

        if (state > 3) {
            startState(0);
            return;
        }

        this.state = state;
        Random random = server.getWorld(World.OVERWORLD).random;

        if (state == 0) {
            stateTicks = random.nextBetween(19200, 28800);
            if (server.isHardcore()) stateTicks /= 2;
            if (WorldComponents.BOSS_STATE.get(scoreboard).elrunezSlain()) stateTicks *= 2;
            randomizeWindDirection(random);
        } else if (state == 1) {
            stateTicks = 900;
        } else if (state == 2) {
            stateTicks = random.nextBetween(4800, 9600);
            if (server.isHardcore()) stateTicks *= 2;
        } else {
            stateTicks = 100;
        }

        markDirty();
    }

    public void setStateTime(int ticks) {
        this.stateTicks = ticks;
    }
    public int getStateTime() {
        return this.stateTicks;
    }

    public boolean shouldRain() {
        return state > 1 || (state == 1 && stateTicks < 100);
    }

    public boolean shouldThunder() {
        return state == 2;
    }

    public float windStrength() {
        if (state == 2) {
            return 1f;
        } else if (state == 1) {
            return 1f - (stateTicks/900f);
        } else if (state == 3) {
            return stateTicks/100f;
        }else return 0f;
    }

    private void randomizeWindDirection(Random random) {
        Vec3d windDir = AirSwingItem.rayZVector(random.nextBetween(-180, 180), 0).multiply(.7);

        windX = windDir.x;
        windY = windDir.z;

        markDirty();
    }

    public double getWindX() {
        return windX;
    }
    public double getWindY() {
        return windY;
    }
}
