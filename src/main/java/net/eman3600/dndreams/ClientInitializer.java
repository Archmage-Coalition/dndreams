package net.eman3600.dndreams;

import net.eman3600.dndreams.initializers.ModScreenHandlerTypes;
import net.eman3600.dndreams.screen.WeavingScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

public class ClientInitializer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ScreenRegistry.register(ModScreenHandlerTypes.WEAVING, WeavingScreen::new);
    }
}
