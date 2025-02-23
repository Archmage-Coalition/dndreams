package net.eman3600.dndreams.entities.ai.shambler;

import net.eman3600.dndreams.entities.mobs.ShamblerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.player.PlayerEntity;

import java.util.EnumSet;
import java.util.List;

public class HiveRageGoal extends TrackTargetGoal {

    protected final ShamblerEntity shambler;
    protected LivingEntity targetEntity;

    public HiveRageGoal(ShamblerEntity mob, boolean checkVisibility, boolean checkNavigable) {
        super(mob, checkVisibility, checkNavigable);
        this.shambler = mob;
        this.setControls(EnumSet.of(Goal.Control.TARGET));
    }

    @Override
    public boolean canStart() {
        if (this.mob.getTarget() != null) return false;
        this.findSharedTarget();
        return this.targetEntity != null;
    }

    @Override
    public void start() {
        this.mob.setTarget(this.targetEntity);
        super.start();
    }

    protected void findSharedTarget() {
        List<ShamblerEntity> hive = shambler.hiveGoal.getNearbyShamblers();

        for (ShamblerEntity entity : hive) {
            if (entity.getTarget() != null && !(entity.getTarget() instanceof ShamblerEntity) && (!(entity.getTarget() instanceof PlayerEntity player) || (!player.isCreative() && !player.isSpectator()))) {
                this.targetEntity = entity.getTarget();
                return;
            }
        }
    }
}
