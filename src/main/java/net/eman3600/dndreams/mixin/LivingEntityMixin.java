package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.cardinal_components.DreamingComponent;
import net.eman3600.dndreams.cardinal_components.TormentComponent;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.eman3600.dndreams.initializers.cca.WorldComponents;
import net.eman3600.dndreams.mixin_interfaces.LivingEntityAccess;
import net.eman3600.dndreams.util.ModTags;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.FluidTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements LivingEntityAccess {
    @Shadow
    @Final
    private AttributeContainer attributes;


    @Shadow public abstract boolean hasStatusEffect(StatusEffect effect);

    @Shadow public abstract boolean addStatusEffect(StatusEffectInstance effect);

    @Shadow public abstract EntityDimensions getDimensions(EntityPose pose);

    @Shadow public abstract boolean isInsideWall();

    @Shadow public abstract boolean removeStatusEffect(StatusEffect type);

    @Shadow public abstract boolean canMoveVoluntarily();

    @Shadow public abstract boolean addStatusEffect(StatusEffectInstance effect, @Nullable Entity source);

    @Shadow public abstract boolean isUndead();

    @Shadow public abstract @Nullable StatusEffectInstance getStatusEffect(StatusEffect effect);

    @Shadow protected abstract int computeFallDamage(float fallDistance, float damageMultiplier);

    @Shadow private boolean effectsChanged;

    @Shadow public abstract boolean canTakeDamage();

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "onDeath", at = @At("HEAD"))
    public void injectDeath(DamageSource damageSource, CallbackInfo info) {
        if ((Entity)this instanceof WitherEntity) {
            WorldComponents.BOSS_STATE.get(world.getScoreboard()).flagWitherSlain(true);
        }
    }



    @Inject(method = "baseTick", at = @At("HEAD"))
    public void dndreams$baseTick(CallbackInfo ci) {
        if (isSubmergedIn(ModTags.FLOWING_SPIRIT) && !hasStatusEffect(ModStatusEffects.INSUBSTANTIAL)) {
            addStatusEffect(new StatusEffectInstance(ModStatusEffects.INSUBSTANTIAL, Integer.MAX_VALUE, 0, true, true));
            if (hasStatusEffect(ModStatusEffects.GRACE)) {
                removeStatusEffect(ModStatusEffects.GRACE);
            }
        } else if (hasStatusEffect(ModStatusEffects.INSUBSTANTIAL) && !isSubmergedIn(ModTags.FLOWING_SPIRIT) && (!isInsideWall() || isOnGround())) {
            removeStatusEffect(ModStatusEffects.INSUBSTANTIAL);
        }
        if (hasStatusEffect(ModStatusEffects.GRACE) && isOnGround()) {
            removeStatusEffect(ModStatusEffects.GRACE);
        }

        if (fluidHeight.getOrDefault(ModTags.SORROW, 0) > 0.1f) {
            if ((Object)this instanceof PlayerEntity player) {
                TormentComponent component = EntityComponents.TORMENT.get(player);

                addStatusEffect(new StatusEffectInstance(ModStatusEffects.LOOMING, 80, 0, true, true));

                component.addPerMinute(component.isShielded() ? 10f : 150f);

                if (!component.isShielded()) {
                    if (!hasStatusEffect(ModStatusEffects.AFFLICTION)) addStatusEffect(new StatusEffectInstance(ModStatusEffects.AFFLICTION, 80, 0, true, true));
                    if (!hasStatusEffect(ModStatusEffects.SUPPRESSED)) addStatusEffect(new StatusEffectInstance(ModStatusEffects.SUPPRESSED, 80, 0, true, true));
                }
            } else {
                if (!hasStatusEffect(ModStatusEffects.AFFLICTION)) addStatusEffect(new StatusEffectInstance(ModStatusEffects.AFFLICTION, 80, 0, true, true));
                if (!hasStatusEffect(ModStatusEffects.SUPPRESSED)) addStatusEffect(new StatusEffectInstance(ModStatusEffects.SUPPRESSED, 80, 0, true, true));
            }
        }

    }

    @Inject(method = "tryUseTotem", at = @At("RETURN"), cancellable = true)
    private void dndreams$tryUseTotem(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue() && (Entity)this instanceof PlayerEntity player) {
            DreamingComponent component = EntityComponents.DREAMING.get(player);
            if (component.isDreaming()) {
                player.setHealth(1.0f);

                RegistryKey<World> registryKey = World.OVERWORLD;
                ServerWorld serverWorld = ((ServerWorld)world).getServer().getWorld(registryKey);
                FabricDimensions.teleport(player, serverWorld, new TeleportTarget(component.returnPos(), Vec3d.ZERO, player.getYaw(), player.getPitch()));

                cir.setReturnValue(true);
            }
        }
    }

    @Redirect(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isSubmergedIn(Lnet/minecraft/tag/TagKey;)Z"))
    private boolean dndreams$baseTick$isSubmergedIn(LivingEntity instance, TagKey<Fluid> tagKey) {
        if (tagKey == FluidTags.WATER) {
            return instance.isSubmergedIn(tagKey) | instance.isSubmergedIn(ModTags.SORROW);
        } else {
            return instance.isSubmergedIn(tagKey);
        }
    }

    @Redirect(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/effect/StatusEffectUtil;hasWaterBreathing(Lnet/minecraft/entity/LivingEntity;)Z"))
    private boolean dndreams$baseTick$waterBreathing(LivingEntity entity) {
        if (entity instanceof PlayerEntity player) {
            TormentComponent component = EntityComponents.TORMENT.get(player);

            if (StatusEffectUtil.hasWaterBreathing(entity) && component.isShielded()) return true;
        }

        return StatusEffectUtil.hasWaterBreathing(entity) && !entity.isSubmergedIn(ModTags.SORROW);
    }

    @Inject(method = "getNextAirUnderwater", at = @At("HEAD"), cancellable = true)
    private void dndreams$getNextAirUnderwater(int air, CallbackInfoReturnable<Integer> cir) {
        try {
            LivingEntity entity = (LivingEntity) (Object) this;

            if (entity instanceof PlayerEntity player && EntityComponents.TORMENT.get(player).isShielded()) {
                return;
            }

            if (entity.isSubmergedIn(ModTags.SORROW)) {
                cir.setReturnValue(Math.max(air > 0 ? air - 5 : air - 2, -20));
            }
        } catch (ClassCastException ignored) {}
    }

    @ModifyVariable(method = "damage", at = @At("HEAD"))
    private float dndreams$damage$affliction(float amount) {
        if (this.hasStatusEffect(ModStatusEffects.AFFLICTION) && !isUndead()) {
            return (float) (amount * Math.pow(1.4f, this.getStatusEffect(ModStatusEffects.AFFLICTION).getAmplifier() + 1));
        } else return amount;
    }

    @Redirect(method = "damage", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/LivingEntity;timeUntilRegen:I", opcode = Opcodes.PUTFIELD))
    private void dndreams$damage$shortenIFrames(LivingEntity instance, int value) {
        if (this.hasStatusEffect(ModStatusEffects.AFFLICTION) && !isUndead()) {
            instance.timeUntilRegen = 14;
        } else {
            instance.timeUntilRegen = value;
        }
    }

    @Inject(method = "addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)Z", at = @At("HEAD"), cancellable = true)
    private void dndreams$addStatusEffect(StatusEffectInstance effect, Entity source, CallbackInfoReturnable<Boolean> cir) {
        StatusEffect status = effect.getEffectType();
        EntityComponents.TORMENT.maybeGet(this).ifPresent(component -> {

            if (component.isShielded() && (status == ModStatusEffects.AFFLICTION || status == StatusEffects.WITHER || status == StatusEffects.DARKNESS)) {
                cir.setReturnValue(false);
            }
        });

        if (getType() == EntityType.WARDEN && status == ModStatusEffects.AFFLICTION) {
            cir.setReturnValue(false);
        }
    }
}
