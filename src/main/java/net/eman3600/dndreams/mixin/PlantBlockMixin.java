package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.initializers.basics.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PlantBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlantBlock.class)
public abstract class PlantBlockMixin extends Block {
    public PlantBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "canPlantOnTop", at = @At("HEAD"), cancellable = true)
    private void dndreams$canPlantOnTop(BlockState floor, BlockView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (floor.isOf(ModBlocks.SHADE_MOSS)) {
            cir.setReturnValue(true);
        }
    }
}
