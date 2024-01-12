package net.eman3600.dndreams.networking.packet_s2c;

import net.eman3600.dndreams.initializers.event.ModMessages;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

public class MotionUpdatePacket {

    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf packet, PacketSender sender) {
        PlayerEntity player = client.player;

        if (player != null) {

            player.setVelocity(packet.readDouble(), packet.readDouble(), packet.readDouble());
        }
    }

    public static void send(ServerPlayerEntity player) {
        PacketByteBuf packet = PacketByteBufs.create();
        Vec3d vec = player.getVelocity();

        packet.writeDouble(vec.x);
        packet.writeDouble(vec.y);
        packet.writeDouble(vec.z);

        ServerPlayNetworking.send(player, ModMessages.MOTION_UPDATE_ID, packet);
    }
}
