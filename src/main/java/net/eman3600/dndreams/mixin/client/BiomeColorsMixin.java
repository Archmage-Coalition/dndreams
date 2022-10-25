package net.eman3600.dndreams.mixin.client;

import net.eman3600.dndreams.initializers.ModDimensions;
import net.eman3600.dndreams.mixin_interfaces.ChunkRendererRegionAccess;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.World;
import net.minecraft.world.level.ColorResolver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(BiomeColors.class)
public abstract class BiomeColorsMixin {
    @Unique
    private static int DREAM_COLOR = 0x009999;

    @Inject(method = "getColor", at = @At("HEAD"), cancellable = true)
    private static void dndreams$getColor(BlockRenderView world, BlockPos pos, ColorResolver resolver, CallbackInfoReturnable<Integer> cir) {
        if (isDream(world)) {
            cir.setReturnValue(DREAM_COLOR);
        }
    }

    @Inject(method = "getGrassColor", at = @At("HEAD"), cancellable = true)
    private static void dndreams$getGrassColor(BlockRenderView world, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        if (isDream(world)) {
            cir.setReturnValue(DREAM_COLOR);
        }
    }

    @Inject(method = "getFoliageColor", at = @At("HEAD"), cancellable = true)
    private static void dndreams$getFoliageColor(BlockRenderView world, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        if (isDream(world)) {
            cir.setReturnValue(DREAM_COLOR);
        }
    }

    @Unique
    private static boolean isDream(BlockRenderView view) {
        try {
            return view instanceof ChunkRendererRegionAccess access && access.getWorld().getRegistryKey() == ModDimensions.DREAM_DIMENSION_KEY;
        } catch (NullPointerException e) {
            return false;
        }
    }
}
