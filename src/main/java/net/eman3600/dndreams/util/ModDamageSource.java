package net.eman3600.dndreams.util;

import net.minecraft.entity.damage.DamageSource;

public class ModDamageSource extends DamageSource {
    public ModDamageSource(String name) {
        super(name);
    }

    @Override
    public ModDamageSource setBypassesArmor() {
        return (ModDamageSource) super.setBypassesArmor();
    }

    @Override
    public ModDamageSource setBypassesProtection() {
        return (ModDamageSource) super.setBypassesProtection();
    }

    @Override
    public ModDamageSource setUnblockable() {
        return (ModDamageSource) super.setUnblockable();
    }
}
