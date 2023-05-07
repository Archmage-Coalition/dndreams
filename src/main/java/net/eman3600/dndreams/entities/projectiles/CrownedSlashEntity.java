package net.eman3600.dndreams.entities.projectiles;

import net.eman3600.dndreams.events.damage_sources.ElectricProjectileDamageSource;
import net.eman3600.dndreams.initializers.basics.ModEnchantments;
import net.eman3600.dndreams.initializers.entity.ModEntities;
import net.eman3600.dndreams.initializers.event.ModMessages;
import net.eman3600.dndreams.items.interfaces.AirSwingItem;
import net.eman3600.dndreams.items.interfaces.MagicDamageItem;
import net.eman3600.dndreams.util.matrices.RotatedBox;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class CrownedSlashEntity extends PersistentProjectileEntity implements ProjectileOverhaulEntity {
    private Vec3d origin;
    public List<LivingEntity> victims = new ArrayList<>();
    public static final float yawPerTick = 2;
    public static final float changeInYaw = 120;
    public static final int moveTicksPerTick = 12;
    public static final float range = 3f;
    public CrownedBeamEntity beam = null;
    private int lifeTicks = 0;
    private int failsafeTicks = 0;
    private boolean wicked = false;


    public CrownedSlashEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public CrownedSlashEntity(double x, double y, double z, World world) {
        super(ModEntities.CROWNED_SLASH, x, y, z, world);
    }

    public CrownedSlashEntity(LivingEntity owner, World world) {
        super(ModEntities.CROWNED_SLASH, owner, world);
    }

    @Override
    public ItemStack asItemStack() {
        return null;
    }

    public void initFromStack(ItemStack stack) {
        if (stack.getItem() instanceof MagicDamageItem item) {
            this.setDamage(item.getMagicDamage(stack));
        } else {
            setDamage(1);
        }

        origin = getPos().subtract(0, 1d, 0);

        setYaw(getOwner().getYaw() + changeInYaw/2);

        wicked = EnchantmentHelper.getLevel(ModEnchantments.WICKED, stack) > 0;

        setPierceLevel((byte) 3);
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();

        try {

            for (int l = 0; l < moveTicksPerTick; l++) {
                if (origin != null && world instanceof ServerWorld serverWorld) {

                    setYaw(getYaw() - yawPerTick);
                    if (getBoundingBox() instanceof RotatedBox box) {
                        setBoundingBox(box.rotated(-yawPerTick));
                    }

                    Vec3d forward = AirSwingItem.rayZVector(this.getYaw(), this.getPitch());

                    setPosition(origin.add(forward.multiply(range)));
                    setVelocity(0, 0, 0);

                    lifeTicks++;

                    if (lifeTicks > changeInYaw / yawPerTick) {
                        break;
                    }

                    Vec3d center = getBoundingBox().getCenter();

                    for (float k = 0; k < range; k += 0.5d) {
                        Vec3d boxCenter = center.subtract(forward.multiply(k));
                        Box box = new Box(boxCenter, boxCenter).expand(0.25d);

                        for (LivingEntity livingEntity : world.getNonSpectatingEntities(LivingEntity.class, box)) {
                            if (livingEntity == getOwner() || isOnTeam(livingEntity) || livingEntity instanceof ArmorStandEntity && ((ArmorStandEntity) livingEntity).isMarker())
                                continue;

                            if (!victims.contains(livingEntity)) {
                                victims.add(livingEntity);
                                if (beam != null) {
                                    beam.victims.add(livingEntity);
                                }


                                livingEntity.timeUntilRegen = 1;

                                livingEntity.takeKnockback(0.4f, MathHelper.sin(getOwner().getYaw() * ((float) Math.PI / 180)), -MathHelper.cos(getOwner().getYaw() * ((float) Math.PI / 180)));
                                livingEntity.damage(ElectricProjectileDamageSource.magic(this, getOwner()), (float) this.getDamage());
                                if (wicked) livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 80, 1), getOwner());
                            }
                        }

                    }

                    PacketByteBuf packet = PacketByteBufs.create();

                    packet.writeDouble(getX());
                    packet.writeDouble(getY());
                    packet.writeDouble(getZ());
                    packet.writeBoolean(wicked);

                    for (ServerPlayerEntity player : serverWorld.getPlayers()) {
                        ServerPlayNetworking.send(player, ModMessages.CROWNED_SLASH_ID, packet);
                    }

                    updatePositionAndAngles(getX(), getY(), getZ(), getYaw(), getPitch());

                }
            }

            if (lifeTicks > changeInYaw / yawPerTick && world instanceof ServerWorld) {
                kill();
            }

        } catch (NullPointerException e) {
            kill();
        }

        if (++failsafeTicks > 600) {
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

    @Override
    public void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
    }

    @Override
    protected boolean tryPickup(PlayerEntity player) {
        return false;
    }

    @Override
    public void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            this.kill();
        }
    }

    @Override
    protected SoundEvent getHitSound() {
        return SoundEvents.BLOCK_AMETHYST_BLOCK_HIT;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putInt("life_ticks", lifeTicks);
        nbt.putInt("failsafe_ticks", failsafeTicks);

        return super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        lifeTicks = nbt.getInt("life_ticks");
        failsafeTicks = nbt.getInt("failsafe_ticks");

        super.readNbt(nbt);
    }
}
