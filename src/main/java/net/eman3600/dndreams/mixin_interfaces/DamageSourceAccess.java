package net.eman3600.dndreams.mixin_interfaces;

import net.minecraft.entity.damage.DamageSource;

public interface DamageSourceAccess {

    void setElectric();
    boolean isElectric();





    static boolean isElectric(DamageSource source) {
        return source instanceof DamageSourceAccess access && access.isElectric();
    }

    static DamageSource create(String name, boolean electric) {
        DamageSource source = new DamageSource(name);
        if (source instanceof DamageSourceAccess access) {
            if (electric) access.setElectric();
        }
        return source;
    }


    DamageSource ELECTRIC = create("dndreams_electric", true).setUnblockable();
    DamageSource SHOCK = create("dndreams_shock", true).setUnblockable();
}
