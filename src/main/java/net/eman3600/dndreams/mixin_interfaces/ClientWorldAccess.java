package net.eman3600.dndreams.mixin_interfaces;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

public interface ClientWorldAccess {
    MinecraftClient getClient();
    ClientPlayerEntity getPlayer();
}
