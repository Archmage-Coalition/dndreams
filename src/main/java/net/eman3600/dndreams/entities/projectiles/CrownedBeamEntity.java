package net.eman3600.dndreams.entities.projectiles;

import net.eman3600.dndreams.initializers.ModEnchantments;
import net.eman3600.dndreams.initializers.ModEntities;
import net.eman3600.dndreams.initializers.ModMessages;
import net.eman3600.dndreams.items.interfaces.AirSwingItem;
import net.eman3600.dndreams.items.interfaces.MagicDamageItem;
import net.eman3600.dndreams.items.magic_sword.CrownedEdgeItem;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
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

public class CrownedBeamEntity extends PersistentProjectileEntity implements ProjectileOverhaulEntity {
    private int lifeTicks = 0;
    private static final int maxLifeTicks = 20;
    private static final double SPEED = 1.0d;
    public List<LivingEntity> victims = new ArrayList<>();
    public CrownedSlashEntity slash = null;
    private Vec3d origin;
    private boolean wicked = false;

    public CrownedBeamEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public CrownedBeamEntity(double x, double y, double z, World world) {
        super(ModEntities.CROWNED_BEAM_ENTITY_TYPE, x, y, z, world);
    }

    public CrownedBeamEntity(LivingEntity owner, World world) {
        super(ModEntities.CROWNED_BEAM_ENTITY_TYPE, owner, world);
    }

    @Override
    protected ItemStack asItemStack() {
        return null;
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    public void initFromStack(ItemStack stack) {
        if (stack.getItem() instanceof MagicDamageItem item) {
            this.setDamage(item.getMagicDamage(stack));
        } else {
            setDamage(1);
        }

        setPos(getX(), getY() - 1, getZ());
        origin = getPos();

        setYaw(getOwner().getYaw());

        setVelocity(0, 0, 0);

        wicked = EnchantmentHelper.getLevel(ModEnchantments.WICKED, stack) > 0;

        setPierceLevel((byte) 3);
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
                                    if (slash != null) {
                                        slash.victims.add(livingEntity);
                                    }

                                    livingEntity.timeUntilRegen = 1;

                                    livingEntity.takeKnockback(0.4f, MathHelper.sin(getOwner().getYaw() * ((float) Math.PI / 180)), -MathHelper.cos(getOwner().getYaw() * ((float) Math.PI / 180)));
                                    livingEntity.damage(DamageSource.magic(getOwner(), getOwner()), (float) this.getDamage());
                                    if (wicked) livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 80, 1), getOwner());
                                }
                            }

                        }

                        Vec3d edge = focus.add(curve.multiply(range));

                        PacketByteBuf packet = PacketByteBufs.create();

                        packet.writeDouble(edge.x);
                        packet.writeDouble(edge.y);
                        packet.writeDouble(edge.z);
                        packet.writeBoolean(wicked);

                        for (ServerPlayerEntity player : serverWorld.getPlayers()) {
                            ServerPlayNetworking.send(player, ModMessages.CROWNED_BEAM_ID, packet);
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
}
