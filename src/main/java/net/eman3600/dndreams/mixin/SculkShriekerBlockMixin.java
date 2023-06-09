package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.cardinal_components.TormentComponent;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.mixin_interfaces.SculkShriekerBlockEntityAccess;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.SculkShriekerBlock;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SculkShriekerBlock.class)
public abstract class SculkShriekerBlockMixin extends BlockWithEntity implements Waterloggable {
    @Shadow @Final public static BooleanProperty CAN_SUMMON;

    protected SculkShriekerBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "getPlacementState", at = @At("RETURN"), cancellable = true)
    private void dndreams$getPlacementState(ItemPlacementContext ctx, CallbackInfoReturnable<BlockState> cir) {
        if (ctx.getWorld() instanceof ServerWorld world) {
            cir.setReturnValue(cir.getReturnValue().with(CAN_SUMMON, TormentComponent.shouldShroud(world, ctx.getBlockPos())));
        }
    }

    @Inject(method = "onSteppedOn", at = @At("HEAD"), cancellable = true)
    private void dndreams$onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity, CallbackInfo ci) {
        if (world instanceof ServerWorld serverWorld) {

            Entity trigger = SculkShriekerBlockEntityAccess.findResponsibleEntity(entity);
            if (trigger != null && EntityComponents.WARDEN.isProvidedBy(trigger)) {

                serverWorld.getBlockEntity(pos, BlockEntityType.SCULK_SHRIEKER).ifPresent(shrieker -> ((SculkShriekerBlockEntityAccess)shrieker).shriekAtAnyone(serverWorld, trigger));
                super.onSteppedOn(world, pos, state, entity);
                ci.cancel();
            }
        }
    }
}
