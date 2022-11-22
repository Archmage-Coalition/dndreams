package net.eman3600.dndreams.world.biome;

import org.betterx.bclib.config.PathConfig;

import static net.eman3600.dndreams.Initializer.MODID;

public class GeneratorOptions {
	public static LayerOptions bigOptions;
	public static LayerOptions mediumOptions;
	public static LayerOptions smallOptions;
	public static int biomeSizeLand;
	public static int biomeSizeVoid;
	public static int biomeSizeCave;
	
	public static void registerGeneratorOptions() {
		PathConfig config = new PathConfig(MODID, "generator", false, false);
		
		bigOptions = new LayerOptions("terrain.layers.bigIslands", config, 300, 200, 64, 20);
		mediumOptions = new LayerOptions("terrain.layers.mediumIslands", config, 150, 100, 64, 20);
		smallOptions = new LayerOptions("terrain.layers.smallIslands", config, 60, 50, 64, 20);
		biomeSizeLand = config.getInt("biomes", "landBiomeSize", 256);
		biomeSizeVoid = config.getInt("biomes", "voidBiomeSize", 256);
		biomeSizeCave = config.getInt("biomes", "caveBiomeSize", 128);
		
		config.saveChanges();
	}
}
