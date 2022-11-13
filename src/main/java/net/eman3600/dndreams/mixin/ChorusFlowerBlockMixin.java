package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.util.ModTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChorusFlowerBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChorusFlowerBlock.class)
public abstract class ChorusFlowerBlockMixin extends Block {
    public ChorusFlowerBlockMixin(Settings settings) {
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

    @Redirect(method = "randomTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"))
    private boolean dndreams$randomTick(BlockState instance, Block block) {
        if (block == Blocks.END_STONE) {
            return instance.isOf(block) || instance.isIn(ModTags.END_STONES);
        } else {
            return instance.isOf(block);
        }
    }
}
