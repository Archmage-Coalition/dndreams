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
import net.minecraft.util.math.random.Random;

public class HemorrhageSlashPacket {
    public static void receive(MinecraftClient client, DelayedClientExecution exe) {
        if (client.world == null) return;
        Vec3d vec = new Vec3d(exe.popDouble(), exe.popDouble(), exe.popDouble());

        client.world.addParticle(ModParticles.HEMORRHAGE_SLASH, false, vec.x, vec.y, vec.z, 0, 0, 0);
    }

    @Environment(EnvType.CLIENT)
    public static void pack(PacketByteBuf buf, DelayedClientExecution exe) {
        exe.pushDouble(buf.readDouble());
        exe.pushDouble(buf.readDouble());
        exe.pushDouble(buf.readDouble());
    }
}
