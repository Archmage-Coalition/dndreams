package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.util.ModTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChorusPlantBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChorusPlantBlock.class)
public abstract class ChorusPlantBlockMixin extends Block {
    public ChorusPlantBlockMixin(Settings settings) {
        super(settings);
    }

    @Redirect(method = "canPlaceAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"))
    private boolean dndreams$canPlaceAt(BlockState instance, Block block) {
        if (block == Blocks.END_STONE) {
            return instance.isOf(block) || instance.isIn(ModTags.END_STONES);
        } else {
            return instance.isOf(block);
        }
    }

    @Redirect(method = "getStateForNeighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"))
    private boolean dndreams$getStateForNeighborUpdate(BlockState instance, Block block) {
        if (block == Blocks.END_STONE) {
            return instance.isOf(block) || instance.isIn(ModTags.END_STONES);
        } else {
            return instance.isOf(block);
        }
    }

    @Redirect(method = "withConnectionProperties", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"))
    private boolean dndreams$withConnectionProperties(BlockState instance, Block block) {
        if (block == Blocks.END_STONE) {
            return instance.isOf(block) || instance.isIn(ModTags.END_STONES);
        } else {
            return instance.isOf(block);
        }
    }
}
