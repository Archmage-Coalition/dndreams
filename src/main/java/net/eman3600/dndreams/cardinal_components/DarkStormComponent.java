package net.eman3600.dndreams.cardinal_components;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.eman3600.dndreams.initializers.cca.WorldComponents;
import net.eman3600.dndreams.initializers.world.ModDimensions;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class DarkStormComponent implements AutoSyncedComponent, ServerTickingComponent {

    private int state = -1;
    private int stateTicks = 0;
    private boolean dirty = false;

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
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putInt("state", state);
        tag.putInt("state_ticks", stateTicks);
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

        if (state > 2) {
            startState(0);
            return;
        }

        this.state = state;
        Random random = server.getWorld(World.OVERWORLD).random;

        if (state == 0) {
            stateTicks = random.nextBetween(19200, 28800);
            if (server.isHardcore()) stateTicks /= 2;
            if (WorldComponents.BOSS_STATE.get(scoreboard).elrunezSlain()) stateTicks *= 2;
        } else if (state == 1) {
            stateTicks = 900;
        } else {
            stateTicks = random.nextBetween(4800, 9600);
            if (server.isHardcore()) stateTicks *= 2;
        }

        markDirty();
    }

    public boolean shouldRain() {
        return state == 2 || (state == 1 && stateTicks < 100);
    }

    public boolean shouldThunder() {
        return state == 2;
    }
}
