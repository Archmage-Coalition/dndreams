package net.eman3600.dndreams.cardinal_components.interfaces;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.eman3600.dndreams.infusions.setup.Infusion;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.Vec3d;

public interface InfusionComponentI extends AutoSyncedComponent, ServerTickingComponent {
    Infusion getInfusion();
    boolean infused();

    void setInfusion(Infusion change);

    void setLinkTicks(int amount);

    boolean linkedToBonfire();

    boolean tryResist(DamageSource source, float amount);

    boolean hasDodge();

    boolean canDodge();

    void setHasDodge(boolean allow);

    void markDirty();

    void giveImmunity();

    boolean hasImmunity();

    void tryDodgeServer(Vec3d velocity);
}
