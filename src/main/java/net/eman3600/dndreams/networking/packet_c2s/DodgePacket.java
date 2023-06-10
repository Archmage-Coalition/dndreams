package net.eman3600.dndreams.networking.packet_c2s;

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

public class DodgePacket {

    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf packet, PacketSender sender) {
        Vec3d velocity = new Vec3d(packet.readDouble(), packet.readDouble(), packet.readDouble());

        EntityComponents.INFUSION.maybeGet(player).ifPresent(infusion -> infusion.tryDodgeServer(velocity));
    }

    @Environment(EnvType.CLIENT)
    public static void send(Vec3d velocity) {

        PacketByteBuf packet = PacketByteBufs.create();

        packet.writeDouble(velocity.x);
        packet.writeDouble(velocity.y);
        packet.writeDouble(velocity.z);

        ClientPlayNetworking.send(ModMessages.DODGE_ID, packet);
    }
}
