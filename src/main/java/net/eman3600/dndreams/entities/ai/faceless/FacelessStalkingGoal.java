package net.eman3600.dndreams.entities.ai.faceless;

import net.eman3600.dndreams.entities.mobs.FacelessEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.Goal;

public class FacelessStalkingGoal extends Goal {

    private final FacelessEntity entity;
    private int timeSinceTele = 0;

    public FacelessStalkingGoal(FacelessEntity entity) {
        this.entity = entity;
    }

    @Override
    public boolean canStart() {

        return entity.getVictim() != null/* && !entity.isCorporeal()*/;
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        Entity victim = entity.getVictim();
        if (timeSinceTele % 20 == 0 && entity.squaredDistanceTo(victim) > 400) {
            entity.teleportTo(victim);
            timeSinceTele = 0;
        } else if (timeSinceTele > 120) {
            boolean bl = entity.teleportRandomly();
            if (bl) timeSinceTele = 0; else timeSinceTele -= 30;
        } else ++timeSinceTele;
    }
}
