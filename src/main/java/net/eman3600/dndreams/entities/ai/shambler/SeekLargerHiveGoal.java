package net.eman3600.dndreams.entities.ai.shambler;

import net.eman3600.dndreams.entities.mobs.ShamblerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.Path;

import java.util.EnumSet;

public class SeekLargerHiveGoal extends Goal {

    protected final ShamblerEntity mob;
    private final double speed;

    private Path path;
    private double targetX;
    private double targetY;
    private double targetZ;
    private int updateCountdownTicks;
    private int cooldown;

    private long lastUpdateTime;

    public SeekLargerHiveGoal(ShamblerEntity mob, double speed) {
        this.mob = mob;
        this.speed = speed;

        this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
    }

    @Override
    public boolean canStart() {
        long l = this.mob.world.getTime();
        if (l - this.lastUpdateTime < 20L) {
            return false;
        }
        this.lastUpdateTime = l;
        LivingEntity livingEntity = this.mob.getTarget();
        if (!(livingEntity instanceof ShamblerEntity)) {
            return false;
        }
        if (!livingEntity.isAlive()) {
            return false;
        }
        this.path = this.mob.getNavigation().findPathTo(livingEntity, 0);
        return this.path != null;
    }

    @Override
    public boolean shouldContinue() {
        LivingEntity livingEntity = this.mob.getTarget();
        if (!(livingEntity instanceof ShamblerEntity)) {
            return false;
        }
        if (!livingEntity.isAlive()) {
            return false;
        }
        return this.mob.isInWalkTargetRange(livingEntity.getBlockPos());
    }

    @Override
    public void start() {
        this.mob.getNavigation().startMovingAlong(this.path, this.speed);
        this.updateCountdownTicks = 0;
        this.cooldown = 0;
    }

    @Override
    public void stop() {
        this.mob.setTarget(null);
        this.mob.getNavigation().stop();
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        LivingEntity livingEntity = this.mob.getTarget();
        if (!(livingEntity instanceof ShamblerEntity)) {
            return;
        }

        this.mob.getLookControl().lookAt(livingEntity, 30.0f, 30.0f);
        double d = this.mob.squaredDistanceTo(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
        if (d < 64) {
            this.mob.setTarget(null);
            return;
        }
        this.updateCountdownTicks = Math.max(this.updateCountdownTicks - 1, 0);
        if ((this.mob.getVisibilityCache().canSee(livingEntity)) && this.updateCountdownTicks <= 0 && (this.targetX == 0.0 && this.targetY == 0.0 && this.targetZ == 0.0 || livingEntity.squaredDistanceTo(this.targetX, this.targetY, this.targetZ) >= 1.0 || this.mob.getRandom().nextFloat() < 0.05f)) {
            this.targetX = livingEntity.getX();
            this.targetY = livingEntity.getY();
            this.targetZ = livingEntity.getZ();
            this.updateCountdownTicks = 4 + this.mob.getRandom().nextInt(7);
            if (d > 1024.0) {
                this.updateCountdownTicks += 10;
            } else if (d > 256.0) {
                this.updateCountdownTicks += 5;
            }
            if (!this.mob.getNavigation().startMovingTo(livingEntity, this.speed)) {
                this.updateCountdownTicks += 15;
            }
            this.updateCountdownTicks = this.getTickCount(this.updateCountdownTicks);
        }
        this.cooldown = Math.max(this.cooldown - 1, 0);
    }
}
