package net.eman3600.dndreams.entities.ai.faceless;

import net.eman3600.dndreams.entities.mobs.FacelessEntity;
import net.minecraft.entity.ai.goal.Goal;

public class FacelessStalkingGoal extends Goal {

    private final FacelessEntity entity;

    public FacelessStalkingGoal(FacelessEntity entity) {
        this.entity = entity;
    }

    @Override
    public boolean canStart() {

        return entity.getVictim() != null && !entity.isCorporeal();
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
    }
}
