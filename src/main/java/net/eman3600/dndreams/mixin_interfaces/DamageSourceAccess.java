package net.eman3600.dndreams.mixin_interfaces;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.entity.damage.ProjectileDamageSource;
import net.minecraft.entity.player.PlayerEntity;

public interface DamageSourceAccess {

    void dndreams$setElectric();
    boolean dndreams$isElectric();
    void dndreams$setAffliction();
    boolean dndreams$isAffliction();
    void dndreams$setParryable(boolean parryable);
    boolean dndreams$isParryable();




    static boolean isElectric(DamageSource source) {
        return source instanceof DamageSourceAccess access && access.dndreams$isElectric();
    }
    static boolean isAffliction(DamageSource source) {
        return source instanceof DamageSourceAccess access && access.dndreams$isAffliction();
    }
    static boolean isParryable(DamageSource source) {
        return source instanceof DamageSourceAccess access && access.dndreams$isParryable();
    }

    static DamageSource create(String name, boolean electric, boolean affliction) {
        DamageSource source = new DamageSource(name);
        if (source instanceof DamageSourceAccess access) {
            if (electric) access.dndreams$setElectric();
            if (affliction) access.dndreams$setAffliction();
        }
        return source;
    }

    static DamageSource magic(Entity attacker) {
        return new EntityDamageSource("dndreams.direct_magic", attacker).setBypassesArmor().setUsesMagic();
    }

    static DamageSource fire(Entity projectile, Entity attacker) {
        return new ProjectileDamageSource("dndreams.fire", projectile, attacker).setFire();
    }

    default boolean isTransethereal() {
        return this instanceof DamageSource source && source.getAttacker() instanceof PlayerEntity player && player.isCreative();
    }


    DamageSource ELECTRIC = create("dndreams.electric", true, false).setUnblockable();
    DamageSource SHOCK = create("dndreams.shock", true, false).setUnblockable().setBypassesArmor().setBypassesProtection();
    DamageSource ROT = new DamageSource("dndreams.rot").setOutOfWorld().setBypassesArmor().setBypassesProtection().setUnblockable();
    DamageSource AFFLICTION = create("dndreams.affliction", false, true).setUsesMagic().setBypassesArmor();
    DamageSource INSANITY = create("dndreams.insanity", false, true).setBypassesArmor().setBypassesProtection().setUnblockable();
    DamageSource CURSED_FLAME = create("dndreams.cursed_flame", false, true).setBypassesArmor().setFire();
}
