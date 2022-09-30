package net.eman3600.dndreams.mixin;

import dev.onyxstudios.cca.api.v3.component.ComponentAccess;
import net.eman3600.dndreams.initializers.ModStatusEffects;
import net.eman3600.dndreams.util.ModTags;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.util.Nameable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.minecraft.world.entity.EntityLike;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin implements Nameable, EntityLike, CommandOutput, ComponentAccess {
    @Inject(method = "isInvulnerableTo", at = @At("RETURN"), cancellable = true)
    private void dndreams$isInvulnerableTo(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        if ((Object)this instanceof LivingEntity living && living.hasStatusEffect(ModStatusEffects.INSUBSTANTIAL)) {
            cir.setReturnValue(cir.getReturnValue() || damageSource == DamageSource.IN_WALL || damageSource == DamageSource.FALL);
        }
    }
}
