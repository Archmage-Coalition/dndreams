package net.eman3600.dndreams.mixin.client;

import net.eman3600.dndreams.ClientInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
@Mixin(BackgroundRenderer.class)
public abstract class BackgroundRendererMixin {

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getSkyColor(Lnet/minecraft/util/math/Vec3d;F)Lnet/minecraft/util/math/Vec3d;"))
    private static Vec3d dndreams$render$getSkyColor(ClientWorld instance, Vec3d cameraPos, float tickDelta) {

        if (ClientInitializer.drawAether(instance)) {
            return new Vec3d(0,0,0);
        }

        return instance.getSkyColor(cameraPos, tickDelta);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getSkyAngle(F)F"))
    private static float dndreams$render$getSkyAngle(ClientWorld instance, float v) {

        if (ClientInitializer.drawAether(instance)) {
            return .5f;
        }

        return instance.getSkyAngle(v);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getSkyAngleRadians(F)F"))
    private static float dndreams$render$getSkyAngleRadians(ClientWorld instance, float v) {

        if (ClientInitializer.drawAether(instance)) {
            return instance.getSkyAngleRadians(18000);
        }

        return instance.getSkyAngleRadians(v);
    }
}
