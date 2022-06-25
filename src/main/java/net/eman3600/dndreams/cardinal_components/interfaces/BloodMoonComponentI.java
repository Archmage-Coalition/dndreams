package net.eman3600.dndreams.cardinal_components.interfaces;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.tick.ClientTickingComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.world.World;

public interface BloodMoonComponentI extends ServerTickingComponent {
    int getChance();
    long getKnownDay();
    boolean damnedNight();
    boolean manualStart();
}
