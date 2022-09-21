package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.initializers.WorldComponents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndermanEntity.class)
public abstract class EndermanEntityMixin extends HostileEntity implements Angerable {

    protected EndermanEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "isPlayerStaring", at = @At("RETURN"), cancellable = true)
    private void playerStareStopper(PlayerEntity player, CallbackInfoReturnable<Boolean> info) {
        if (world.getScoreboard() != null && WorldComponents.BOSS_STATE.get(world.getScoreboard()).dragonSlain()) {
            info.setReturnValue(false);
        }
    }
}
