package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.mixin_interfaces.DamageSourceAccess;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DamageSource.class)
public abstract class DamageSourceMixin implements DamageSourceAccess {
    private boolean electric = false;

    @Override
    public void setElectric() {
        electric = true;
    }

    @Override
    public boolean isElectric() {
        return electric || (Object)this == DamageSource.LIGHTNING_BOLT;
    }
}
