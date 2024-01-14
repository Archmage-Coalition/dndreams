package net.eman3600.dndreams.mixin.client;

import net.eman3600.dndreams.ClientInitializer;
import net.eman3600.dndreams.cardinal_components.TormentComponent;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.initializers.cca.WorldComponents;
import net.eman3600.dndreams.items.celestium.CelestiumArmorItem;
import net.eman3600.dndreams.mixin_interfaces.ClientWorldAccess;
import net.eman3600.dndreams.mixin_interfaces.LightmapTextureManagerAccess;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Environment(EnvType.CLIENT)
@Mixin(LightmapTextureManager.class)
public abstract class LightmapTextureManagerMixin implements LightmapTextureManagerAccess {

    @Shadow @Final private MinecraftClient client;

    @Unique private final float SHADOW = 26;
    @Unique private final float DEPTHS = 40;
    @Unique private final float SANITY_SHADOW = 30;
    @Unique private final float DARKNESS_THRESHOLD = 70;
    @Unique private final float HAUNTED = 110;
    @Unique private final float CELESTIUM = 46;

    @Shadow @Final private NativeImage image;

    @Shadow @Final private NativeImageBackedTexture texture;

    @Inject(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/NativeImage;setColor(III)V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void dndreams$update(float delta, CallbackInfo ci, ClientWorld clientWorld, float f, float g, float h, float i, float j, float l, float k, Vec3f vec3f, float m, Vec3f vec3f2, int n, int o, float p, float q, float r, float s, float t, boolean bl, float v, Vec3f vec3f5, int w, int x, int y, int z) {

        if (ClientInitializer.drawAether(clientWorld)) {
            z = recalculatedLight(z);
            y = recalculatedLight(y);
            x = recalculatedLight(x);

            image.setColor(o, n, 0xFF << 24 | z << 16 | y << 8 | x);
        } else if (clientWorld instanceof ClientWorldAccess access) {
            float strength = (float)(EntityComponents.TORMENT.get(access.getPlayer()).getShroud()) / TormentComponent.MAX_SHROUD * DEPTHS;

            z = recalculatedLight(z, strength);
            y = recalculatedLight(y, strength);
            x = recalculatedLight(x, strength);

            TormentComponent component = EntityComponents.TORMENT.get(access.getPlayer());

            if (component.getAttunedSanity() < DARKNESS_THRESHOLD && !component.isTruthActive()) {
                float clamped = MathHelper.clamp(component.getAttunedSanity(), 0, DARKNESS_THRESHOLD);
                clamped = SANITY_SHADOW - (clamped * (SANITY_SHADOW /(DARKNESS_THRESHOLD)));

                z = darkenLight(z, clamped);
                y = darkenLight(y, clamped);
                x = recalculatedLight(x, clamped);
            }

            if (component.getHaunt() > 0) {
                float haunt = HAUNTED * component.getHaunt() / TormentComponent.MAX_HAUNT;

                z = brightenLight(z, haunt);
                y = brightenLight(y, haunt);
                x = brightenLight(x, haunt/2);
            }

            if (CelestiumArmorItem.wornPieces(access.getPlayer()) >= 4) {
                z = brightenLight(z, CELESTIUM);
                y = brightenLight(y, CELESTIUM);
                x = brightenLight(x, CELESTIUM);
            }

            image.setColor(o, n, 0xFF << 24 | z << 16 | y << 8 | x);
        }
    }

    @Inject(method = "getBrightness", at = @At("HEAD"), cancellable = true)
    private static void dndreams$getBrightness(DimensionType type, int lightLevel, CallbackInfoReturnable<Float> cir) {

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world != null && client.player != null && client.world.getRegistryKey() == World.END && WorldComponents.BOSS_STATE.get(client.world.getScoreboard()).dragonSlain()) {

            cir.setReturnValue(1f);
        }
    }

    @Unique
    private int recalculatedLight(int i) {
        return recalculatedLight(i, SHADOW);
    }

    @Unique
    private int recalculatedLight(int i, float shadow) {
        return (int)(Math.max(0, i - shadow) * 255.0/(255 - shadow));
    }

    @Unique
    private int darkenLight(int i, float shadow) {
        return (int)(Math.max(0, i - shadow));
    }

    @Unique
    private int brightenLight(int i, float brightness) {
        return (int)(Math.min(255, i + brightness));
    }

    @Override
    public void clearLightMap() {
        this.client.getProfiler().push("lightTex");

        for (int n = 0; n < 16; ++n) {
            for (int o = 0; o < 16; ++o) {

                this.image.setColor(n, o, 0xFFFFFFFF);
            }
        }

        this.texture.upload();
        this.client.getProfiler().pop();
    }
}
