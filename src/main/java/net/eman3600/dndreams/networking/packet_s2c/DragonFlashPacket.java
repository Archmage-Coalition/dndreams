package net.eman3600.dndreams.networking.packet_s2c;

import net.eman3600.dndreams.initializers.EntityComponents;
import net.eman3600.dndreams.mixin_interfaces.HudMixinI;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;

public class DragonFlashPacket {


    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        ((HudMixinI)client.inGameHud).setDragonFlash(buf.getInt(0));
        System.out.println("Packet sent!");
    }
}
