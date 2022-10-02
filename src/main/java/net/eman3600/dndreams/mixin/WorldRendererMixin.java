package net.eman3600.dndreams.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.eman3600.dndreams.ClientInitializer;
import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.initializers.ModDimensions;
import net.eman3600.dndreams.initializers.WorldComponents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
import org.spongepowered.asm.util.Quantifier;

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

    @Inject(method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/math/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getMoonPhase()I"))
    private void dndreams$renderSky$changeMoon(MatrixStack matrices, Matrix4f projectionMatrix, float tickDelta, Camera camera, boolean bl, Runnable runnable, CallbackInfo info) {
        if (WorldComponents.BLOOD_MOON.get(this.world).damnedNight()) {
            RenderSystem.setShaderTexture(0, BLOOD_MOON_PHASES);
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
                }
            }

            info.cancel();
        }
    }



    @Unique
    private boolean drawAether() {
        return ClientInitializer.drawAether(world);
    }
}
