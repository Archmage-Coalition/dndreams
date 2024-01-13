package net.eman3600.dndreams.networking.packet_c2s;

import net.eman3600.dndreams.cardinal_components.InfusionComponent;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.initializers.event.ModMessages;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

public class AirJumpPacket {

    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf packet, PacketSender sender) {

        InfusionComponent component = EntityComponents.INFUSION.get(player);

        component.airJump();
    }

    @Environment(EnvType.CLIENT)
    public static void send() {

        ClientPlayNetworking.send(ModMessages.AIR_JUMP_ID, PacketByteBufs.empty());
    }
}
