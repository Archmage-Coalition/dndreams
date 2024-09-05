package net.eman3600.dndreams.cardinal_components;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.eman3600.dndreams.cardinal_components.interfaces.BossStateComponentI;
import net.eman3600.dndreams.initializers.cca.WorldComponents;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Difficulty;
import org.jetbrains.annotations.Nullable;

public class BossStateComponent implements BossStateComponentI, AutoSyncedComponent {
    private boolean dragonSlain = false;
    private boolean witherSlain = false;
    private boolean elrunezSlain = false;

    private final Scoreboard scoreboard;
    @Nullable private final MinecraftServer server;

    public BossStateComponent(Scoreboard scoreboard, @Nullable MinecraftServer server) {
        this.scoreboard = scoreboard;
        this.server = server;
    }

    @Override
    public boolean dragonSlain() {
        return dragonSlain;
    }

    @Override
    public void flagDragonSlain(boolean flag) {
        dragonSlain = flag;
        setDifficulty();
        WorldComponents.BOSS_STATE.sync(scoreboard);
    }

    @Override
    public boolean witherSlain() {
        return witherSlain;
    }

    @Override
    public void flagWitherSlain(boolean flag) {
        witherSlain = flag;
        WorldComponents.BOSS_STATE.sync(scoreboard);
    }

    @Override
    public boolean elrunezSlain() {
        return elrunezSlain;
    }

    @Override
    public void flagElrunezSlain(boolean flag) {
        elrunezSlain = flag;
        WorldComponents.BOSS_STATE.sync(scoreboard);
    }




    @Override
    public boolean gatewaysSlain() {
        return dragonSlain && witherSlain;
    }

    @Override
    public int totalGatewaysSlain() {
        int slain = 0;

        if (dragonSlain)
            slain++;
        if (witherSlain)
            slain++;

        return slain;
    }

    @Override
    public void setDifficulty() {
        if (server != null) {
            server.setDifficultyLocked(true);
            server.setDifficulty(dragonSlain ? Difficulty.HARD : Difficulty.NORMAL, true);
        }
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        dragonSlain = tag.getBoolean("dragon_slain");
        witherSlain = tag.getBoolean("wither_slain");
        elrunezSlain = tag.getBoolean("elrunez_slain");

        setDifficulty();
        //WorldComponents.BOSS_STATE.sync(scoreboard);
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putBoolean("dragon_slain", dragonSlain);
        tag.putBoolean("wither_slain", witherSlain);
        tag.putBoolean("elrunez_slain", elrunezSlain);
    }
}
