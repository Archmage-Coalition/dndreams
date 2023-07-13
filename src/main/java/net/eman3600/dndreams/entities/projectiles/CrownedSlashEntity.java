package net.eman3600.dndreams.entities.projectiles;

import net.eman3600.dndreams.events.damage_sources.AfflictionProjectileDamageSource;
import net.eman3600.dndreams.initializers.basics.ModEnchantments;
import net.eman3600.dndreams.initializers.entity.ModEntities;
import net.eman3600.dndreams.initializers.event.ModMessages;
import net.eman3600.dndreams.items.interfaces.AirSwingItem;
import net.eman3600.dndreams.items.interfaces.MagicDamageItem;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.decoration.ArmorStandEntity;
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

public class CrownedSlashEntity extends BeamProjectileEntity {
    public List<LivingEntity> victims = new ArrayList<>();
    public static final int DURATION = 4;
    public static final int DETAIL = 6;
    public static final float REACH = 3f;
    public static final float RANGE = 3f;
    public static final float PLAYER_OFFSET = 2.6f;
    public static TrackedData<Boolean> WICKED = DataTracker.registerData(CrownedSlashEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public static TrackedData<Integer> LIFE = DataTracker.registerData(CrownedSlashEntity.class, TrackedDataHandlerRegistry.INTEGER);
    public static TrackedData<Float> ROLL = DataTracker.registerData(CrownedSlashEntity.class, TrackedDataHandlerRegistry.FLOAT);


    public CrownedSlashEntity(EntityType<? extends BeamProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public CrownedSlashEntity(LivingEntity owner, World world) {
        super(ModEntities.CROWNED_SLASH, owner, world);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        getDataTracker().startTracking(WICKED, false);
        getDataTracker().startTracking(LIFE, 0);
        getDataTracker().startTracking(ROLL, 0f);
    }

    public void tickLife() {
        getDataTracker().set(LIFE, getDataTracker().get(LIFE) + 1);
    }

    public int getLife() {
        return getDataTracker().get(LIFE);
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
            updated = updated.add(AirSwingItem.rayZVector(this.getYaw(), this.getPitch()).multiply(PLAYER_OFFSET));

            setPosition(updated);
        }

        getDataTracker().set(ROLL, roll);

        getDataTracker().set(WICKED, EnchantmentHelper.getLevel(ModEnchantments.WICKED, stack) > 0);
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();

        try {

            if (world instanceof ServerWorld serverWorld) {
                for (int i = 0; i < DETAIL; i++) {

                    Vec3d renderPos = getRolledPosition(getLife() + (float)i / DETAIL);

                    PacketByteBuf packet = PacketByteBufs.create();

                    packet.writeDouble(renderPos.x);
                    packet.writeDouble(renderPos.y);
                    packet.writeDouble(renderPos.z);
                    packet.writeBoolean(getDataTracker().get(WICKED));

                    for (ServerPlayerEntity player : serverWorld.getPlayers()) {
                        ServerPlayNetworking.send(player, ModMessages.CROWNED_SLASH_ID, packet);
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

                        for (LivingEntity livingEntity : world.getNonSpectatingEntities(LivingEntity.class, box)) {
                            if (livingEntity == getOwner() || !livingEntity.canHit() || isOnTeam(livingEntity) || livingEntity instanceof ArmorStandEntity && ((ArmorStandEntity) livingEntity).isMarker())
                                continue;

                            if (!victims.contains(livingEntity)) {
                                victims.add(livingEntity);


                                livingEntity.timeUntilRegen = 1;

                                livingEntity.takeKnockback(0.4f, MathHelper.sin(getYaw() * ((float) Math.PI / 180)), -MathHelper.cos(getYaw() * ((float) Math.PI / 180)));
                                livingEntity.damage(getDataTracker().get(WICKED) ? AfflictionProjectileDamageSource.magic(this, getOwner()) : DamageSource.magic(this, getOwner()), (float) this.getDamage());
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

    private boolean isOnTeam(LivingEntity entity) {
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
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        DataTracker tracker = getDataTracker();

        if (nbt.contains("Wicked")) {
            tracker.set(WICKED, nbt.getBoolean("Wicked"));
        }
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

        nbt.putBoolean("Wicked", tracker.get(WICKED));
        nbt.putInt("Life", tracker.get(LIFE));
        nbt.putFloat("Roll", tracker.get(ROLL));
    }

    public static float randomlyRoll(World world) {
        int i = world.random.nextInt(4);

        int j = i != 0 && i != 3 ? i * 60 : i == 0 ? 30 : 150;

        return j + 90;
    }
}
