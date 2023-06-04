package net.eman3600.dndreams.entities.ai;

import net.eman3600.dndreams.items.magic_bow.MagicBowItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

import java.util.EnumSet;

public class MagicBowAttackGoal<T extends HostileEntity>
        extends Goal {
    private final T actor;
    private final double speed;
    private int attackInterval;
    private final float squaredRange;
    private int cooldown = -1;
    private int targetSeeingTicker;
    private boolean movingToLeft;
    private boolean backward;
    private int combatTicks = -1;
    public final Item bowItem;

    public MagicBowAttackGoal(T actor, double speed, int attackInterval, float range, Item bowItem) {
        this.actor = actor;
        this.speed = speed;
        this.attackInterval = attackInterval;
        this.squaredRange = range * range;
        this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
        this.bowItem = bowItem;

        if (!(bowItem instanceof MagicBowItem)) {
            throw new IllegalArgumentException("bowItem is not a MagicBowItem!");
        }
    }

    public void setAttackInterval(int attackInterval) {
        this.attackInterval = attackInterval;
    }

    @Override
    public boolean canStart() {
        if (this.actor.getTarget() == null) {
            return false;
        }
        return this.isHoldingBow();
    }

    public void setRanged(boolean ranged) {
        if (ranged) {
            actor.setStackInHand(Hand.MAIN_HAND, new ItemStack(bowItem));
        } else {
            actor.setStackInHand(ProjectileUtil.getHandPossiblyHolding(actor, bowItem), ItemStack.EMPTY);
        }
    }

    protected boolean isHoldingBow() {
        return this.actor.isHolding(bowItem);
    }

    @Override
    public boolean shouldContinue() {
        return (this.canStart() || !this.actor.getNavigation().isIdle()) && this.isHoldingBow();
    }

    @Override
    public void start() {
        super.start();
        this.actor.setAttacking(true);
    }

    @Override
    public void stop() {
        super.stop();
        this.actor.setAttacking(false);
        this.targetSeeingTicker = 0;
        this.cooldown = -1;
        this.actor.clearActiveItem();
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        boolean bl2;
        LivingEntity livingEntity = this.actor.getTarget();
        if (livingEntity == null) {
            return;
        }
        double d = this.actor.squaredDistanceTo(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
        boolean bl = this.actor.getVisibilityCache().canSee(livingEntity);
        boolean bl3 = bl2 = this.targetSeeingTicker > 0;
        if (bl != bl2) {
            this.targetSeeingTicker = 0;
        }
        if (bl) {
            ++this.targetSeeingTicker;
        } else {
            --this.targetSeeingTicker;
        }
        if (d > (double)this.squaredRange || this.targetSeeingTicker < 20) {
            this.actor.getNavigation().startMovingTo(livingEntity, this.speed);
            this.combatTicks = -1;
        } else {
            this.actor.getNavigation().stop();
            ++this.combatTicks;
        }
        if (this.combatTicks >= 20) {
            if ((double) this.actor.getRandom().nextFloat() < 0.3) {
                boolean bl4 = this.movingToLeft = !this.movingToLeft;
            }
            if ((double) this.actor.getRandom().nextFloat() < 0.3) {
                this.backward = !this.backward;
            }
            this.combatTicks = 0;
        }
        if (this.combatTicks > -1) {
            if (d > (double)(this.squaredRange * 0.75f)) {
                this.backward = false;
            } else if (d < (double)(this.squaredRange * 0.25f)) {
                this.backward = true;
            }
            this.actor.getMoveControl().strafeTo(this.backward ? -0.5f : 0.5f, this.movingToLeft ? 0.5f : -0.5f);
            this.actor.lookAtEntity(livingEntity, 30.0f, 30.0f);
        } else {
            this.actor.getLookControl().lookAt(livingEntity, 30.0f, 30.0f);
        }
        if (this.actor.isUsingItem()) {
            int i;
            if (!bl && this.targetSeeingTicker < -60) {
                this.actor.clearActiveItem();
            } else if (bl && (i = this.actor.getItemUseTime()) >= ((MagicBowItem)bowItem).pullTime()) {
                this.actor.clearActiveItem();
                ((RangedAttackMob)this.actor).attack(livingEntity, ((MagicBowItem)bowItem).getBowPullProgress(i));
                this.cooldown = this.attackInterval;
            }
        } else if (--this.cooldown <= 0 && this.targetSeeingTicker >= -60) {
            this.actor.setCurrentHand(ProjectileUtil.getHandPossiblyHolding(this.actor, bowItem));
        }
    }
}