package net.eman3600.dndreams.entities.projectiles;

import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.initializers.entity.ModEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BrewLiquidEntity extends AbstractBrewEntity {
    public BrewLiquidEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public BrewLiquidEntity(double d, double e, double f, World world) {
        super(ModEntities.BREW_LIQUID, d, e, f, world);
    }

    public BrewLiquidEntity(LivingEntity livingEntity, World world) {
        super(ModEntities.BREW_LIQUID, livingEntity, world);
    }

    @Override
    public void splash(List<StatusEffectInstance> effects, @Nullable Entity target) {

    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.BREW_LIQUID;
    }
}
