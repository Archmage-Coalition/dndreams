package net.eman3600.dndreams.mixin_interfaces;

import net.eman3600.dndreams.util.DelayedClientExecution;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface ClientWorldAccess {
    MinecraftClient dndreams$getClient();
    PlayerEntity dndreams$getPlayer();
    void dndreams$delayPacket(PacketByteBuf packet, BiConsumer<MinecraftClient, DelayedClientExecution> function);
    void dndreams$delayPacket(PacketByteBuf packet, BiConsumer<MinecraftClient, DelayedClientExecution> function, BiConsumer<PacketByteBuf, DelayedClientExecution> setup);
}
