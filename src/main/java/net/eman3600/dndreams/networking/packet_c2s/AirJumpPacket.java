package net.eman3600.dndreams.networking.packet_c2s;

import net.eman3600.dndreams.cardinal_components.InfusionComponent;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.initializers.event.ModMessages;
import net.eman3600.dndreams.mixin_interfaces.LivingEntityAccess;
import net.eman3600.dndreams.networking.packet_s2c.GoldenLandPacket;
import net.eman3600.dndreams.networking.packet_s2c.MotionUpdatePacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class AirJumpPacket {

    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf packet, PacketSender sender) {

        InfusionComponent component = EntityComponents.INFUSION.get(player);

        if (component.airJump()) {

            player.setVelocity(packet.readDouble(), packet.readDouble(), packet.readDouble());
            player.world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BLOCK_AMETHYST_BLOCK_STEP, SoundCategory.BLOCKS, 1, 1);
            GoldenLandPacket.send(player.getWorld(), player.getPos(), player.getYaw());
        }
    }

    @Environment(EnvType.CLIENT)
    public static void send(Vec3d velocity) {

        PacketByteBuf packet = PacketByteBufs.create();

        packet.writeDouble(velocity.x);
        packet.writeDouble(velocity.y);
        packet.writeDouble(velocity.z);

        ClientPlayNetworking.send(ModMessages.AIR_JUMP_ID, packet);
    }
}
