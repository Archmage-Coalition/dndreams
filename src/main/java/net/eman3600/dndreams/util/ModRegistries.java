package net.eman3600.dndreams.util;

import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.initializers.ModAttributes;
import net.eman3600.dndreams.initializers.ModBlocks;
import net.eman3600.dndreams.initializers.ModItems;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.minecraft.entity.EntityType;

public class ModRegistries {
    public static void register() {
        registerFuels();
        registerFlammableBlocks();
        registerStrippables();
        registerAttributes();
    }

    private static void registerFuels() {
        Initializer.LOGGER.info("Registering fuels for " + Initializer.MODID);

        FuelRegistry registry = FuelRegistry.INSTANCE;

        registry.add(ModItems.NIGHTMARE_FUEL, 3200);
    }

    private static void registerStrippables() {
        StrippableBlockRegistry.register(ModBlocks.DREAMWOOD, ModBlocks.STRIPPED_DREAMWOOD);
        StrippableBlockRegistry.register(ModBlocks.DREAMWOOD_LOG, ModBlocks.STRIPPED_DREAMWOOD_LOG);
    }

    private static void registerFlammableBlocks() {
        FlammableBlockRegistry registry = FlammableBlockRegistry.getDefaultInstance();

        registry.add(ModBlocks.DREAMWOOD_LOG, 5, 5);
        registry.add(ModBlocks.DREAMWOOD, 5, 5);
        registry.add(ModBlocks.STRIPPED_DREAMWOOD_LOG, 5, 5);
        registry.add(ModBlocks.STRIPPED_DREAMWOOD, 5, 5);

        registry.add(ModBlocks.DREAMWOOD_PLANKS, 5, 20);
        registry.add(ModBlocks.DREAMWOOD_LEAVES, 30, 60);
    }

    private static void registerAttributes() {

    }
}
