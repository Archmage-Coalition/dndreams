package net.eman3600.dndreams.networking.packet_s2c;

import net.eman3600.dndreams.initializers.event.ModMessages;
import net.eman3600.dndreams.initializers.event.ModParticles;
import net.eman3600.dndreams.items.interfaces.AirSwingItem;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class GoldenLandPacket {

    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf packet, PacketSender sender) {
        ClientWorld world = client.world;

        Vec3d pos = new Vec3d(packet.readDouble(), packet.readDouble(), packet.readDouble());
        float yaw = packet.readFloat();

        Vec3d offset = AirSwingItem.rayXVector(yaw, 0).multiply(.5f);

        pos = pos.add(offset.multiply(-3));

        for (int i = -3; i <= 3; i++) {
            world.addParticle(ModParticles.GOLDEN_LAND, pos.x, pos.y, pos.z, 0, 0, 0);

            pos = pos.add(offset);
        }
    }

    public static void send(ServerWorld world, Vec3d pos, float yaw) {
        PacketByteBuf packet = PacketByteBufs.create();

        packet.writeDouble(pos.x);
        packet.writeDouble(pos.y);
        packet.writeDouble(pos.z);
        packet.writeFloat(yaw);

        for (ServerPlayerEntity player: PlayerLookup.tracking(world, new BlockPos(pos))) {

            ServerPlayNetworking.send(player, ModMessages.GOLDEN_LAND_ID, packet);
        }
    }
}
