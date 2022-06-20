package net.eman3600.dndreams.events;

import net.eman3600.dndreams.mixin_interfaces.ImplementPlayerStats;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerEvents implements ServerPlayerEvents.CopyFrom {
    @Override
    public void copyFromPlayer(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
        ImplementPlayerStats original = (ImplementPlayerStats)oldPlayer;
        ImplementPlayerStats player = (ImplementPlayerStats)newPlayer;

        player.getMana().putFloat("value", original.getMana().getFloat("value"));
        player.getMana().putFloat("max", original.getMana().getFloat("max"));
        player.getTorment().putFloat("value", original.getTorment().getFloat("value"));
        player.getInitiated().putBoolean("value", original.getInitiated().getBoolean("value"));
    }
}
