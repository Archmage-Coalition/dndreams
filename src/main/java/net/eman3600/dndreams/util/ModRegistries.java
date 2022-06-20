package net.eman3600.dndreams.util;

import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.initializers.ModItems;
import net.fabricmc.fabric.api.registry.FuelRegistry;

public class ModRegistries {
    public static void register() {
        registerFuels();
    }

    private static void registerFuels() {
        Initializer.LOGGER.info("Registering fuels for " + Initializer.MODID);

        FuelRegistry registry = FuelRegistry.INSTANCE;

        registry.add(ModItems.DREAM_POWDER, 3200);
    }
}
