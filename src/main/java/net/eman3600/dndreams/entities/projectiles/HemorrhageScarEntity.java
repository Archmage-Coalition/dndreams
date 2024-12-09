package net.eman3600.dndreams.entities.projectiles;

import net.eman3600.dndreams.initializers.entity.ModEntities;
import net.eman3600.dndreams.initializers.event.ModMessages;
import net.eman3600.dndreams.items.interfaces.AirSwingItem;
import net.eman3600.dndreams.items.interfaces.MagicDamageItem;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class HemorrhageScarEntity extends BeamProjectileEntity {
    private static final int DURATION = 120;
    public static TrackedData<Integer> LIFE = DataTracker.registerData(HemorrhageScarEntity.class, TrackedDataHandlerRegistry.INTEGER);
    public static TrackedData<Float> ROLL = DataTracker.registerData(HemorrhageScarEntity.class, TrackedDataHandlerRegistry.FLOAT);

    public HemorrhageScarEntity(EntityType<? extends BeamProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public HemorrhageScarEntity(LivingEntity owner, World world) {
        super(ModEntities.HEMORRHAGE_SCAR, owner, world);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        getDataTracker().startTracking(LIFE, 0);
        getDataTracker().startTracking(ROLL, 0f);
    }

    public void tickLife() {
        getDataTracker().set(LIFE, getDataTracker().get(LIFE) + 1);
    }

    public int getLife() {
        return getDataTracker().get(LIFE);
    }

    public int getDuration() {
        return DURATION;
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    public void initFromStack(ItemStack stack, float roll) {
        if (stack.getItem() instanceof MagicDamageItem item) {
            setDamage(item.getMagicDamage(stack));
        } else {
            setDamage(1);
        }

        if (getOwner() != null) {
            setYaw(getOwner().getYaw());
            setPitch(getOwner().getPitch());

            Vec3d updated = getPos();
            updated = updated.add(AirSwingItem.rayZVector(this.getYaw(), this.getPitch()).multiply(CrownedSlashEntity.PLAYER_OFFSET));

            setPosition(updated);
        }

        getDataTracker().set(ROLL, roll);
    }

    @Override
    public void tick() {
        try {

            if (world instanceof ServerWorld serverWorld && !firstUpdate) {
                for (int i = 0; i <= CrownedSlashEntity.DETAIL * CrownedSlashEntity.DURATION; i++) {

                    Vec3d renderPos = getRolledPosition((float)i / (CrownedSlashEntity.DETAIL));

                    PacketByteBuf packet = PacketByteBufs.create();

                    packet.writeDouble(renderPos.x);
                    packet.writeDouble(renderPos.y);
                    packet.writeDouble(renderPos.z);

                    for (ServerPlayerEntity player : serverWorld.getPlayers()) {
                        ServerPlayNetworking.send(player, ModMessages.HEMORRHAGE_SCAR_ID, packet);
                    }
                }

                tickLife();

                if (getLife() > getDuration()) {
                    kill();
                }

            }

        } catch (NullPointerException e) {
            kill();
        }


        super.tick();
    }

    public void slash() {
        if (!world.isClient) {

            LivingEntity owner = getOwner() instanceof LivingEntity o ? o : null;

            world.playSound(null, getX(), getY(), getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, this.getSoundCategory(), 1.0f, .8f);
            world.playSound(null, getX(), getY(), getZ(), SoundEvents.ENTITY_DROWNED_AMBIENT, this.getSoundCategory(), 1.0f, 2f);

            HemorrhageSlashEntity slash = new HemorrhageSlashEntity(owner, world);
            slash.initFromScar(this, dataTracker.get(ROLL));
            world.spawnEntity(slash);

            discard();
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
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        DataTracker tracker = getDataTracker();

        if (nbt.contains("Life")) {
            tracker.set(LIFE, nbt.getInt("Life"));
        }
        if (nbt.contains("Roll")) {
            tracker.set(ROLL, nbt.getFloat("Roll"));
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        DataTracker tracker = getDataTracker();

        nbt.putInt("Life", tracker.get(LIFE));
        nbt.putFloat("Roll", tracker.get(ROLL));
    }

    private Vec3d getRolledPosition(float delta) {
        Vec3d result = getPos();

        float distance = (delta * CrownedSlashEntity.RANGE / CrownedSlashEntity.DURATION) - (CrownedSlashEntity.RANGE * .5f);

        Vec3d offset = AirSwingItem.rollYVector(getYaw(), getPitch(), getDataTracker().get(ROLL)).multiply(distance);

        return result.add(offset);
    }
}
