package net.eman3600.dndreams.mixin;

import com.mojang.authlib.GameProfile;
import net.eman3600.dndreams.initializers.ModStatusEffects;
import net.eman3600.dndreams.initializers.WorldComponents;
import net.eman3600.dndreams.mixin_interfaces.LivingEntityMixinI;
import net.eman3600.dndreams.util.ModTags;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements LivingEntityMixinI {
    @Shadow
    @Final
    private AttributeContainer attributes;


    @Shadow public abstract boolean hasStatusEffect(StatusEffect effect);

    @Shadow public abstract boolean addStatusEffect(StatusEffectInstance effect);

    @Shadow public abstract EntityDimensions getDimensions(EntityPose pose);

    @Shadow public abstract boolean isInsideWall();

    @Shadow public abstract boolean removeStatusEffect(StatusEffect type);

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
        if (!((Entity)this instanceof ClientPlayerEntity)) {
            if (isSubmergedIn(ModTags.FLOWING_SPIRIT) && !hasStatusEffect(ModStatusEffects.INSUBSTANTIAL)) {
                addStatusEffect(new StatusEffectInstance(ModStatusEffects.INSUBSTANTIAL, Integer.MAX_VALUE));
            } else if (hasStatusEffect(ModStatusEffects.INSUBSTANTIAL) && !isSubmergedIn(ModTags.FLOWING_SPIRIT) && (!isInsideWall() || isOnGround())) {
                removeStatusEffect(ModStatusEffects.INSUBSTANTIAL);
            }
        }
    }

    @Unique
    public boolean isTrulyInsideWall() {
        float f = getDimensions(getPose()).width * 0.8F;
        Box box = Box.of(this.getEyePos(), (double)f, 1.0E-6D, (double)f);
        return BlockPos.stream(box).anyMatch((pos) -> {
            BlockState blockState = this.world.getBlockState(pos);
            return !blockState.isAir() && blockState.shouldSuffocate(this.world, pos) && VoxelShapes.matchesAnywhere(blockState.getCollisionShape(this.world, pos).offset((double)pos.getX(), (double)pos.getY(), (double)pos.getZ()), VoxelShapes.cuboid(box), BooleanBiFunction.AND);
        });
    }
}
