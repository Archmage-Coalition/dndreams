package net.eman3600.dndreams.entities.states.faceless;

import net.eman3600.dndreams.entities.ai.faceless.LookAtVictimGoal;
import net.eman3600.dndreams.entities.mobs.FacelessEntity;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;

public class FacelessObservationState implements FacelessState {

    private final FacelessEntity entity;

    public FacelessObservationState(FacelessEntity entity) {
        this.entity = entity;
    }


    @Override
    public void onStart(GoalSelector goalSelector, GoalSelector targetSelector) {
        goalSelector.add(8, new LookAtVictimGoal(this.entity, false));
    }

    @Override
    public void onEnd(GoalSelector goalSelector, GoalSelector targetSelector) {
        goalSelector.clear();
    }

    @Override
    public void onSeen() {
        entity.resetStateTicks();
    }

    @Override
    public FacelessEntity getMob() {
        return entity;
    }

    @Override
    public String getName() {
        return "observation";
    }

    @Override
    public boolean canView(PlayerEntity player) {
        return entity.isVictim(player);
    }

    @Override
    public boolean shouldVanish() {
        return false;
    }

    @Override
    public float renderedOpacity(PlayerEntity player) {
        return 0.9f;
    }

    @Override
    public float renderedClarity(PlayerEntity player) {
        return 0.1f;
    }

    @Override
    @Nullable
    public String getNextState() {
        return getSanity() >= 5 ? null : "engagement";
    }
}
