package net.eman3600.dndreams.mixin.client;

import net.eman3600.dndreams.ClientInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Environment(EnvType.CLIENT)
@Mixin(LightmapTextureManager.class)
public abstract class LightmapTextureManagerMixin {

    @Shadow @Final private MinecraftClient client;

    @Unique private final int SHADOW = 26;

    @Shadow @Final private NativeImage image;

    @Inject(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/NativeImage;setColor(III)V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void dndreams$update(float delta, CallbackInfo ci, ClientWorld clientWorld, float f, float g, float h, float i, float j, float l, float k, Vec3f vec3f, float m, Vec3f vec3f2, int n, int o, float p, float q, float r, float s, float t, boolean bl, float v, Vec3f vec3f5, int w, int x, int y, int z) {
        if (ClientInitializer.drawAether(clientWorld)) {
            z = recalculated_light(z);
            y = recalculated_light(y);
            x = recalculated_light(x);

            //int alpha = MathHelper.clamp(z + y + x, 0, 0xFF);

            image.setColor(o, n, 0xFF << 24 | z << 16 | y << 8 | x);

            //System.out.println("Alpha: " + alpha + " R: " + z + " G: " + y + " B: " + x);
        }
    }

    @Unique
    private int recalculated_light(int i) {
        return (int)(Math.max(0, i - SHADOW) * 255.0/(255 - SHADOW));
    }
}
