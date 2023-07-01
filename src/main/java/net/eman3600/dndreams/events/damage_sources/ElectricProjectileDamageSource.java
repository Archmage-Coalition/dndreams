package net.eman3600.dndreams.events.damage_sources;

import net.eman3600.dndreams.mixin_interfaces.DamageSourceAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.ProjectileDamageSource;
import org.jetbrains.annotations.Nullable;

public class ElectricProjectileDamageSource extends ProjectileDamageSource {


    public ElectricProjectileDamageSource(String name, Entity projectile, @Nullable Entity attacker) {
        super(name, projectile, attacker);
        ((DamageSourceAccess)this).setElectric();
    }

    public static DamageSource projectile(Entity magic, @Nullable Entity attacker) {
        return new ElectricProjectileDamageSource("electric", magic, attacker).setUnblockable();
    }

    public static DamageSource magic(Entity magic, @Nullable Entity attacker) {
        return new ElectricProjectileDamageSource("electric", magic, attacker).setUnblockable().setUsesMagic();
    }
}
