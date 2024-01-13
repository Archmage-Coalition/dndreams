package net.eman3600.dndreams.networking.packet_s2c;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.eman3600.dndreams.initializers.event.ModMessages;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

public class DreamShiftPacket {

    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf packet, PacketSender sender) {
        TrinketComponent trinkets = TrinketsApi.getTrinketComponent(client.player).get();

        for (var map : trinkets.getInventory().values()) for (var inv : map.values()) {

            for (int i = 0; i < inv.size(); i++) {

                inv.setStack(i, ItemStack.EMPTY);
            }
        }

        trinkets.update();
    }

    public static void send(ServerPlayerEntity player) {
        ServerPlayNetworking.send(player, ModMessages.DREAM_SHIFT_ID, PacketByteBufs.empty());
    }
}
