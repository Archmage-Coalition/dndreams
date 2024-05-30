package net.eman3600.dndreams.entities.projectiles;

import net.eman3600.dndreams.initializers.entity.ModEntities;
import net.eman3600.dndreams.items.interfaces.AirSwingItem;
import net.eman3600.dndreams.items.interfaces.MagicDamageItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ConcurrentModificationException;

import static net.eman3600.dndreams.Initializer.MODID;

public class FallingStarEntity extends BeamProjectileEntity {
    public static final int DURATION = 200;
    public static final int MIN_PITCH = 70;
    public static final int MAX_PITCH = 85;
    public static final int MIN_DISTANCE = 20;
    public static final int MAX_DISTANCE = 30;
    public static final float SPEED = 2.5f;
    public static TrackedData<Integer> LIFE = DataTracker.registerData(FallingStarEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public static final Identifier TEXTURE = new Identifier(MODID, "textures/entity/projectile/falling_star.png");


    public FallingStarEntity(EntityType<? extends BeamProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public FallingStarEntity(LivingEntity owner, World world) {
        super(ModEntities.FALLING_STAR, owner, world);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        getDataTracker().startTracking(LIFE, 0);
    }

    public void tickLife() {
        getDataTracker().set(LIFE, getDataTracker().get(LIFE) + 1);
    }

    public int getLife() {
        return getDataTracker().get(LIFE);
    }

    public void initFromStack(ItemStack stack, float yaw, float pitch, int count, float distance, Vec3d target) {
        if (stack.getItem() instanceof MagicDamageItem item) {
            setDamage(item.getMagicDamage(stack) / count);
        } else {
            setDamage(1);
        }

        setYaw(yaw);
        setPitch(pitch);

        target = target.add(AirSwingItem.rayZVector(yaw, pitch).multiply(-distance));

        setPosition(target);
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();

        try {

            if (world instanceof ServerWorld) {

                Vec3d forward = AirSwingItem.rayZVector(this.getYaw(), this.getPitch());
                setVelocity(forward.multiply(SPEED));
                velocityDirty = true;

                tickLife();

                if (getLife() > DURATION) {
                    kill();
                }
            }

        } catch (NullPointerException e) {
            kill();
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        Entity target = entityHitResult.getEntity();

        if (target != null && target != getOwner() && target.canHit() && !isOnTeam(target)) {

            target.timeUntilRegen = 1;
            if (target instanceof LivingEntity livingEntity) {
                livingEntity.takeKnockback(0.4f, MathHelper.sin(getYaw() * ((float) Math.PI / 180)), -MathHelper.cos(getYaw() * ((float) Math.PI / 180)));
            }
            target.damage(DamageSource.magic(this, getOwner()), this.getDamage());

            kill();
        }
    }

    private boolean isOnTeam(Entity entity) {
        try {
            return getOwner().isTeammate(entity);
        } catch (NullPointerException e) {
            return false;
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        DataTracker tracker = getDataTracker();

        if (nbt.contains("Life")) {
            tracker.set(LIFE, nbt.getInt("Life"));
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        DataTracker tracker = getDataTracker();

        nbt.putInt("Life", tracker.get(LIFE));
    }

    public static float randomlyDistance(World world) {
        try {

            return world.random.nextBetween(MIN_DISTANCE, MAX_DISTANCE) + world.random.nextFloat();
        } catch (ConcurrentModificationException e) {
            return MIN_DISTANCE;
        }
    }

    public static float randomlyPitch(World world) {
        try {

            return world.random.nextBetween(MIN_PITCH, MAX_PITCH - 1) + world.random.nextFloat();
        } catch (ConcurrentModificationException e) {
            return MIN_DISTANCE;
        }
    }
}
