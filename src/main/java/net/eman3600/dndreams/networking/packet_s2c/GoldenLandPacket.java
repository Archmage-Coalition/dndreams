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
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class GoldenLandPacket {

    private static final int DISTANCE = 2;
    private static final float HALF_DIST = DISTANCE * .5f;
    private static final float SPACING = .5f;

    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf packet, PacketSender sender) {
        ClientWorld world = client.world;

        Vec3d pos = new Vec3d(packet.readDouble(), packet.readDouble(), packet.readDouble());
        float yaw = packet.readFloat();

        Vec3d offsetX = AirSwingItem.rayXVector(yaw, 0).multiply(SPACING);
        Vec3d offsetZ = AirSwingItem.rayZVector(yaw, 0).multiply(SPACING);

        renderLine(world, pos.add(offsetZ.multiply(-HALF_DIST)).add(offsetX.multiply(-DISTANCE)), offsetX);
        renderLine(world, pos.add(offsetZ.multiply(HALF_DIST)).add(offsetX.multiply(-DISTANCE)), offsetX);
        renderLine(world, pos.add(offsetX.multiply(-HALF_DIST)).add(offsetZ.multiply(-DISTANCE)), offsetZ);
        renderLine(world, pos.add(offsetX.multiply(HALF_DIST)).add(offsetZ.multiply(-DISTANCE)), offsetZ);
    }

    private static void renderLine(ClientWorld world, Vec3d pos, Vec3d dir) {

        for (int i = -DISTANCE; i <= DISTANCE; i++) {
            world.addParticle(ModParticles.GOLDEN_LAND, pos.x, pos.y, pos.z, 0, 0, 0);

            pos = pos.add(dir);
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
