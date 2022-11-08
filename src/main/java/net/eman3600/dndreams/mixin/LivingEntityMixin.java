package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.cardinal_components.DreamingComponent;
import net.eman3600.dndreams.initializers.EntityComponents;
import net.eman3600.dndreams.initializers.ModFluids;
import net.eman3600.dndreams.initializers.ModStatusEffects;
import net.eman3600.dndreams.initializers.WorldComponents;
import net.eman3600.dndreams.mixin_interfaces.LivingEntityAccess;
import net.eman3600.dndreams.util.ModTags;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
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
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
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
            if (!hasStatusEffect(ModStatusEffects.AFFLICTION)) addStatusEffect(new StatusEffectInstance(ModStatusEffects.AFFLICTION, 80, 0, true, true));

            if (!hasStatusEffect(ModStatusEffects.SUPPRESSED)) addStatusEffect(new StatusEffectInstance(ModStatusEffects.SUPPRESSED, 80, 0, true, true));

            if ((Object)this instanceof PlayerEntity player) {
                if (!hasStatusEffect(ModStatusEffects.LOOMING)) addStatusEffect(new StatusEffectInstance(ModStatusEffects.LOOMING, 80, 0, true, true));

                EntityComponents.TORMENT.get(player).addPerSecond(3f);
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
}
