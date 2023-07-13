package net.eman3600.dndreams.events.damage_sources;

import net.eman3600.dndreams.mixin_interfaces.DamageSourceAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.ProjectileDamageSource;
import org.jetbrains.annotations.Nullable;

public class AfflictionProjectileDamageSource extends ProjectileDamageSource {


    public AfflictionProjectileDamageSource(String name, Entity projectile, @Nullable Entity attacker) {
        super(name, projectile, attacker);
        ((DamageSourceAccess)this).setAffliction();
    }

    public static DamageSource projectile(Entity magic, @Nullable Entity attacker) {
        return new AfflictionProjectileDamageSource("affliction", magic, attacker).setUnblockable();
    }

    public static DamageSource magic(Entity magic, @Nullable Entity attacker) {
        return new AfflictionProjectileDamageSource("affliction", magic, attacker).setUnblockable().setUsesMagic();
    }
}
