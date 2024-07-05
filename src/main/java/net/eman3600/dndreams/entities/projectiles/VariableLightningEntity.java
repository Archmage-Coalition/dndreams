package net.eman3600.dndreams.entities.projectiles;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

public class VariableLightningEntity extends LightningEntity {

    public static final TrackedData<Float> DAMAGE = DataTracker.registerData(VariableLightningEntity.class, TrackedDataHandlerRegistry.FLOAT);

    public VariableLightningEntity(EntityType<? extends LightningEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();

        dataTracker.startTracking(DAMAGE, 8f);
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        nbt.putFloat("damage", dataTracker.get(DAMAGE));
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        dataTracker.set(DAMAGE, nbt.getFloat("damage"));
    }

    public float getDamage() {
        return dataTracker.get(DAMAGE);
    }

    public void setDamage(float damage) {
        dataTracker.set(DAMAGE, damage);
    }
}
