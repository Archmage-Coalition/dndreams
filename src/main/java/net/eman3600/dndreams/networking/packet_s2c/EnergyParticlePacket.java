package net.eman3600.dndreams.networking.packet_s2c;

import net.eman3600.dndreams.blocks.energy.CosmicFountainBlock;
import net.eman3600.dndreams.initializers.basics.ModBlocks;
import net.eman3600.dndreams.initializers.event.ModMessages;
import net.eman3600.dndreams.initializers.event.ModParticles;
import net.eman3600.dndreams.util.DelayedClientExecution;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

public class EnergyParticlePacket {
    @Environment(EnvType.CLIENT)
    public static void receive(MinecraftClient client, DelayedClientExecution exe) {
        BlockPos pos = new BlockPos(exe.popInt(), exe.popInt(), exe.popInt());
        BlockPos blockPos = new BlockPos(exe.popInt(), exe.popInt(), exe.popInt());

        if (client.world != null) ((CosmicFountainBlock)ModBlocks.COSMIC_FOUNTAIN).displayEnchantParticle(client.world, pos, blockPos, ModParticles.COSMIC_ENERGY);
    }

    @Environment(EnvType.CLIENT)
    public static void pack(PacketByteBuf buf, DelayedClientExecution exe) {
        exe.pushInt(buf.readInt());
        exe.pushInt(buf.readInt());
        exe.pushInt(buf.readInt());

        exe.pushInt(buf.readInt());
        exe.pushInt(buf.readInt());
        exe.pushInt(buf.readInt());
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
