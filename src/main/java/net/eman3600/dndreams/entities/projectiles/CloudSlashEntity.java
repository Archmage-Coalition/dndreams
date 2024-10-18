package net.eman3600.dndreams.entities.projectiles;

import net.eman3600.dndreams.initializers.basics.ModEnchantments;
import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.eman3600.dndreams.initializers.entity.ModEntities;
import net.eman3600.dndreams.initializers.event.ModMessages;
import net.eman3600.dndreams.items.interfaces.AirSwingItem;
import net.eman3600.dndreams.items.interfaces.MagicDamageItem;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.ProjectileEntity;
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
import java.util.List;

public class CloudSlashEntity extends BeamProjectileEntity {
    private static final int DURATION = 7;
    private static final double SPEED = 1.2d;
    public static TrackedData<Integer> LIFE = DataTracker.registerData(CloudSlashEntity.class, TrackedDataHandlerRegistry.INTEGER);
    public static TrackedData<Float> ROLL = DataTracker.registerData(CloudSlashEntity.class, TrackedDataHandlerRegistry.FLOAT);

    public CloudSlashEntity(EntityType<? extends BeamProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public CloudSlashEntity(LivingEntity owner, World world) {
        super(ModEntities.CLOUD_SLASH, owner, world);
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
                        ServerPlayNetworking.send(player, ModMessages.CLOUD_SLASH_ID, packet);
                    }
                }

                Vec3d forward = AirSwingItem.rayZVector(this.getYaw(), this.getPitch());
                setVelocity(forward.multiply(SPEED));
                velocityDirty = true;


                if (isDamaging()) {
                    for (int i = 0; i <= CrownedSlashEntity.DURATION; i++) {
                        Vec3d center = getRolledPosition(i);

                        for (float k = 0; k < CrownedSlashEntity.REACH; k += 1d) {
                            Vec3d boxCenter = center.add(forward.multiply(k));
                            Box box = new Box(boxCenter, boxCenter).expand(0.5d);

                            for (Entity target : world.getNonSpectatingEntities(Entity.class, box)) {
                                if (target == getOwner() || !target.canHit() || isOnTeam(target))
                                    continue;

                                if (target instanceof LivingEntity livingEntity) {
                                    livingEntity.takeKnockback(0.6f, MathHelper.sin(getYaw() * ((float) Math.PI / 180)), -MathHelper.cos(getYaw() * ((float) Math.PI / 180)));
                                } else if (target instanceof ProjectileEntity projectile) {

                                    Vec3d deltaV = forward.multiply(.8f);
                                    projectile.addVelocity(deltaV.x, deltaV.y, deltaV.z);

                                    target.damage(DamageSource.magic(this, getOwner()), this.getDamage());
                                }

                            }

                        }
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
