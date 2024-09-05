package net.eman3600.dndreams.mixin.client;

import net.eman3600.dndreams.cardinal_components.MusicTrackerComponent;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.initializers.event.ModMessages;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.MusicTracker;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.sound.MusicSound;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    private boolean isPlayingCustomMusic = false;

    @Shadow @Nullable public ClientPlayerEntity player;

    @Shadow public abstract SoundManager getSoundManager();

    @Shadow public abstract MusicTracker getMusicTracker();

    @Inject(method = "doAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;resetLastAttackedTicks()V"))
    private void dndreams$doAttack(CallbackInfoReturnable<Boolean> cir) {
        ClientPlayNetworking.send(ModMessages.AIR_SWING_ID, PacketByteBufs.create());
    }

    @Inject(method = "getMusicType", at = @At("HEAD"), cancellable = true)
    private void dndreams$getMusicType(CallbackInfoReturnable<MusicSound> cir) {
        if (player != null && EntityComponents.MUSIC_TRACKER.isProvidedBy(player)) {
            MusicTrackerComponent component = EntityComponents.MUSIC_TRACKER.get(player);

            if (component.shouldUseCustomMusic()) {
                isPlayingCustomMusic = true;
                cir.setReturnValue(component.getCustomMusic());
            } else if (isPlayingCustomMusic) {
                isPlayingCustomMusic = false;
                getMusicTracker().stop();
            }
        }
    }
}
