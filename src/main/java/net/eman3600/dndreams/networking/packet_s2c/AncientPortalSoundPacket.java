package net.eman3600.dndreams.networking.packet_s2c;

import net.eman3600.dndreams.initializers.ModParticles;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class AncientPortalSoundPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf packet, PacketSender sender) {
        Vec3d vec = new Vec3d(packet.readDouble(), packet.readDouble(), packet.readDouble());

        boolean useDistance = !packet.readBoolean();
        boolean activation = packet.readBoolean();

        client.world.playSound(new BlockPos(vec), activation ? SoundEvents.BLOCK_BEACON_ACTIVATE : SoundEvents.BLOCK_BEACON_DEACTIVATE, SoundCategory.BLOCKS, 2f, 1f, useDistance);
    }
}
