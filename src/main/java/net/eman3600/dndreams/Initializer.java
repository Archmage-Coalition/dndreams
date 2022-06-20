package net.eman3600.dndreams;

import net.eman3600.dndreams.initializers.*;
import net.eman3600.dndreams.util.ModRegistries;
import net.eman3600.dndreams.world.gen.ModWorldGen;
import net.fabricmc.api.ModInitializer;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Initializer implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static String MODID = "dndreams";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");

		ModItems.registerItems();
		ModBlocks.registerBlocks();
		ModRegistries.register();
		ModStatusEffects.registerEffects();
		ModFeatures.registerFeatures();

		ModWorldGen.generate();
	}
}
