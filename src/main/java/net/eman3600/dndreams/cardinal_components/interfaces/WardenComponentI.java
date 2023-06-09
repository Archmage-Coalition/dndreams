package net.eman3600.dndreams.cardinal_components.interfaces;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface WardenComponentI extends ServerTickingComponent, AutoSyncedComponent {

    int getSpawnTicks();
    int getDangerTicks();

    void alertShrieker(World world, BlockPos pos);

    void neutralizeSpawn();

    void markDirty();

    BlockPos getShriekerPos();
    Identifier getShriekerDimension();
}
