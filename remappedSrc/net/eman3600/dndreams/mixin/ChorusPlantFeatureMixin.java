package net.eman3600.dndreams.mixin;

import com.mojang.serialization.Codec;
import net.eman3600.dndreams.util.ModTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.feature.ChorusPlantFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChorusPlantFeature.class)
public abstract class ChorusPlantFeatureMixin extends Feature<DefaultFeatureConfig> {
    public ChorusPlantFeatureMixin(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Redirect(method = "generate", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"))
    private boolean dndreams$randomTick(BlockState instance, Block block) {
        if (block == Blocks.END_STONE) {
            return instance.isOf(block) || instance.isIn(ModTags.END_STONES);
        } else {
            return instance.isOf(block);
        }
    }
}
