package net.eman3600.dndreams.networking.packet_s2c;

import net.eman3600.dndreams.initializers.event.ModParticles;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class CrownedBeamPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf packet, PacketSender sender) {
        if (client.world == null) return;
        Vec3d vec = new Vec3d(packet.readDouble(), packet.readDouble(), packet.readDouble());

        boolean wicked = packet.readBoolean();
        boolean weak = packet.readBoolean();

        if (wicked) {
            Random random = new Random();

            if (random.nextInt(3) == 0) {
                client.world.addParticle(ModParticles.CROWNED_WICKED, true, vec.x, vec.y, vec.z, 0, 0, 0);
                return;
            }
        }

        client.world.addParticle(weak ? ModParticles.CROWNED_BEAM_WEAK : ModParticles.CROWNED_BEAM, true, vec.x, vec.y, vec.z, 0, 0, 0);
    }
}
