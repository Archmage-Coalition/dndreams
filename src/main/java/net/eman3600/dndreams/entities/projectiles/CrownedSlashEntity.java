package net.eman3600.dndreams.entities.projectiles;

import net.eman3600.dndreams.initializers.ModEntities;
import net.eman3600.dndreams.initializers.ModMessages;
import net.eman3600.dndreams.items.interfaces.AirSwingItem;
import net.eman3600.dndreams.items.magic_sword.CrownedEdgeItem;
import net.eman3600.dndreams.util.matrices.RotatedBox;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
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
    private List<LivingEntity> victims = new ArrayList<>();
    private static final float yawPerTick = 2;
    private static final float changeInYaw = 120;
    private static final int moveTicksPerTick = 12;
    private static final float range = 3f;
    private int lifeTicks = 0;


    public CrownedSlashEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public CrownedSlashEntity(double x, double y, double z, World world) {
        super(ModEntities.CROWNED_SLASH_ENTITY_TYPE, x, y, z, world);
    }

    public CrownedSlashEntity(LivingEntity owner, World world) {
        super(ModEntities.CROWNED_SLASH_ENTITY_TYPE, owner, world);
    }

    @Override
    public ItemStack asItemStack() {
        return null;
    }

    public void initFromStack(ItemStack stack) {
        if (stack.getItem() instanceof CrownedEdgeItem item) {
            this.setDamage(item.magicDamage());
        } else {
            setDamage(1);
        }

        origin = getPos().subtract(0, 1d, 0);

        setYaw(getOwner().getYaw() + changeInYaw/2);

        Vec3d vec = AirSwingItem.rayZVector(this.getYaw(), this.getPitch());
        Vec3d side = AirSwingItem.rayXVector(this.getYaw(), this.getPitch());

        setBoundingBox(new RotatedBox(origin.subtract(vec).subtract(side).subtract(0, 0.5d, 0), origin.add(vec).add(side).add(0, 0.5d, 0)));

        setPierceLevel((byte) 3);
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    public void baseTick() {
        super.baseTick();
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

                                livingEntity.timeUntilRegen = 1;

                                livingEntity.takeKnockback(0.4f, MathHelper.sin(getOwner().getYaw() * ((float) Math.PI / 180)), -MathHelper.cos(getOwner().getYaw() * ((float) Math.PI / 180)));
                                livingEntity.damage(DamageSource.magic(getOwner(), getOwner()), (float) this.getDamage());
                            }
                        }

                    }

                    PacketByteBuf packet = PacketByteBufs.create();

                    packet.writeDouble(getX());
                    packet.writeDouble(getY());
                    packet.writeDouble(getZ());

                    for (ServerPlayerEntity player : serverWorld.getPlayers()) {
                        ServerPlayNetworking.send(player, ModMessages.CROWNED_SLASH_ID, packet);
                    }

                    updatePositionAndAngles(getX(), getY(), getZ(), getYaw(), getPitch());

                }/* else if (world.isClient && !doNotDisplay && !world.getNonSpectatingEntities(PlayerEntity.class, getBoundingBox()).contains(getOwner())) {

                    Vec3d supposedPos = origin.add(AirSwingItem.rayZVector(getYaw() - yawPerTick * (l + 1), getPitch()).multiply(range));
                    if (lifeTicks + l > changeInYaw / yawPerTick) continue;


//                        for (double i = box.minX; i <= box.maxX; i += 0.25d) {
//                            for (double j = box.minZ; j <= box.maxZ; j += 0.25d) {
//                                this.world.addParticle(ModParticles.CROWNED_SLASH_PARTICLE, i, center.y, j, 0, 0, 0);
//                            }
//                        }

                    this.world.addParticle(ModParticles.CROWNED_SLASH_PARTICLE, supposedPos.x, supposedPos.y, supposedPos.z, 0, 0, 0);

                } else if (world.isClient) {
                    origin = getPos().subtract(0, 1d, 0);

                    doNotDisplay = false;
                }*/
            }

            if (lifeTicks > changeInYaw / yawPerTick && world instanceof ServerWorld) {
                kill();
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
    protected void updateRotation() {

    }
}
