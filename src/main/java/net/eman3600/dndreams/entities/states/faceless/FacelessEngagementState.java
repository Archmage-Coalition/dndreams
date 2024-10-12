package net.eman3600.dndreams.entities.states.faceless;

import net.eman3600.dndreams.entities.ai.faceless.LookAtVictimGoal;
import net.eman3600.dndreams.entities.mobs.FacelessEntity;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;

public class FacelessEngagementState implements FacelessState {

    private final FacelessEntity entity;

    public FacelessEngagementState(FacelessEntity entity) {
        this.entity = entity;
    }


    @Override
    public void onStart(GoalSelector goalSelector, GoalSelector targetSelector) {
        goalSelector.add(8, new LookAtVictimGoal(this.entity, true));
    }

    @Override
    public void onEnd(GoalSelector goalSelector, GoalSelector targetSelector) {
        goalSelector.clear();
    }

    @Override
    public FacelessEntity getMob() {
        return entity;
    }

    @Override
    public String getName() {
        return "engagement";
    }

    @Override
    public boolean canView(PlayerEntity player) {
        return true;
    }

    @Override
    public boolean shouldVanish() {
        return getSanity() >= 5f;
    }

    @Override
    public float renderedOpacity(PlayerEntity player) {
        return 0.9f;
    }

    @Override
    public float renderedClarity(PlayerEntity player) {
        return 1f;
    }

    @Override
    @Nullable
    public String getNextState() {
        return null;
    }
}
