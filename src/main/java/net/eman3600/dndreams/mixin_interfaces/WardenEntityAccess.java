package net.eman3600.dndreams.mixin_interfaces;

import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public interface WardenEntityAccess {


    static void hauntClosePlayers(ServerWorld world, Vec3d pos, @Nullable Entity entity, int duration, int range) {
        StatusEffectInstance statusEffectInstance = new StatusEffectInstance(ModStatusEffects.HAUNTED, duration, 0, false, false);
        StatusEffectUtil.addEffectToPlayersWithinDistance(world, entity, pos, range, statusEffectInstance, Math.max(1, duration - 20));
    }
}
