package net.eman3600.dndreams.events;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;

public class ModEventsRegister {
    public static void registerEvents() {
        ServerPlayerEvents.COPY_FROM.register(new PlayerEvents());
    }
}
