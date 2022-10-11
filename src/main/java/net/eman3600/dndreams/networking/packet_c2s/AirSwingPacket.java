package net.eman3600.dndreams.networking.packet_c2s;

import net.eman3600.dndreams.initializers.ModParticles;
import net.eman3600.dndreams.items.interfaces.AirSwingItem;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import org.checkerframework.checker.nullness.qual.NonNull;

public class AirSwingPacket {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf packet, PacketSender sender) {
        if (player.getMainHandStack().getItem() instanceof AirSwingItem item) {
            item.swingItem(player, Hand.MAIN_HAND, player.getWorld(), player.getMainHandStack(), null);
        }


    }
}
