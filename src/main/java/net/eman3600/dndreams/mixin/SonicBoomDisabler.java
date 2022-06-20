package net.eman3600.dndreams.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.SonicBoomTask;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SonicBoomTask.class)
public abstract class SonicBoomDisabler {
    public boolean shouldRun(ServerWorld serverWorld, WardenEntity wardenEntity) {
        return false;
    }
}
