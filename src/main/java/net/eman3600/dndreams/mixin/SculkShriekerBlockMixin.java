package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.cardinal_components.TormentComponent;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.SculkShriekerBlock;
import net.minecraft.block.Waterloggable;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.BooleanProperty;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
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


}
