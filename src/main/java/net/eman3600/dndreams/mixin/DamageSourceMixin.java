package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.mixin_interfaces.DamageSourceAccess;
import net.eman3600.dndreams.util.ModTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.ProjectileEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(DamageSource.class)
public abstract class DamageSourceMixin implements DamageSourceAccess {

    @Shadow public abstract @Nullable Entity getAttacker();

    @Shadow public abstract @Nullable Entity getSource();

    private boolean electric = false;
    private boolean affliction = false;

    @Override
    public void setElectric() {
        electric = true;
    }
    @Override
    public boolean isElectric() {
        return electric || (Object)this == DamageSource.LIGHTNING_BOLT;
    }

    @Override
    public void setAffliction() {
        this.affliction = true;
    }
    @Override
    public boolean isAffliction() {
        return affliction || (getAttacker() instanceof LivingEntity entity && entity.getType().isIn(ModTags.GLOOM_ENTITIES)) || (getSource() instanceof ProjectileEntity projectile && projectile.getType().isIn(ModTags.GLOOM_PROJECTILE_ENTITIES));
    }
}
