package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.util.ModRegistries;
import net.minecraft.block.*;
import net.minecraft.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(LichenGrower.LichenGrowChecker.class)
public abstract class LichenGrowCheckerMixin implements LichenGrower.GrowChecker {

    @Shadow protected MultifaceGrowthBlock lichen;

    @Inject(method = "canGrow(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;Lnet/minecraft/block/BlockState;)Z", cancellable = true, at = @At("HEAD"))
    private void dndreams$canGrow(BlockView world, BlockPos pos, BlockPos growPos, Direction direction, BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (lichen instanceof SculkVeinBlock) {
            List<TagKey<Block>> tags = state.streamTags().toList();

            for (TagKey<Block> tag: tags) {
                if (ModRegistries.SCULK_TRANSFORM.containsKey(tag)) {
                    cir.setReturnValue(false);
                    return;
                }
            }
        }
    }
}
