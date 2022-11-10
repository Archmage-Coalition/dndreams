package net.eman3600.dndreams.mixin.client;

import net.eman3600.dndreams.initializers.event.ModParticles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {

    @Shadow private ClientWorld world;

    @Shadow @Final private static Logger LOGGER;

    @Inject(method = "onParticle", at = @At("HEAD"), cancellable = true)
    private void dndreams$onParticle(ParticleS2CPacket packet, CallbackInfo ci) {
        if (packet.getParameters() == ModParticles.COSMIC_ENERGY || packet.getParameters() == ModParticles.SOUL_ENERGY) {
            double d = packet.getOffsetX();
            double e = packet.getOffsetY();
            double f = packet.getOffsetZ();
            try {
                this.world.addParticle(packet.getParameters(), packet.isLongDistance(), packet.getX(), packet.getY(), packet.getZ(), d, e, f);
            }
            catch (Throwable throwable) {
                LOGGER.warn("Could not spawn particle effect {}", packet.getParameters());
            }

            ci.cancel();
        }
    }
}
