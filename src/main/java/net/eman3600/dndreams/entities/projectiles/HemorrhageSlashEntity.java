package net.eman3600.dndreams.entities.projectiles;

import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.eman3600.dndreams.initializers.entity.ModEntities;
import net.eman3600.dndreams.initializers.event.ModMessages;
import net.eman3600.dndreams.items.interfaces.AirSwingItem;
import net.eman3600.dndreams.items.interfaces.BloodlustItem;
import net.eman3600.dndreams.items.interfaces.MagicDamageItem;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class HemorrhageSlashEntity extends BeamProjectileEntity {
    public List<Entity> victims = new ArrayList<>();
    public static final int DURATION = 4;
    public static final int DETAIL = 6;
    public static final float REACH = 3f;
    public static final float RANGE = 3f;
    public static final float PLAYER_OFFSET = 2.6f;
    public static TrackedData<Integer> LIFE = DataTracker.registerData(HemorrhageSlashEntity.class, TrackedDataHandlerRegistry.INTEGER);
    public static TrackedData<Float> ROLL = DataTracker.registerData(HemorrhageSlashEntity.class, TrackedDataHandlerRegistry.FLOAT);


    public HemorrhageSlashEntity(EntityType<? extends BeamProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public HemorrhageSlashEntity(LivingEntity owner, World world) {
        super(ModEntities.HEMORRHAGE_SLASH, owner, world);
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

    public void initFromStack(ItemStack stack) {
        if (stack.getItem() instanceof MagicDamageItem item) {
            setDamage(item.getMagicDamage(stack));
        } else {
            setDamage(1);
        }

        if (getOwner() != null) {
            setYaw(getOwner().getYaw());
            setPitch(getOwner().getPitch());

            Vec3d updated = getPos();
            updated = updated.add(AirSwingItem.rayZVector(this.getYaw(), this.getPitch()).multiply(PLAYER_OFFSET));

            setPosition(updated);
        }
    }

    public void initFromScar(HemorrhageScarEntity scar, float roll) {
        setDamage(scar.getDamage());

        if (getOwner() != null) {
            setYaw(scar.getYaw());
            setPitch(scar.getPitch());

            setPosition(scar.getPos());
        }

        getDataTracker().set(ROLL, roll);
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    public void tick() {

        if (!world.isClient && firstUpdate && dataTracker.get(ROLL) == 0) {
            dataTracker.set(ROLL, CrownedSlashEntity.randomlyRoll(world));
        }

        super.tick();

        try {

            if (world instanceof ServerWorld serverWorld) {
                for (int i = 0; i < DETAIL; i++) {

                    Vec3d renderPos = getRolledPosition(getLife() + (float)i / DETAIL);

                    PacketByteBuf packet = PacketByteBufs.create();

                    packet.writeDouble(renderPos.x);
                    packet.writeDouble(renderPos.y);
                    packet.writeDouble(renderPos.z);

                    for (ServerPlayerEntity player : serverWorld.getPlayers()) {
                        ServerPlayNetworking.send(player, ModMessages.HEMORRHAGE_SLASH_ID, packet);
                    }
                }

                Vec3d forward = AirSwingItem.rayZVector(this.getYaw(), this.getPitch());

                setVelocity(0, 0, 0);
                velocityDirty = true;

                Vec3d center = getRolledPosition(getLife());

                if (isDamaging()) {
                    for (float k = 0; k < REACH; k += 1d) {
                        Vec3d boxCenter = center.subtract(forward.multiply(k));
                        Box box = new Box(boxCenter, boxCenter).expand(0.5d);

                        for (Entity target : world.getNonSpectatingEntities(LivingEntity.class, box)) {
                            if (target == getOwner() || !target.canHit() || isOnTeam(target))
                                continue;

                            if (!victims.contains(target)) {
                                victims.add(target);


                                target.timeUntilRegen = 1;

                                if (target instanceof LivingEntity livingEntity) {
                                    livingEntity.takeKnockback(0.4f, MathHelper.sin(getYaw() * ((float) Math.PI / 180)), -MathHelper.cos(getYaw() * ((float) Math.PI / 180)));

                                    livingEntity.addStatusEffect(new StatusEffectInstance(ModStatusEffects.HEARTBLEED, 200));
                                }
                                target.damage(BloodlustItem.hemorrhage(getOwner()), this.getDamage());


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
    }

    private boolean isOnTeam(Entity entity) {
        try {
            return getOwner().isTeammate(entity);
        } catch (NullPointerException e) {
            return false;
        }
    }

    private Vec3d getRolledPosition(float delta) {
        Vec3d result = getPos();

        float distance = (delta * RANGE / DURATION) - (RANGE * .5f);

        Vec3d offset = AirSwingItem.rollYVector(getYaw(), getPitch(), getDataTracker().get(ROLL)).multiply(distance);

        return result.add(offset);
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

    public static float randomlyRoll(World world) {
        try {
            int i = world.random.nextInt(4);

            int j = i != 0 && i != 3 ? i * 60 : i == 0 ? 30 : 150;

            return j + 90;
        } catch (ConcurrentModificationException e) {
            return 120;
        }
    }
}
