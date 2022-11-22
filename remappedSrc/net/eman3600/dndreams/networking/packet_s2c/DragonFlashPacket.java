package net.eman3600.dndreams.networking.packet_s2c;

import net.eman3600.dndreams.mixin_interfaces.HudAccess;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;

public class DragonFlashPacket {


    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf packet, PacketSender sender) {
        ((HudAccess)client.inGameHud).setDragonFlash(packet.getInt(0));
        System.out.println("Packet sent!");
    }
}
