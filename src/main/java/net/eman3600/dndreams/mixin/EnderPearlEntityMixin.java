package net.eman3600.dndreams.mixin;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(EnderPearlEntity.class)
public abstract class EnderPearlEntityMixin extends ThrownItemEntity {
    public EnderPearlEntityMixin(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(method = "onCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    private boolean dndreams$onCollision$dissociation(Entity instance, DamageSource source, float amount) {
        if (instance instanceof LivingEntity entity) {
            Optional<TrinketComponent> optional = TrinketsApi.getTrinketComponent(entity);

            if (optional.isPresent() && optional.get().isEquipped(ModItems.DISSOCIATION_CHARM) && !entity.hasStatusEffect(ModStatusEffects.DISCORDANT)) {
                entity.addStatusEffect(new StatusEffectInstance(ModStatusEffects.DISCORDANT, 160));

                return false;
            }
        }

        return instance.damage(source, amount);
    }
}
