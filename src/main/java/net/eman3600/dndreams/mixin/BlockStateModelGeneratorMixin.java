package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.initializers.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.client.BlockStateModelGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockStateModelGenerator.class)
public abstract class BlockStateModelGeneratorMixin {
    @Shadow public abstract BlockStateModelGenerator.BuiltinModelPool registerBuiltin(Block block, Block particleBlock);

    @Inject(method = "register", at = @At("HEAD"))
    private void dndreams$register(CallbackInfo ci) {
        this.registerBuiltin(ModBlocks.COSMIC_PORTAL, Blocks.OBSIDIAN);
    }
}
