package net.eman3600.dndreams.entities.states.faceless;

import net.eman3600.dndreams.entities.mobs.FacelessEntity;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.injection.selectors.TargetSelector;

public class FacelessObservationState implements FacelessState {

    private final FacelessEntity faceless;
    private int waitTicks = 0;
    private boolean seen = false;

    public FacelessObservationState(FacelessEntity faceless) {
        this.faceless = faceless;
    }


    @Override
    public void onStart(GoalSelector goalSelector, GoalSelector targetSelector) {

    }

    @Override
    public void onEnd(GoalSelector goalSelector, GoalSelector targetSelector) {

    }

    @Override
    public FacelessEntity getMob() {
        return faceless;
    }

    @Override
    public String getName() {
        return "observation";
    }

    @Override
    public boolean canView(PlayerEntity player) {
        return player == faceless.getVictim();
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
    public void writeNbt(NbtCompound nbt) {
        nbt.putInt("WaitTicks", waitTicks);
        nbt.putBoolean("Seen", seen);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        this.waitTicks = nbt.getInt("WaitTicks");
        this.seen = nbt.getBoolean("Seen");
    }
}
