package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.util.ModTags;
import net.minecraft.block.Block;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FarmlandBlock.class)
public abstract class FarmlandBlockMixin extends Block {
    public FarmlandBlockMixin(Settings settings) {
        super(settings);
    }

    @Redirect(method = "isWaterNearby", at = @At(value = "INVOKE", target = "Lnet/minecraft/fluid/FluidState;isIn(Lnet/minecraft/tag/TagKey;)Z"))
    private static boolean dndreams$isWaterNearby(FluidState instance, TagKey<Fluid> tag) {
        return instance.isIn(tag) || instance.isIn(ModTags.FLOWING_SPIRIT);
    }
}
