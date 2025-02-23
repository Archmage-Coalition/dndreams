package net.eman3600.dndreams.entities.ai.shambler;

import net.eman3600.dndreams.entities.mobs.ShamblerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;
import java.util.List;

public class ShamblerBombGoal extends Goal {

    protected final ShamblerEntity mob;
    private final double speed;
    protected CreeperEntity creeper;

    private Path path;
    private double targetX;
    private double targetY;
    private double targetZ;
    private int cooldown;
    private int bombCooldown;
    private int updateCountdownTicks;

    private static final int BOMB_COOLDOWN = 100;

    private long lastUpdateTime;

    public ShamblerBombGoal(ShamblerEntity mob, double speed) {
        this.mob = mob;
        this.speed = speed;
        this.bombCooldown = 0;

        this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
    }

    @Override
    public boolean canStart() {
        if (bombCooldown > 0) {
            bombCooldown--;
            return false;
        }
        long l = this.mob.world.getTime();
        if (l - this.lastUpdateTime < 40L) {
            return false;
        }
        this.lastUpdateTime = l;
        LivingEntity target = this.mob.getTarget();
        if (!(target instanceof PlayerEntity)) {
            return false;
        }
        if (!target.isAlive()) {
            return false;
        }

        if (this.mob.hiveGoal.getHiveSize() < 2) return false;

        List<ShamblerEntity> hive = this.mob.hiveGoal.getNearbyShamblers();

        for (ShamblerEntity entity : hive) {
            if (entity.bombGoal.creeper != null) {
                return false;
            }
        }

        CreeperEntity creeper = findClosestCreeper();
        if (creeper == null || !creeper.isAlive()) {
            return false;
        }

        this.path = this.mob.getNavigation().findPathTo(creeper, 0);
        if (this.path != null) {
            this.creeper = creeper;
            return true;
        }
        return false;
    }

    @Override
    public boolean shouldContinue() {
        if (creeper == null || !creeper.isAlive() || this.mob.getTarget() == null) {
            return false;
        }
        if (!(creeper.squaredDistanceTo(this.mob.getTarget()) > 7 && this.mob.squaredDistanceTo(this.mob.getTarget()) > 9)) {
            return false;
        }
        return this.mob.isInWalkTargetRange(creeper.getBlockPos());
    }

    @Override
    public void stop() {
        this.creeper = null;
        this.mob.getNavigation().stop();
    }

    @Override
    public void start() {
        this.mob.getNavigation().startMovingAlong(this.path, this.speed);
        this.updateCountdownTicks = 0;
        this.cooldown = 0;
    }

    @Override
    public void tick() {
        if (creeper == null || this.mob.getTarget() == null) {
            return;
        }

        this.mob.getLookControl().lookAt(creeper, 30.0f, 30.0f);
        double d = this.mob.squaredDistanceTo(creeper.getX(), creeper.getY(), creeper.getZ());
        if (d < getSquaredMaxAttackDistance(creeper)) {
            throwEntity(creeper, this.mob.getTarget());
            this.bombCooldown = BOMB_COOLDOWN;
            this.creeper = null;
            return;
        }
        this.updateCountdownTicks = Math.max(this.updateCountdownTicks - 1, 0);
        if ((this.mob.getVisibilityCache().canSee(creeper)) && this.updateCountdownTicks <= 0 && (this.targetX == 0.0 && this.targetY == 0.0 && this.targetZ == 0.0 || creeper.squaredDistanceTo(this.targetX, this.targetY, this.targetZ) >= 1.0 || this.mob.getRandom().nextFloat() < 0.05f)) {
            this.targetX = creeper.getX();
            this.targetY = creeper.getY();
            this.targetZ = creeper.getZ();
            this.updateCountdownTicks = 4 + this.mob.getRandom().nextInt(7);
            if (d > 1024.0) {
                this.updateCountdownTicks += 10;
            } else if (d > 256.0) {
                this.updateCountdownTicks += 5;
            }
            if (!this.mob.getNavigation().startMovingTo(creeper, this.speed)) {
                this.updateCountdownTicks += 15;
            }
            this.updateCountdownTicks = this.getTickCount(this.updateCountdownTicks);
        }
        this.cooldown = Math.max(this.cooldown - 1, 0);
    }

    /**
     * Throws a live entity at another live entity
     * @param entity the entity to be thrown
     * @param target the target to be thrown at
     */
    public static void throwEntity(LivingEntity entity, LivingEntity target) {
        Vec3d lineTo = target.getPos().subtract(entity.getPos());

        double velY = .8f;
        double timeToLand = 10 + (MathHelper.sqrt(.64f - (float)(.16f * MathHelper.clamp(lineTo.getY(), -10, 4))) * 12.5);

        System.out.println("Time to Land: " + timeToLand);

        Vec3d horizontalLine = lineTo.subtract(0, lineTo.y, 0);
        double velH = MathHelper.clamp((horizontalLine.length() * (-.09431068)) / (Math.pow(.91, timeToLand) - 1), 0, 4d);

        System.out.println("Vel H: " + velH);

        Vec3d vel = horizontalLine.normalize().multiply(velH);
        entity.setVelocity(vel.x, velY, vel.z);
        entity.velocityModified = true;
        entity.velocityDirty = true;
    }

    protected CreeperEntity findClosestCreeper() {

        return this.mob.world.getClosestEntity(this.mob.world.getEntitiesByClass(CreeperEntity.class, this.getSearchBox(14d), creeper -> (creeper.isOnGround() && creeper.squaredDistanceTo(this.mob.getTarget()) > 10 && this.mob.squaredDistanceTo(creeper) < this.mob.squaredDistanceTo(this.mob.getTarget()))), TargetPredicate.DEFAULT, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
    }

    protected Box getSearchBox(double distance) {
        return this.mob.getBoundingBox().expand(distance, 4.0, distance);
    }

    protected double getSquaredMaxAttackDistance(LivingEntity entity) {
        return this.mob.getWidth() * 2.0f * (this.mob.getWidth() * 2.0f) + entity.getWidth();
    }
}
