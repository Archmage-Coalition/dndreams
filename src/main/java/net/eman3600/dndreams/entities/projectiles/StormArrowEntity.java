package net.eman3600.dndreams.entities.projectiles;

import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.eman3600.dndreams.initializers.entity.ModEntities;
import net.eman3600.dndreams.items.interfaces.AirSwingItem;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class StormArrowEntity extends PersistentProjectileEntity implements GravityProjectileEntity {

    public static final TrackedData<Boolean> RAIN = DataTracker.registerData(StormArrowEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    private static final int MIN_ARROWS = 4;
    private static final int MAX_ARROWS = 7;
    private static final int MIN_DISTANCE = 65;
    private static final int MAX_DISTANCE = 105;
    private static final int MIN_PITCH = 60;
    private static final int MAX_PITCH = 80;
    private static final double MAX_OFFSET = 3;
    private static final double RAIN_SPEED = 5.9;
    private static final int SPACING = 3;

    public StormArrowEntity(EntityType<? extends StormArrowEntity> entityType, World world) {
        super(entityType, world);
    }

    public StormArrowEntity(World world, LivingEntity owner, boolean rain) {
        super(ModEntities.STORM_ARROW, owner, world);

        dataTracker.set(RAIN, rain);
        pickupType = rain ? PickupPermission.DISALLOWED : pickupType;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();

        dataTracker.startTracking(RAIN, false);
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(ModItems.STORM_ARROW);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        nbt.putBoolean("Rain", dataTracker.get(RAIN));
    }

    @Override
    public void tick() {
        super.tick();

        if (!world.isClient && dataTracker.get(RAIN) && inGroundTime > 140) {
            discard();
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        dataTracker.set(RAIN, nbt.getBoolean("Rain"));
    }

    @Override
    public float getGravity() {
        return dataTracker.get(RAIN) ? .02f : 0.05f;
    }

    private void rainArrows(double velocity, boolean critical) {
        int arrows = MathHelper.ceil((world.random.nextBetween(MIN_ARROWS, MAX_ARROWS) + world.random.nextDouble()) * velocity);

        System.out.println("Impact velocity: " + velocity);

        for (int i = 0; i < arrows; i++) {

            StormArrowEntity arrow = new StormArrowEntity(world, getOwner() instanceof LivingEntity e ? e : null, true);

            Vec3d target = getPos().add(randomOffset(), randomOffset(), randomOffset());
            int distance = world.random.nextBetween(MIN_DISTANCE, MAX_DISTANCE) + (SPACING * i);

            float yaw = world.random.nextInt(360) - 180;
            float pitch = world.random.nextBetween(MIN_PITCH, MAX_PITCH);

            setYaw(yaw);
            setPitch(pitch);

            target = target.add(AirSwingItem.rayZVector(yaw, pitch).multiply(-distance));
            arrow.setVelocity(AirSwingItem.rayZVector(yaw, pitch).multiply(RAIN_SPEED));
            arrow.setPosition(target);

            arrow.setDamage(getDamage() * velocity / RAIN_SPEED);
            arrow.setCritical(critical);

            world.spawnEntity(arrow);
        }
    }

    private double randomOffset() {
        return (world.random.nextDouble() - .5) * 2 * MAX_OFFSET;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);

        if (!dataTracker.get(RAIN) && !world.isClient) {

            rainArrows(getVelocity().length(), isCritical());
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        boolean critical = isCritical();
        Vec3d velocity = getVelocity();
        super.onBlockHit(blockHitResult);

        if (!dataTracker.get(RAIN) && !world.isClient) {

            rainArrows(velocity.length(), critical);
        }
    }

    @Override
    protected void onHit(LivingEntity target) {
        super.onHit(target);

        target.addStatusEffect(new StatusEffectInstance(ModStatusEffects.SMOTE, dataTracker.get(RAIN) ? 80 : 140));
    }
}
