package net.eman3600.dndreams.cardinal_components;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.eman3600.dndreams.cardinal_components.interfaces.BossStateComponentI;
import net.eman3600.dndreams.initializers.WorldComponents;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionTypes;

public class BossStateComponent implements BossStateComponentI, AutoSyncedComponent {
    private boolean dragonSlain = false;
    private boolean witherSlain = false;

    private Scoreboard scoreboard;
    private MinecraftServer server;

    public BossStateComponent(Scoreboard worldIn, MinecraftServer serverIn) {
        scoreboard = worldIn;
        server = serverIn;
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
        server.setDifficultyLocked(true);
        server.setDifficulty(dragonSlain ? Difficulty.HARD : Difficulty.NORMAL, true);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        dragonSlain = tag.getBoolean("dragon_slain");
        witherSlain = tag.getBoolean("wither_slain");

        setDifficulty();
        WorldComponents.BOSS_STATE.sync(scoreboard);
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putBoolean("dragon_slain", dragonSlain);
        tag.putBoolean("wither_slain", witherSlain);
    }
}
