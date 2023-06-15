package net.eman3600.dndreams.networking.packet_s2c;

import net.eman3600.dndreams.mixin_interfaces.HudAccess;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class DragonFlashPacket {


    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf packet, PacketSender sender) {

        ((HudAccess)client.inGameHud).setDragonFlash(packet.readInt());

        client.world.playSound(client.player, client.player.getBlockPos(), SoundEvents.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.HOSTILE, 1f, 1f);
    }
}
