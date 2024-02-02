package net.eman3600.dndreams.networking.packet_s2c;

import net.eman3600.dndreams.initializers.event.ModMessages;
import net.eman3600.dndreams.initializers.event.ModParticles;
import net.eman3600.dndreams.items.interfaces.AirSwingItem;
import net.eman3600.dndreams.items.magic_bow.BloodyCarbineItem;
import net.eman3600.dndreams.networking.packet_c2s.AirSwingPacket;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class BloodyLaserPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf packet, PacketSender sender) {
        Vec3d vec = new Vec3d(packet.readDouble(), packet.readDouble(), packet.readDouble());

        float pitch = packet.readFloat();
        float yaw = packet.readFloat();

        Vec3d angle = AirSwingItem.rayZVector(yaw, pitch).multiply(.25f);
        World world = client.world;

        for (int i = 0; i < BloodyCarbineItem.RANGE * 4; i++) {

            vec = vec.add(angle);
            world.addParticle(ModParticles.BLOODY_LASER, true, vec.x, vec.y, vec.z, 0, 0, 0);

            Box box = Box.of(vec, .5, .5, .5);
            if (world.getBlockCollisions(null, box).iterator().hasNext()) {
                break;
            }
        }
    }

    public static void send(ServerWorld world, Vec3d origin, float pitch, float yaw) {
        PacketByteBuf packet = PacketByteBufs.create();

        packet.writeDouble(origin.x);
        packet.writeDouble(origin.y);
        packet.writeDouble(origin.z);
        packet.writeFloat(pitch);
        packet.writeFloat(yaw);

        for (ServerPlayerEntity player: world.getPlayers()) {
            ServerPlayNetworking.send(player, ModMessages.BLOODY_LASER_ID, packet);
        }
    }
}
