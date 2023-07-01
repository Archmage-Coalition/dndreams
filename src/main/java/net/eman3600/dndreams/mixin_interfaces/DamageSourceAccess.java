package net.eman3600.dndreams.mixin_interfaces;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;

public interface DamageSourceAccess {

    void setElectric();
    boolean isElectric();
    void setAffliction();
    boolean isAffliction();





    static boolean isElectric(DamageSource source) {
        return source instanceof DamageSourceAccess access && access.isElectric();
    }
    static boolean isAffliction(DamageSource source) {
        return source instanceof DamageSourceAccess access && access.isAffliction();
    }

    static DamageSource create(String name, boolean electric, boolean affliction) {
        DamageSource source = new DamageSource(name);
        if (source instanceof DamageSourceAccess access) {
            if (electric) access.setElectric();
            if (affliction) access.setAffliction();
        }
        return source;
    }

    default boolean isTransethereal() {
        return this instanceof DamageSource source && source.getAttacker() instanceof PlayerEntity player && player.isCreative();
    }


    DamageSource ELECTRIC = create("dndreams.electric", true, false).setUnblockable();
    DamageSource SHOCK = create("dndreams.shock", true, false).setUnblockable();
    DamageSource ROT = new DamageSource("dndreams.rot").setOutOfWorld().setBypassesArmor().setBypassesProtection().setUnblockable();
    DamageSource AFFLICTION = create("dndreams.affliction", false, true).setUsesMagic();
}
