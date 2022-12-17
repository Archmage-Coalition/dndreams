package net.eman3600.dndreams.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.eman3600.dndreams.ClientInitializer;
import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.initializers.cca.WorldComponents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
    @Unique
    private static final Identifier BLOOD_MOON_PHASES = new Identifier(Initializer.MODID,"textures/environment/blood_moon_phases.png");

    @Unique
    private static final Identifier EMPTY_MOON = new Identifier(Initializer.MODID,"textures/environment/empty_moon.png");

    @Shadow
    private ClientWorld world;

    @Shadow @Final private MinecraftClient client;

    @Shadow protected abstract boolean method_43788(Camera camera);

    @Shadow @Nullable private VertexBuffer starsBuffer;

    @Shadow public abstract void tick();

    @Shadow public abstract void tickRainSplashing(Camera camera);

    @Inject(method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/math/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getMoonPhase()I"))
    private void dndreams$renderSky$changeMoon(MatrixStack matrices, Matrix4f projectionMatrix, float tickDelta, Camera camera, boolean bl, Runnable runnable, CallbackInfo info) {
        if (WorldComponents.BLOOD_MOON.get(this.world).damnedNight()) {
            //RenderSystem.setShaderTexture(0, BLOOD_MOON_PHASES);
            RenderSystem.setShaderColor(1f, 0f, 0f, 1f);
        } else if (drawAether()) {
            RenderSystem.setShaderTexture(0, EMPTY_MOON);
        }
    }

    @Inject(method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/math/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V", at = @At(value = "HEAD", shift = At.Shift.AFTER), cancellable = true)
    private void dndreams$renderSky$drawAether(MatrixStack matrices, Matrix4f projectionMatrix, float tickDelta, Camera camera, boolean bl, Runnable runnable, CallbackInfo info) {
        if (drawAether()) {
            if (!bl) {
                CameraSubmersionType cameraSubmersionType = camera.getSubmersionType();
                if (cameraSubmersionType != CameraSubmersionType.POWDER_SNOW && cameraSubmersionType != CameraSubmersionType.LAVA && !method_43788(camera)) {
                    RenderSystem.disableTexture();
                    //BackgroundRenderer.setFogBlack();

                    RenderSystem.setShaderColor(1, 1, 1, 1);
                    BackgroundRenderer.clearFog();
                    this.starsBuffer.bind();
                    this.starsBuffer.draw(matrices.peek().getPositionMatrix(), projectionMatrix, GameRenderer.getPositionShader());
                    VertexBuffer.unbind();
                    runnable.run();

                    RenderSystem.setShaderColor(0, 0, 0, 1.0F);

                    RenderSystem.enableTexture();
                    RenderSystem.depthMask(true);
                    BackgroundRenderer.clearFog();
                }
            }

            info.cancel();
        }
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/DimensionEffects;useThickFog(II)Z"))
    private boolean dndreams$render$removeFog(DimensionEffects instance, int i, int j) {
        if (drawAether()) return false;

        return instance.useThickFog(i, j);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;getSkyDarkness(F)F"))
    private float dndreams$render$getSkyDarkness(GameRenderer instance, float tickDelta) {
        if (drawAether()) return 1f;

        return instance.getSkyDarkness(tickDelta);
    }

    @Redirect(method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/math/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/DimensionEffects;getFogColorOverride(FF)[F"))
    private float[] dndreams$renderSky$removeFog(DimensionEffects instance, float skyAngle, float tickDelta) {
        if (drawAether()) return null;

        return instance.getFogColorOverride(skyAngle, tickDelta);
    }



    @Unique
    private boolean drawAether() {
        return ClientInitializer.drawAether(world);
    }
}
