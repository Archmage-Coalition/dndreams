package net.eman3600.dndreams.entities.projectiles;

import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.initializers.entity.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public class SkyboundArrowEntity extends PersistentProjectileEntity implements GravityProjectileEntity {
    private static final double BLAST_RANGE = 5d;

    private double ascension = 0d;

    public SkyboundArrowEntity(EntityType<? extends SkyboundArrowEntity> entityType, World world) {
        super(entityType, world);
    }

    public SkyboundArrowEntity(World world, LivingEntity owner) {
        super(ModEntities.SKYBOUND_ARROW, owner, world);
    }

    public SkyboundArrowEntity(World world, double x, double y, double z) {
        super(ModEntities.SKYBOUND_ARROW, x, y, z, world);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(ModItems.SKYBOUND_ARROW);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        nbt.putDouble("Ascension", ascension);
    }

    @Override
    public void tick() {
        super.tick();

        ascension += getY() - prevY;

        if (!world.isClient && ascension > 400) {
            discard();
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        ascension = nbt.getDouble("Ascension");
    }

    @Override
    public float getGravity() {
        return -0.05f;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        double d = getDamage();
        setDamage(d * 1.5f);
        super.onEntityHit(entityHitResult);
        setDamage(d);
    }
}
