package net.eman3600.dndreams.mixin_interfaces;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

public interface LivingEntityAccess {
    boolean isTrulyInsideWall();

    boolean hasNotBrokenLava();

    boolean shouldResist(float damage, DamageSource source);

    boolean isJumping();
    int getJumpingCooldown();
    void setJumpingCooldown(int jumpingCooldown);

    float getJumpVelocity();
}
