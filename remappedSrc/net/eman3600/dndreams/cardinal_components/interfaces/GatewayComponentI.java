package net.eman3600.dndreams.cardinal_components.interfaces;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public interface GatewayComponentI extends ServerTickingComponent, AutoSyncedComponent {
    int getEvent();

    Vec3d getReturnPos();

    boolean isChallenge();

    ServerWorld getExitDimension(boolean success);

    boolean hasFoughtPhantomLord();

    void setFoughtPhantomLord(boolean foughtPhantomLord);
}
