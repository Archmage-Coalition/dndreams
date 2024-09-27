package net.eman3600.dndreams.entities.projectiles;

import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.eman3600.dndreams.initializers.entity.ModEntities;
import net.eman3600.dndreams.networking.packet_s2c.ManagoldFlashPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class DreadfulArrowEntity extends PersistentProjectileEntity {
    public static final TrackedData<Integer> DURATION = DataTracker.registerData(DreadfulArrowEntity.class, TrackedDataHandlerRegistry.INTEGER);
    public static final TrackedData<Integer> AMPLIFIER = DataTracker.registerData(DreadfulArrowEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public DreadfulArrowEntity(EntityType<? extends DreadfulArrowEntity> entityType, World world) {
        super(entityType, world);
    }

    public DreadfulArrowEntity(World world, LivingEntity owner) {
        super(ModEntities.DREADFUL_ARROW, owner, world);
    }

    public DreadfulArrowEntity(World world, LivingEntity owner, int duration, int amplifier) {
        super(ModEntities.DREADFUL_ARROW, owner, world);

        dataTracker.set(DURATION, duration);
        dataTracker.set(AMPLIFIER, amplifier);
    }

    public DreadfulArrowEntity(World world, double x, double y, double z) {
        super(ModEntities.DREADFUL_ARROW, x, y, z, world);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();

        dataTracker.startTracking(DURATION, 200);
        dataTracker.startTracking(AMPLIFIER, 0);
    }

    @Override
    public void tick() {
        if (firstUpdate) {
            setPierceLevel((byte)(getPierceLevel() + 1));
        }

        super.tick();
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(ModItems.DREADFUL_ARROW);
    }

    @Override
    protected void onHit(LivingEntity target) {
        super.onHit(target);
        StatusEffectInstance statusEffectInstance = new StatusEffectInstance(ModStatusEffects.HEARTBLEED, dataTracker.get(DURATION), dataTracker.get(AMPLIFIER));
        target.addStatusEffect(statusEffectInstance, this.getEffectCause());
    }
}
