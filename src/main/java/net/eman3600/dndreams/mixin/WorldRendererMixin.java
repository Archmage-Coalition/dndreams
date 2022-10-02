package net.eman3600.dndreams.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.initializers.ModDimensions;
import net.eman3600.dndreams.initializers.WorldComponents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
import org.spongepowered.asm.util.Quantifier;

@Environment(EnvType.CLIENT)
@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
    @Unique
    private static final Identifier BLOOD_MOON_PHASES = new Identifier(Initializer.MODID,"textures/environment/blood_moon_phases.png");

    @Shadow
    private ClientWorld world;

    @Shadow @Final private MinecraftClient client;

    @Redirect(method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/math/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getSkyColor(Lnet/minecraft/util/math/Vec3d;F)Lnet/minecraft/util/math/Vec3d;"))
    private Vec3d dndreams$renderSky$changeSky(ClientWorld instance, Vec3d cameraPos, float tickDelta) {
        if (drawAether(instance)) {
            return new Vec3d(0, 0, 0);
        }

        return instance.getSkyColor(cameraPos, tickDelta);
    }

    @Inject(method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/math/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getMoonPhase()I"))
    private void dndreams$renderSky$changeMoon(MatrixStack matrices, Matrix4f projectionMatrix, float tickDelta, Camera camera, boolean bl, Runnable runnable, CallbackInfo info) {
        if (WorldComponents.BLOOD_MOON.get(this.world).damnedNight()) {
            RenderSystem.setShaderTexture(0, BLOOD_MOON_PHASES);
        }
    }

    @Unique
    private boolean drawAether(World world) {
        return world.getDimensionKey() == ModDimensions.GATEWAY_TYPE_KEY;
    }
}
