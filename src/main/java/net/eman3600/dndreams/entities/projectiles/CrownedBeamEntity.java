package net.eman3600.dndreams.entities.projectiles;

import net.eman3600.dndreams.events.damage_sources.AfflictionProjectileDamageSource;
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

public class CrownedBeamEntity extends BeamProjectileEntity {
    private static final int DURATION = 60;
    private static final int DURATION_WEAK = 5;
    private static final double SPEED = 1.0d;
    public List<Entity> victims = new ArrayList<>();
    public static TrackedData<Boolean> WICKED = DataTracker.registerData(CrownedBeamEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public static TrackedData<Boolean> WEAK = DataTracker.registerData(CrownedBeamEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public static TrackedData<Integer> LIFE = DataTracker.registerData(CrownedBeamEntity.class, TrackedDataHandlerRegistry.INTEGER);
    public static TrackedData<Float> ROLL = DataTracker.registerData(CrownedBeamEntity.class, TrackedDataHandlerRegistry.FLOAT);

    public CrownedBeamEntity(EntityType<? extends BeamProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public CrownedBeamEntity(LivingEntity owner, World world) {
        super(ModEntities.CROWNED_BEAM, owner, world);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        getDataTracker().startTracking(WICKED, false);
        getDataTracker().startTracking(WEAK, false);
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
        return dataTracker.get(WEAK) ? DURATION_WEAK : DURATION;
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

        getDataTracker().set(WICKED, EnchantmentHelper.getLevel(ModEnchantments.WICKED, stack) > 0);
        getDataTracker().set(WEAK, stack.isOf(ModItems.CROWNED_EDGE));
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
                    packet.writeBoolean(getDataTracker().get(WICKED));
                    packet.writeBoolean(getDataTracker().get(WEAK));

                    for (ServerPlayerEntity player : serverWorld.getPlayers()) {
                        ServerPlayNetworking.send(player, ModMessages.CROWNED_BEAM_ID, packet);
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

                                if (!victims.contains(target)) {
                                    victims.add(target);


                                    target.timeUntilRegen = 1;

                                    if (target instanceof LivingEntity livingEntity) {
                                        livingEntity.takeKnockback(0.4f, MathHelper.sin(getYaw() * ((float) Math.PI / 180)), -MathHelper.cos(getYaw() * ((float) Math.PI / 180)));

                                        if (dataTracker.get(WICKED)) {
                                            livingEntity.addStatusEffect(new StatusEffectInstance(ModStatusEffects.MORTAL, 200));
                                        }
                                    }
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
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        DataTracker tracker = getDataTracker();

        if (nbt.contains("Wicked")) {
            tracker.set(WICKED, nbt.getBoolean("Wicked"));
        }
        if (nbt.contains("Weak")) {
            tracker.set(WEAK, nbt.getBoolean("Weak"));
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
        nbt.putBoolean("Weak", tracker.get(WEAK));
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
