package net.eman3600.dndreams.mixin;

import dev.onyxstudios.cca.api.v3.component.ComponentAccess;
import net.eman3600.dndreams.cardinal_components.GatewayComponent;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.initializers.world.ModDimensions;
import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.eman3600.dndreams.util.ModTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.fluid.Fluid;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Nameable;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.entity.EntityLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin implements Nameable, EntityLike, CommandOutput, ComponentAccess {
    @Shadow protected int netherPortalTime;

    @Shadow public World world;

    @Shadow public abstract void setPosition(Vec3d pos);

    @Shadow public abstract void resetNetherPortalCooldown();

    @Shadow public abstract int getMaxNetherPortalTime();

    @Shadow protected boolean inNetherPortal;

    @Shadow protected abstract void tickNetherPortalCooldown();

    @Shadow protected abstract boolean updateWaterState();

    @Shadow public abstract boolean updateMovementInFluid(TagKey<Fluid> tag, double speed);

    @Shadow protected abstract void updateSubmergedInWaterState();

    @Inject(method = "isInvulnerableTo", at = @At("RETURN"), cancellable = true)
    private void dndreams$isInvulnerableTo(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        if ((Object)this instanceof LivingEntity living && (living.hasStatusEffect(ModStatusEffects.INSUBSTANTIAL) || living.hasStatusEffect(ModStatusEffects.GRACE))) {
            cir.setReturnValue(cir.getReturnValue() || damageSource == DamageSource.IN_WALL || damageSource == DamageSource.FALL);
        }
    }

    @Inject(method = "tickNetherPortal", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;push(Ljava/lang/String;)V"), cancellable = true)
    private void dndreams$tickNetherPortal(CallbackInfo ci) {
        if (((Object)this) instanceof ServerPlayerEntity player) {
            GatewayComponent component = EntityComponents.GATEWAY.get(player);

            if (world.getRegistryKey() == ModDimensions.GATEWAY_DIMENSION_KEY) {
                if (component.isChallenge()) {
                    this.netherPortalTime = getMaxNetherPortalTime();
                    resetNetherPortalCooldown();
                    this.inNetherPortal = false;

                    component.exitGateway(true);

                    tickNetherPortalCooldown();

                    ci.cancel();
                } else {
                    component.setFoughtPhantomLord(true);

                    setPosition(component.getReturnPos());
                }
            } else if (world.getRegistryKey() == World.OVERWORLD && !component.hasFoughtPhantomLord()) {
                this.netherPortalTime = getMaxNetherPortalTime();
                resetNetherPortalCooldown();
                this.inNetherPortal = false;

                component.enterGateway(0, false);

                tickNetherPortalCooldown();

                ci.cancel();
            }
        }
    }

    @Redirect(method = "checkWaterState", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;updateMovementInFluid(Lnet/minecraft/tag/TagKey;D)Z"))
    private boolean dndreams$checkWaterState$updateMovementInFluid(Entity instance, TagKey<Fluid> tag, double speed) {
        return instance.updateMovementInFluid(tag, speed) | instance.updateMovementInFluid(ModTags.SORROW, speed);
    }

    @Redirect(method = "updateSubmergedInWaterState", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isSubmergedIn(Lnet/minecraft/tag/TagKey;)Z"))
    private boolean dndreams$updateSubmergedInWaterState$isSubmergedIn(Entity instance, TagKey<Fluid> fluidTag) {
        return instance.isSubmergedIn(fluidTag) | instance.isSubmergedIn(ModTags.SORROW);
    }

}
