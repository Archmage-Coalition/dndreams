package net.eman3600.dndreams.networking.packet_s2c;

import net.eman3600.dndreams.initializers.event.ModMessages;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class ManagoldFlashPacket {

    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf packet, PacketSender sender) {

        Vec3d pos = new Vec3d(packet.readDouble(), packet.readDouble(), packet.readDouble());
        ParticleManager manager = client.particleManager;

        Particle flash = manager.addParticle(ParticleTypes.FLASH, pos.x, pos.y, pos.z, 0, 0, 0);
        flash.setColor(.965f, .761f, .263f);
    }

    public static void send(ServerWorld world, Vec3d pos) {
        PacketByteBuf packet = PacketByteBufs.create();

        packet.writeDouble(pos.x);
        packet.writeDouble(pos.y);
        packet.writeDouble(pos.z);

        for (ServerPlayerEntity player: PlayerLookup.tracking(world, new BlockPos(pos))) {

            ServerPlayNetworking.send(player, ModMessages.MANAGOLD_FLASH_ID, packet);
        }
    }
}
