package net.eman3600.dndreams.entities.states;

import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.injection.selectors.TargetSelector;

public interface MobState<T extends MobEntity> {

    void onStart(GoalSelector goalSelector, GoalSelector targetSelector);
    void onEnd(GoalSelector goalSelector, GoalSelector targetSelector);

    T getMob();
    String getName();

    default void tick() {}
}
