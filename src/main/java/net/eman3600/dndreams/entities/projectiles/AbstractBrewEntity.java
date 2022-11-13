package net.eman3600.dndreams.entities.projectiles;

import net.eman3600.dndreams.items.consumable.brew.AbstractBrewItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtil;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class AbstractBrewEntity extends ThrownItemEntity implements FlyingItemEntity {


    public AbstractBrewEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public AbstractBrewEntity(EntityType<? extends ThrownItemEntity> entityType, double d, double e, double f, World world) {
        super(entityType, d, e, f, world);
    }

    public AbstractBrewEntity(EntityType<? extends ThrownItemEntity> entityType, LivingEntity livingEntity, World world) {
        super(entityType, livingEntity, world);
    }

    @Override
    protected float getGravity() {
        return 0.05f;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (this.world.isClient) {
            return;
        }

        ItemStack stack = getStack();
        List<StatusEffectInstance> list = PotionUtil.getPotionEffects(stack);

        if (list.size() > 0) splash(list, hitResult.getType() == HitResult.Type.ENTITY ? ((EntityHitResult)hitResult).getEntity() : null);

        int i = hasNonInstantEffect(list) ? WorldEvents.SPLASH_POTION_SPLASHED : WorldEvents.INSTANT_SPLASH_POTION_SPLASHED;
        this.world.syncWorldEvent(i, this.getBlockPos(), PotionUtil.getColor(PotionUtil.getCustomPotionEffects(stack)));
        this.discard();
    }

    protected static boolean hasNonInstantEffect(List<StatusEffectInstance> effects) {
        for (StatusEffectInstance effect: effects) {
            if (effect.getEffectType().isInstant()) return false;
        }

        return true;
    }

    public abstract void splash(List<StatusEffectInstance> effects, @Nullable Entity target);
}
