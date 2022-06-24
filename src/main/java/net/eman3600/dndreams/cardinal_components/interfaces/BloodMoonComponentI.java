package net.eman3600.dndreams.cardinal_components.interfaces;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.world.World;

public interface BloodMoonComponentI extends Component {
    int getChance();
    long getKnownDay();
    boolean damnedNight();
    void tick();
    boolean manualStart();
}
