package net.eman3600.dndreams.mixin_interfaces;

import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;

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

    default boolean isTransethereal() {
        return this instanceof DamageSource source && source.getAttacker() instanceof PlayerEntity player && (EntityComponents.TORMENT.get(player).getAttunedSanity() <= 25 || player.isCreative());
    }


    DamageSource ELECTRIC = create("dndreams_electric", true).setUnblockable();
    DamageSource SHOCK = create("dndreams_shock", true).setUnblockable();
}
