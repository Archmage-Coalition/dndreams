package net.eman3600.dndreams.networking.packet_s2c;

import net.eman3600.dndreams.initializers.event.ModParticles;
import net.eman3600.dndreams.util.DelayedClientExecution;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class CloudSlashPacket {
    @Environment(EnvType.CLIENT)
    public static void receive(MinecraftClient client, DelayedClientExecution exe) {
        Vec3d vec = new Vec3d(exe.popDouble(), exe.popDouble(), exe.popDouble());

        client.world.addParticle(ModParticles.CLOUD_WIND, true, vec.x, vec.y, vec.z, 0, 0, 0);
    }

    @Environment(EnvType.CLIENT)
    public static void pack(PacketByteBuf buf, DelayedClientExecution exe) {
        exe.pushDouble(buf.readDouble());
        exe.pushDouble(buf.readDouble());
        exe.pushDouble(buf.readDouble());
    }
}
