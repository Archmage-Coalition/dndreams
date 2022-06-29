package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.initializers.WorldComponents;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderDragonFight.class)
public abstract class DragonFightMixin {
    @Shadow
    @Final
    private ServerWorld world;

    @Inject(method = "dragonKilled", at = @At("HEAD"))
    public void killComponent(EnderDragonEntity dragon, CallbackInfo info) {
        WorldComponents.BOSS_STATE.get(world.getScoreboard()).flagDragonSlain(true);
    }

    @Inject(method = "createDragon", at = @At("HEAD"))
    public void reviveComponent(CallbackInfoReturnable info) {
        WorldComponents.BOSS_STATE.get(world.getScoreboard()).flagDragonSlain(false);
    }
}
