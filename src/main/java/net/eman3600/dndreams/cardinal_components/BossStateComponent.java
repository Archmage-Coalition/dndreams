package net.eman3600.dndreams.cardinal_components;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.eman3600.dndreams.cardinal_components.interfaces.BossStateComponentI;
import net.eman3600.dndreams.initializers.WorldComponents;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

public class BossStateComponent implements BossStateComponentI, AutoSyncedComponent {
    private boolean dragonSlain = false;
    private boolean witherSlain = false;

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
    public boolean witherSlain() {
        return witherSlain;
    }

    @Override
    public void flagWitherSlain(boolean flag) {
        witherSlain = flag;
        WorldComponents.BOSS_STATE.sync(world);
    }




    @Override
    public boolean gatewaysSlain() {
        return dragonSlain && witherSlain;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        dragonSlain = tag.getBoolean("dragon_slain");
        witherSlain = tag.getBoolean("wither_slain");

        WorldComponents.BOSS_STATE.sync(world);
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putBoolean("dragon_slain", dragonSlain);
        tag.putBoolean("wither_slain", witherSlain);
    }
}
