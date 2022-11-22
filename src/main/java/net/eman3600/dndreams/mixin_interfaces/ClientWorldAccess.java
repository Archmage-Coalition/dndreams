package net.eman3600.dndreams.mixin_interfaces;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;

public interface ClientWorldAccess {
    MinecraftClient getClient();
    PlayerEntity getPlayer();
}
