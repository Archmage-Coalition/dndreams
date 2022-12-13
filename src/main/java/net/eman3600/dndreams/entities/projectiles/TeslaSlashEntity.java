package net.eman3600.dndreams.entities.projectiles;

import net.eman3600.dndreams.events.damage_sources.ElectricProjectileDamageSource;
import net.eman3600.dndreams.initializers.entity.ModEntities;
import net.eman3600.dndreams.initializers.event.ModMessages;
import net.eman3600.dndreams.items.interfaces.AirSwingItem;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

import static net.eman3600.dndreams.entities.projectiles.CrownedSlashEntity.*;

public class TeslaSlashEntity extends PersistentProjectileEntity implements ProjectileOverhaulEntity {
    private int lifeTicks = 0;
    private static final int maxLifeTicks = 5;
    private static final double SPEED = 2.0d;
    public List<LivingEntity> victims = new ArrayList<>();
    private Vec3d origin;
    private float shock = 0f;

    public TeslaSlashEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public TeslaSlashEntity(LivingEntity owner, World world, float shock) {
        super(ModEntities.TESLA_SLASH, owner, world);
        this.shock = shock;
        setDamage(shock);

        setPos(getX(), getY() - 1, getZ());
        origin = getPos();

        setYaw(getOwner().getYaw());
        setPierceLevel((byte) 3);
    }

    @Override
    protected ItemStack asItemStack() {
        return null;
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    public void tick() {
        if (world instanceof ServerWorld serverWorld && !firstUpdate) {
            if (origin != null) {
                try {

                    Vec3d forward = AirSwingItem.rayZVector(getYaw(), getPitch());
                    Vec3d focus = getPos().subtract(forward.multiply(-range));

                    for (float i = -changeInYaw / 2; i <= changeInYaw / 2; i += yawPerTick) {
                        Vec3d curve = AirSwingItem.rayZVector(getYaw() + i, getPitch());

                        for (float k = 1; k < range; k += 0.5d) {
                            Vec3d boxCenter = focus.add(curve.multiply(k));
                            Box box = new Box(boxCenter, boxCenter).expand(0.25d);

                            for (LivingEntity livingEntity : world.getNonSpectatingEntities(LivingEntity.class, box)) {
                                if (livingEntity == getOwner() || isOnTeam(livingEntity) || livingEntity instanceof ArmorStandEntity && ((ArmorStandEntity) livingEntity).isMarker())
                                    continue;

                                if (!victims.contains(livingEntity)) {
                                    victims.add(livingEntity);

                                    livingEntity.timeUntilRegen = 1;

                                    livingEntity.takeKnockback(0.4f, MathHelper.sin(getOwner().getYaw() * ((float) Math.PI / 180)), -MathHelper.cos(getOwner().getYaw() * ((float) Math.PI / 180)));
                                    livingEntity.damage(ElectricProjectileDamageSource.projectile(this, getOwner()), (float) this.getDamage());
                                }
                            }

                        }

                        Vec3d edge = focus.add(curve.multiply(range));

                        PacketByteBuf packet = PacketByteBufs.create();

                        packet.writeDouble(edge.x);
                        packet.writeDouble(edge.y);
                        packet.writeDouble(edge.z);

                        for (ServerPlayerEntity player : serverWorld.getPlayers()) {
                            ServerPlayNetworking.send(player, ModMessages.TESLA_SLASH_ID, packet);
                        }
                    }


                    setVelocity(AirSwingItem.rayZVector(getYaw(), getPitch()).multiply(SPEED));

                } catch (NullPointerException e) {
                    kill();
                }
            }

            lifeTicks++;

            if (lifeTicks > maxLifeTicks) {
                kill();
            }
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

        return super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        lifeTicks = nbt.getInt("life_ticks");

        super.readNbt(nbt);
    }
}
