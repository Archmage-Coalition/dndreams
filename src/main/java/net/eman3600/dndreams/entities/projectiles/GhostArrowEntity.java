package net.eman3600.dndreams.entities.projectiles;

import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.initializers.entity.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public class GhostArrowEntity extends PersistentProjectileEntity implements GravityProjectileEntity {

    private int time = 0;
    private static final int MAX_TIME = 40;
    private static final double VELOCITY_MULT = 1.5d;

    private static final double DAMAGE_MULT = 1.2 / VELOCITY_MULT;

    public GhostArrowEntity(EntityType<? extends GhostArrowEntity> entityType, World world) {
        super(entityType, world);
    }

    public GhostArrowEntity(World world, LivingEntity owner) {
        super(ModEntities.GHOST_ARROW, owner, world);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(ModItems.GHOST_ARROW);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        nbt.putInt("GhostTime", time);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        time = nbt.getInt("GhostTime");
    }

    @Override
    public void tick() {
        if (firstUpdate) {
            setVelocity(getVelocity().multiply(VELOCITY_MULT));
        }

        super.tick();

        if (this.world.isClient && !this.inGround) {
            this.world.addParticle(ParticleTypes.FALLING_OBSIDIAN_TEAR, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
        }


        time++;

        if (!world.isClient && time > MAX_TIME) {
            discard();
        }
    }

    @Override
    public float getGravity() {
        return 0f;
    }

    @Override
    public boolean penetratesBlocks() {
        return true;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        setPierceLevel((byte) 16);

        double d = getDamage();
        setDamage(d * DAMAGE_MULT);
        super.onEntityHit(entityHitResult);
        setDamage(d);
    }

    @Override
    protected void checkBlockCollision() {
        // Lol no
    }
}
