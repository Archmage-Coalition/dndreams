package net.eman3600.dndreams.entities.ai.faceless;

import net.eman3600.dndreams.entities.mobs.FacelessEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

public class LookAtVictimGoal extends Goal {

    private final FacelessEntity entity;
    private final boolean mustBeUnseen;
    private Entity target;

    public LookAtVictimGoal(FacelessEntity entity, boolean mustBeUnseen) {
        this.entity = entity;
        this.mustBeUnseen = mustBeUnseen;
        this.setControls(EnumSet.of(Goal.Control.LOOK));
    }


    @Override
    public boolean canStart() {
        this.target = entity.getVictim();
        return target != null && !(mustBeUnseen && entity.isSeen());
    }

    @Override
    public boolean shouldContinue() {
        return target != null && target.isAlive() && !(mustBeUnseen && entity.isSeen());
    }

    @Override
    public void stop() {
        this.target = null;
    }

    @Override
    public void tick() {
        if (!this.target.isAlive()) {
            return;
        }
        double d = this.target.getEyeY();
        this.entity.getLookControl().lookAt(this.target.getX(), d, this.target.getZ());
    }
}
