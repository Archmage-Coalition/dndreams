package net.eman3600.dndreams;

import net.eman3600.dndreams.initializers.basics.*;
import net.eman3600.dndreams.initializers.bclib.ModBiomes;
import net.eman3600.dndreams.initializers.entity.ModAttributes;
import net.eman3600.dndreams.initializers.entity.ModEntities;
import net.eman3600.dndreams.initializers.event.*;
import net.eman3600.dndreams.initializers.world.ModConfiguredFeatures;
import net.eman3600.dndreams.initializers.world.ModDimensions;
import net.eman3600.dndreams.initializers.world.ModFeatures;
import net.eman3600.dndreams.integration.commands.ModCommands;
import net.eman3600.dndreams.items.tool_mirror.ModHoeItem;
import net.eman3600.dndreams.items.tool_mirror.ModShovelItem;
import net.eman3600.dndreams.util.LootModifiers;
import net.eman3600.dndreams.util.ModRegistries;
import net.eman3600.dndreams.util.ModTags;
import net.eman3600.dndreams.world.biome.GeneratorOptions;
import net.eman3600.dndreams.world.biome.HavenBiomeSource;
import net.eman3600.dndreams.world.gen.ModWorldGen;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.bernie.geckolib3.GeckoLib;

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

		GeneratorOptions.registerGeneratorOptions();
		ModConfiguredFeatures.registerConfiguredFeatures();

		ModAttributes.registerAttributes();

		ModBlocks.registerBlocks();
		ModItems.registerItems();
		ModEnchantments.registerEnchants();
		ModFluids.registerFluids();
		ModHoeItem.injectTillActions();
		ModShovelItem.injectPathStates();
		ModStatusEffects.registerEffects();
		ModPotions.registerPotions();
		ModFeatures.registerFeatures();

		ModRituals.registerRituals();

		ModBlockEntities.registerBlockEntities();
		ModRecipeTypes.registerTypes();
		ModScreenHandlerTypes.registerTypes();

		ModWorldGen.generate();

		ModLootConditions.registerConditions();
		LootModifiers.modifyLootTables();

		ModStats.registerStats();
		ModTags.registerTags();
		ModRegistries.register();

		ModParticles.registerParticles();
		ModEntities.registerEntities();

		ModDimensions.registerDimensions();
		ModBiomes.registerBiomes();
		Registry.register(Registry.BIOME_SOURCE, new Identifier(MODID, "haven_biome_source"), HavenBiomeSource.CODEC);

		ModMessages.registerC2SPackets();
		ModCallbacks.registerCallbacks();

		ModCommands.registerCommands();

		GeckoLib.initialize();
	}
}
