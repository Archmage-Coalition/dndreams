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

    @Shadow public abstract boolean bypassesArmor();

    @Shadow public abstract boolean isExplosive();

    private boolean electric = false;
    private boolean affliction = false;
    private boolean parryable = false;
    private boolean unparryable = false;

    @Override
    public void dndreams$setElectric() {
        electric = true;
    }
    @Override
    public boolean dndreams$isElectric() {
        return electric || (Object)this == DamageSource.LIGHTNING_BOLT;
    }

    @Override
    public void dndreams$setAffliction() {
        this.affliction = true;
    }
    @Override
    public boolean dndreams$isAffliction() {
        return affliction || (Object) this == DamageSource.WITHER || (getAttacker() instanceof LivingEntity entity && entity.getType().isIn(ModTags.GLOOM_ENTITIES)) || (getSource() instanceof ProjectileEntity projectile && projectile.getType().isIn(ModTags.GLOOM_PROJECTILE_ENTITIES));
    }

    @Override
    public void dndreams$setParryable(boolean parryable) {
        this.parryable = parryable;
        this.unparryable = !parryable;
    }
    @Override
    public boolean dndreams$isParryable() {
        return parryable || (!bypassesArmor() && !unparryable && !isExplosive());
    }
}
