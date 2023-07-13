package net.eman3600.dndreams.entities.projectiles;

import net.eman3600.dndreams.events.damage_sources.ElectricProjectileDamageSource;
import net.eman3600.dndreams.initializers.entity.ModEntities;
import net.eman3600.dndreams.initializers.event.ModMessages;
import net.eman3600.dndreams.items.interfaces.AirSwingItem;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class TeslaSlashEntity extends BeamProjectileEntity {
    private static final int DURATION = 40;
    private static final double SPEED = 2.0d;
    public List<LivingEntity> victims = new ArrayList<>();
    public static TrackedData<Integer> LIFE = DataTracker.registerData(TeslaSlashEntity.class, TrackedDataHandlerRegistry.INTEGER);
    public static TrackedData<Float> ROLL = DataTracker.registerData(TeslaSlashEntity.class, TrackedDataHandlerRegistry.FLOAT);

    public TeslaSlashEntity(EntityType<? extends BeamProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public TeslaSlashEntity(LivingEntity owner, World world, float shock) {
        super(ModEntities.TESLA_SLASH, owner, world);
        setDamage(shock);
        getDataTracker().set(ROLL, CrownedSlashEntity.randomlyRoll(world));

        if (getOwner() != null) {
            setYaw(getOwner().getYaw());
            setPitch(getOwner().getPitch());

            Vec3d updated = getPos();
            updated = updated.add(AirSwingItem.rayZVector(this.getYaw(), this.getPitch()).multiply(CrownedSlashEntity.PLAYER_OFFSET));

            setPosition(updated);
        }
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

    @Override
    public boolean hasNoGravity() {
        return true;
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
                        ServerPlayNetworking.send(player, ModMessages.TESLA_SLASH_ID, packet);
                    }
                }

                Vec3d forward = AirSwingItem.rayZVector(this.getYaw(), this.getPitch());
                System.out.println(forward);
                setVelocity(forward.multiply(SPEED));
                velocityDirty = true;


                if (isDamaging()) {
                    for (int i = 0; i <= CrownedSlashEntity.DURATION; i++) {
                        Vec3d center = getRolledPosition(i);

                        for (float k = 0; k < CrownedSlashEntity.REACH; k += 1d) {
                            Vec3d boxCenter = center.add(forward.multiply(k));
                            Box box = new Box(boxCenter, boxCenter).expand(0.5d);

                            for (LivingEntity livingEntity : world.getNonSpectatingEntities(LivingEntity.class, box)) {
                                if (livingEntity == getOwner() || !livingEntity.canHit() || isOnTeam(livingEntity) || livingEntity instanceof ArmorStandEntity && ((ArmorStandEntity) livingEntity).isMarker())
                                    continue;

                                if (!victims.contains(livingEntity)) {
                                    victims.add(livingEntity);


                                    livingEntity.timeUntilRegen = 1;

                                    livingEntity.takeKnockback(0.4f, MathHelper.sin(getYaw() * ((float) Math.PI / 180)), -MathHelper.cos(getYaw() * ((float) Math.PI / 180)));
                                    livingEntity.damage(ElectricProjectileDamageSource.projectile(this, getOwner()), this.getDamage());
                                }
                            }

                        }
                    }
                }

                tickLife();

                if (getLife() > DURATION) {
                    kill();
                }

            }

        } catch (NullPointerException e) {
            kill();
        }


        super.tick();
    }





    private boolean isOnTeam(LivingEntity entity) {
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
        if (nbt.contains("Roll")) {
            tracker.set(ROLL, nbt.getFloat("Roll"));
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

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
