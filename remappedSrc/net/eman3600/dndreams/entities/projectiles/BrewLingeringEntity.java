package net.eman3600.dndreams.entities.projectiles;

import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.initializers.entity.ModEntities;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionUtil;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BrewLingeringEntity extends AbstractBrewEntity {
    public BrewLingeringEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public BrewLingeringEntity(double d, double e, double f, World world) {
        super(ModEntities.BREW_LINGERING, d, e, f, world);
    }

    public BrewLingeringEntity(LivingEntity livingEntity, World world) {
        super(ModEntities.BREW_LINGERING, livingEntity, world);
    }

    @Override
    public void splash(List<StatusEffectInstance> effects, @Nullable Entity target) {
        AreaEffectCloudEntity cloud = new AreaEffectCloudEntity(this.world, this.getX(), this.getY(), this.getZ());
        Entity cause = this.getEffectCause();

        if (cause instanceof LivingEntity) {
            cloud.setOwner((LivingEntity)cause);
        }

        cloud.setRadius(3.0f);
        cloud.setRadiusOnUse(-0.5f);
        cloud.setWaitTime(10);
        cloud.setRadiusGrowth(-cloud.getRadius() / (float)cloud.getDuration());

        for (StatusEffectInstance effect : effects) {
            cloud.addEffect(new StatusEffectInstance(effect));
        }

        cloud.setColor(PotionUtil.getColor(effects));
        world.spawnEntity(cloud);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.BREW_LINGERING;
    }
}
