package net.eman3600.dndreams.initializers.world;

import net.eman3600.dndreams.Initializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;

public class ModDimensions {
    public static final Identifier DREAM_ID = new Identifier(Initializer.MODID, "dream");
    public static final Identifier GATEWAY_ID = new Identifier(Initializer.MODID, "gateway");

    public static final RegistryKey<DimensionOptions> DREAM_OPTIONS_KEY = RegistryKey.of(Registry.DIMENSION_KEY, DREAM_ID);
    public static final RegistryKey<DimensionType> DREAM_TYPE_KEY = RegistryKey.of
            (Registry.DIMENSION_TYPE_KEY, DREAM_ID);
    public static final RegistryKey<World> DREAM_DIMENSION_KEY = RegistryKey.of
            (Registry.WORLD_KEY, DREAM_ID);

    public static final RegistryKey<DimensionOptions> GATEWAY_OPTIONS_KEY = RegistryKey.of(Registry.DIMENSION_KEY, GATEWAY_ID);
    public static final RegistryKey<DimensionType> GATEWAY_TYPE_KEY = RegistryKey.of
            (Registry.DIMENSION_TYPE_KEY, GATEWAY_ID);
    public static final RegistryKey<World> GATEWAY_DIMENSION_KEY = RegistryKey.of
            (Registry.WORLD_KEY, GATEWAY_ID);

    public static void registerDimensions() {}
}
