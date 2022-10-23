package net.eman3600.dndreams.networking.packet_s2c;

import net.eman3600.dndreams.blocks.candle.CosmicFountainBlock;
import net.eman3600.dndreams.blocks.entities.CosmicFountainBlockEntity;
import net.eman3600.dndreams.initializers.ModBlocks;
import net.eman3600.dndreams.initializers.ModMessages;
import net.eman3600.dndreams.initializers.ModParticles;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import javax.swing.text.html.parser.Entity;
import java.util.Random;

public class EnergyParticlePacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf packet, PacketSender sender) {
        BlockPos pos = new BlockPos(packet.readInt(), packet.readInt(), packet.readInt());
        BlockPos blockPos = new BlockPos(packet.readInt(), packet.readInt(), packet.readInt());

        if (client.world != null) ((CosmicFountainBlock)ModBlocks.COSMIC_FOUNTAIN).displayEnchantParticle(client.world, pos, blockPos, ModParticles.COSMIC_ENERGY);
    }

    public static void send(ServerPlayerEntity player, BlockPos pos, BlockPos offset) {
        PacketByteBuf packet = PacketByteBufs.create();

        packet.writeInt(pos.getX());
        packet.writeInt(pos.getY());
        packet.writeInt(pos.getZ());

        packet.writeInt(offset.getX());
        packet.writeInt(offset.getY());
        packet.writeInt(offset.getZ());

        ServerPlayNetworking.send(player, ModMessages.ENERGY_PARTICLE_ID, packet);
    }

    public static void send(ServerPlayerEntity player, BlockPos pos, LivingEntity target) {
        send(player, target.getBlockPos(), pos.subtract(target.getBlockPos()));
    }
}
