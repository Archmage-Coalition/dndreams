package net.eman3600.dndreams.events.damage_sources;

import net.eman3600.dndreams.mixin_interfaces.DamageSourceAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.ProjectileDamageSource;
import org.jetbrains.annotations.Nullable;

public class ElectricProjectileDamageSource extends ProjectileDamageSource implements DamageSourceAccess {
    public ElectricProjectileDamageSource(String name, Entity projectile, @Nullable Entity attacker) {
        super(name, projectile, attacker);
    }

    public static DamageSource projectile(Entity magic, @Nullable Entity attacker) {
        return new ElectricProjectileDamageSource("electric", magic, attacker).setUnblockable();
    }

    @Override
    public void setElectric() {}

    @Override
    public boolean isElectric() {
        return true;
    }
}
