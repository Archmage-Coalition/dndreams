package net.eman3600.dndreams.entities.projectiles;

import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.initializers.entity.ModEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BrewSplashEntity extends AbstractBrewEntity {
    public BrewSplashEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public BrewSplashEntity(double d, double e, double f, World world) {
        super(ModEntities.BREW_SPLASH, d, e, f, world);
    }

    public BrewSplashEntity(LivingEntity livingEntity, World world) {
        super(ModEntities.BREW_SPLASH, livingEntity, world);
    }

    @Override
    public void splash(List<StatusEffectInstance> effects, @Nullable Entity target) {
        Box box = this.getBoundingBox().expand(4.0, 2.0, 4.0);
        List<LivingEntity> list = this.world.getNonSpectatingEntities(LivingEntity.class, box);
        if (!list.isEmpty()) {
            Entity cause = this.getEffectCause();
            for (LivingEntity livingEntity : list) {
                double d;
                if (!livingEntity.isAffectedBySplashPotions() || !((d = this.squaredDistanceTo(livingEntity)) < 16.0)) continue;
                double e = 1.0 - Math.sqrt(d) / 4.0;
                if (livingEntity == target) {
                    e = 1.0;
                }
                for (StatusEffectInstance effect : effects) {
                    StatusEffect status = effect.getEffectType();
                    if (status.isInstant()) {
                        status.applyInstantEffect(this, this.getOwner(), livingEntity, effect.getAmplifier(), e);
                        continue;
                    }
                    int i = (int)(e * (double) effect.getDuration() + 0.5);
                    if (i <= 20) continue;
                    livingEntity.addStatusEffect(new StatusEffectInstance(status, i, effect.getAmplifier(), effect.isAmbient(), effect.shouldShowParticles()), cause);
                }
            }
        }
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.BREW_SPLASH;
    }
}
