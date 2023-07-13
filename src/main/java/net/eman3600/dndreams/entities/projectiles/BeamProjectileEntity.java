package net.eman3600.dndreams.entities.projectiles;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class BeamProjectileEntity extends ProjectileEntity {

    public static TrackedData<Float> DAMAGE = DataTracker.registerData(BeamProjectileEntity.class, TrackedDataHandlerRegistry.FLOAT);
    public static TrackedData<Boolean> DAMAGING = DataTracker.registerData(BeamProjectileEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public BeamProjectileEntity(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    protected BeamProjectileEntity(EntityType<? extends ProjectileEntity> type, double x, double y, double z, World world) {
        this(type, world);
        this.setPosition(x, y, z);
    }

    public BeamProjectileEntity(EntityType<? extends ProjectileEntity> entityType, LivingEntity owner, World world) {
        this(entityType, owner.getX(), owner.getEyeY() - (double)0.1f, owner.getZ(), world);
        this.setOwner(owner);
    }

    public void setDamage(float damage) {
        getDataTracker().set(DAMAGE, damage);
    }

    public float getDamage() {
        return getDataTracker().get(DAMAGE);
    }

    public void setDamaging(boolean damaging) {
        getDataTracker().set(DAMAGING, damaging);
    }

    public boolean isDamaging() {
        return getDataTracker().get(DAMAGING);
    }

    @Override
    protected void initDataTracker() {
        getDataTracker().startTracking(DAMAGE, 3f);
        getDataTracker().startTracking(DAMAGING, true);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        DataTracker tracker = getDataTracker();

        if (nbt.contains("Damage")) {
            tracker.set(DAMAGE, nbt.getFloat("Damage"));
        }
        if (nbt.contains("Damaging")) {
            tracker.set(DAMAGING, nbt.getBoolean("Damaging"));
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        DataTracker tracker = getDataTracker();

        nbt.putFloat("Damage", tracker.get(DAMAGE));
        nbt.putBoolean("Damaging", tracker.get(DAMAGING));
    }

    protected SoundEvent getCollideSound() {
        return SoundEvents.BLOCK_AMETHYST_BLOCK_HIT;
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);

        this.playSound(this.getCollideSound(), 1.0f, 1.2f / (this.random.nextFloat() * 0.2f + 0.9f));
        kill();
    }

    @Override
    public void tick() {
        super.tick();

        Vec3d velocity = this.getVelocity();
        Vec3d vec3d3 = this.getPos();
        HitResult hitResult = this.world.raycast(new RaycastContext(vec3d3, vec3d3.add(velocity), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this));
        if (hitResult.getType() != HitResult.Type.MISS) {
            onCollision(hitResult);
        }

        velocity = this.getVelocity();
        double e = velocity.x;
        double f = velocity.y;
        double g = velocity.z;

        double h = this.getX() + e;
        double j = this.getY() + f;
        double k = this.getZ() + g;

        this.setPosition(h, j, k);
        this.checkBlockCollision();
    }
}
