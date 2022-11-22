package net.eman3600.dndreams.mixin.client;

import net.eman3600.dndreams.ClientInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.resource.SynchronousResourceReloader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public abstract class GameRendererMixin implements SynchronousResourceReloader, AutoCloseable {

    @Shadow @Final private MinecraftClient client;

    @Inject(method = "getSkyDarkness", at = @At("HEAD"), cancellable = true)
    private void dndreams$getSkyDarkness(float tickDelta, CallbackInfoReturnable<Float> cir) {
        if (ClientInitializer.drawAether(client.world)) {
            cir.setReturnValue(1f);
        }
    }
}
