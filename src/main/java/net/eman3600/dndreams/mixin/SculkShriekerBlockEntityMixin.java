package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.mixin_interfaces.SculkShriekerBlockEntityAccess;
import net.eman3600.dndreams.mixin_interfaces.WardenEntityAccess;
import net.minecraft.block.BlockState;
import net.minecraft.block.SculkShriekerBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.SculkShriekerBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.listener.GameEventListener;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SculkShriekerBlockEntity.class)
public abstract class SculkShriekerBlockEntityMixin extends BlockEntity implements SculkShriekerBlockEntityAccess {
    @Shadow private int warningLevel;

    @Shadow protected abstract void shriek(ServerWorld world, @Nullable Entity entity);

    @Shadow protected abstract boolean canWarn(ServerWorld world);

    public SculkShriekerBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Inject(method = "shriek(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/Entity;)V", at = @At("TAIL"))
    private void dndreams$shriek(ServerWorld world, Entity entity, CallbackInfo ci) {
        if (canWarn(world)) {
            WardenEntityAccess.hauntClosePlayers(world, Vec3d.ofCenter(this.pos), entity, 80, 40);

            EntityComponents.WARDEN.maybeGet(entity).ifPresent(component -> {
                component.alertShrieker(world, getPos());
            });
        }
    }

    @Inject(method = "accepts", at = @At("HEAD"), cancellable = true)
    private void dndreams$accepts(ServerWorld world, GameEventListener listener, BlockPos pos, GameEvent event, GameEvent.Emitter emitter, CallbackInfoReturnable<Boolean> cir) {
        if (!this.isRemoved() && !this.getCachedState().get(SculkShriekerBlock.SHRIEKING) && SculkShriekerBlockEntityAccess.findResponsibleEntity(emitter.sourceEntity()) != null) cir.setReturnValue(true);
    }

    @Inject(method = "accept", at = @At("HEAD"), cancellable = true)
    private void dndreams$accept(ServerWorld world, GameEventListener listener, BlockPos pos, GameEvent event, Entity entity, Entity sourceEntity, float distance, CallbackInfo ci) {
        Entity trigger = SculkShriekerBlockEntityAccess.findResponsibleEntity(sourceEntity != null ? sourceEntity : entity);

        if (trigger != null && EntityComponents.WARDEN.isProvidedBy(trigger)) {
            shriekAtAnyone(world, trigger);
            ci.cancel();
        }
    }

    @Override
    public void shriekAtAnyone(ServerWorld world, Entity entity) {
        BlockState blockState = this.getCachedState();
        if (blockState.get(SculkShriekerBlock.SHRIEKING)) {
            return;
        }
        this.warningLevel = 0;
        this.shriek(world, entity);
    }

    @Inject(method = "warn", at = @At("HEAD"), cancellable = true)
    private void dndreams$warn$remove(ServerWorld world, CallbackInfo ci) {
        ci.cancel();
    }
}
