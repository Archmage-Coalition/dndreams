package net.eman3600.dndreams.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.initializers.WorldComponents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
import org.spongepowered.asm.util.Quantifier;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
    @Unique
    private static final Identifier BLOOD_MOON_PHASES = new Identifier(Initializer.MODID,"textures/environment/blood_moon_phases.png");

    @Shadow
    private ClientWorld world;

    @Inject(method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/math/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getMoonPhase()I"))
    private void changeMoon(MatrixStack matrices, Matrix4f projectionMatrix, float tickDelta, Camera camera, boolean bl, Runnable runnable, CallbackInfo info) {
        if (WorldComponents.BLOOD_MOON.get(this.world).damnedNight()) {
            RenderSystem.setShaderTexture(0, BLOOD_MOON_PHASES);
        }
    }
}
