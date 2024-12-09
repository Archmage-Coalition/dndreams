package net.eman3600.dndreams.networking.packet_s2c;

import net.eman3600.dndreams.initializers.event.ModParticles;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;

public class HemorrhageScarPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf packet, PacketSender sender) {
        if (client.world == null) return;
        Vec3d vec = new Vec3d(packet.readDouble(), packet.readDouble(), packet.readDouble());

        Random random = client.world.random;

        client.world.addParticle(ModParticles.HEMORRHAGE_SCAR, false, vec.x, vec.y, vec.z, 0, 0, 0);
    }
}
