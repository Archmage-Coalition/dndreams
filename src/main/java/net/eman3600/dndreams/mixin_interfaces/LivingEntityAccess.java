package net.eman3600.dndreams.mixin_interfaces;

import net.minecraft.entity.damage.DamageSource;

public interface LivingEntityAccess {
    boolean isTrulyInsideWall();

    boolean dndreams$hasNotBrokenLava();
    boolean dndreams$hasNotBrokenHydro();

    boolean dndreams$shouldResist(float damage, DamageSource source);

    boolean dndreams$isJumping();
    int dndreams$getJumpingCooldown();
    void dndreams$setJumpingCooldown(int jumpingCooldown);

    float getJumpVelocity();
}
