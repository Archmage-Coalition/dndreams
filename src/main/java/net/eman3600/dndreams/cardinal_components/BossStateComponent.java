package net.eman3600.dndreams.cardinal_components;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.eman3600.dndreams.cardinal_components.interfaces.BossStateComponentI;
import net.eman3600.dndreams.initializers.WorldComponents;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

public class BossStateComponent implements BossStateComponentI, AutoSyncedComponent {
    private boolean dragonSlain = false;

    private World world;

    public BossStateComponent(World worldIn) {
        world = worldIn;
    }

    @Override
    public boolean dragonSlain() {
        return dragonSlain;
    }

    @Override
    public void flagDragonSlain(boolean flag) {
        dragonSlain = flag;
        WorldComponents.BOSS_STATE.sync(world);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        dragonSlain = tag.getBoolean("dragon_slain");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putBoolean("dragon_slain", dragonSlain);
    }
}
