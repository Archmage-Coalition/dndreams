package net.eman3600.dndreams.networking.packet_s2c;

import net.eman3600.dndreams.mixin_interfaces.ClientWorldAccess;
import net.eman3600.dndreams.util.DelayedClientExecution;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;

import java.util.function.BiConsumer;

public class DelayedS2CPacket {
    public static ClientPlayNetworking.PlayChannelHandler create(BiConsumer<MinecraftClient, DelayedClientExecution> function) {
        return (client, handler, buf, sender) -> {
            if (client.world instanceof ClientWorldAccess access) {
                access.dndreams$delayPacket(buf, function);
            }
        };
    }

    /**
     * Creates a delayed play channel handler that executes a function
     * on the next client world tick on the main render thread
     * @param function the function to execute
     * @param setup allows data to be transferred from
     *              the sent packet to the function
     * @return
     */
    public static ClientPlayNetworking.PlayChannelHandler create(BiConsumer<MinecraftClient, DelayedClientExecution> function, BiConsumer<PacketByteBuf, DelayedClientExecution> setup) {
        return (client, handler, buf, sender) -> {
            if (client.world instanceof ClientWorldAccess access) {
                access.dndreams$delayPacket(buf, function, setup);
            }
        };
    }
}
