package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.items.interfaces.AirSwingItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin implements ServerPlayPacketListener {

    @Shadow public ServerPlayerEntity player;

    @Shadow @Final private MinecraftServer server;

    @Inject(method = "onHandSwing", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;updateLastActionTime()V"))
    private void dndreams$onHandSwing(HandSwingC2SPacket packet, CallbackInfo ci) {
        ItemStack stack = player.getStackInHand(packet.getHand());

        try {
            if (stack.getItem() instanceof AirSwingItem item) {
                item.swingItem(player, packet.getHand(), (ServerWorld) player.world, stack, null);
            }
        } catch (ClassCastException ignored) {}
    }

    @Inject(method = "checkForSpam", at = @At("HEAD"), cancellable = true)
    private void dndreams$checkForSpam(CallbackInfo ci) {
        if (server.isHost(player.getGameProfile())) {
            ci.cancel();
        }
    }
}
