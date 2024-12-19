package net.eman3600.dndreams.networking.packet_s2c;

import net.eman3600.dndreams.initializers.event.ModMessages;
import net.eman3600.dndreams.util.DelayedClientExecution;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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

    @Environment(EnvType.CLIENT)
    public static void receive(MinecraftClient client, DelayedClientExecution exe) {

        Vec3d pos = new Vec3d(exe.popDouble(), exe.popDouble(), exe.popDouble());
        ParticleManager manager = client.particleManager;

        Particle flash = manager.addParticle(ParticleTypes.FLASH, pos.x, pos.y, pos.z, 0, 0, 0);
        if (flash != null)
            flash.setColor(.965f, .761f, .263f);
    }

    @Environment(EnvType.CLIENT)
    public static void pack(PacketByteBuf buf, DelayedClientExecution exe) {
        exe.pushDouble(buf.readDouble());
        exe.pushDouble(buf.readDouble());
        exe.pushDouble(buf.readDouble());
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
