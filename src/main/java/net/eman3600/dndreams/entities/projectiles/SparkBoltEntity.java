package net.eman3600.dndreams.entities.projectiles;

import net.eman3600.dndreams.entities.WaterIgnorant;
import net.eman3600.dndreams.initializers.entity.ModEntities;
import net.eman3600.dndreams.initializers.event.ModParticles;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import static net.eman3600.dndreams.Initializer.MODID;

public class SparkBoltEntity extends ThrownEntity implements WaterIgnorant {

    public static final Identifier TEXTURE = new Identifier(MODID, "textures/entity/projectile/spark_bolt.png");
    private static final TrackedData<Float> DAMAGE = DataTracker.registerData(SparkBoltEntity.class, TrackedDataHandlerRegistry.FLOAT);



    public SparkBoltEntity(EntityType<? extends ThrownEntity> entityType, World world) {
        super(entityType, world);
    }

    public SparkBoltEntity(LivingEntity owner, World world) {
        super(ModEntities.SPARK_BOLT, owner, world);
    }

    public SparkBoltEntity(World world, double x, double y, double z) {
        super(ModEntities.SPARK_BOLT, x, y, z, world);
    }

    @Override
    protected void initDataTracker() {
        this.getDataTracker().startTracking(DAMAGE, 3f);
    }

    public void setDamage(float damage) {
        this.getDataTracker().set(DAMAGE, damage);
    }



    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        Entity entity = entityHitResult.getEntity();

        if (entity == getOwner()) return;

        float i = getDataTracker().get(DAMAGE);
        entity.timeUntilRegen = 0;
        entity.damage(DamageSource.magic(this, this.getOwner()), i);
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.world.isClient) {
            this.world.sendEntityStatus(this, EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES);
            this.discard();
        }
    }

    @Override
    protected float getGravity() {
        return 0.07f;
    }

    @Override
    public void tick() {
        super.tick();

        Vec3d vec3d = this.getVelocity();
        double e = vec3d.x;
        double f = vec3d.y;
        double g = vec3d.z;
        if (world.isClient()) {
            for (int i = 0; i < 2; ++i) {
                this.world.addParticle(ModParticles.DIAMOND_SPARK, this.getX() + e * (double)i / 4.0, this.getY() + f * (double)i / 4.0, this.getZ() + g * (double)i / 4.0, -e, -f + 0.2, -g);
            }
        }
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        nbt.putFloat("Damage", dataTracker.get(DAMAGE));
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        dataTracker.set(DAMAGE, nbt.getFloat("Damage"));
    }
}
