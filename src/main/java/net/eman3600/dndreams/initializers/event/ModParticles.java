package net.eman3600.dndreams.initializers.event;

import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.particles.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.particle.EnchantGlyphParticle;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static net.eman3600.dndreams.Initializer.MODID;

public class ModParticles {
    public static final DefaultParticleType CROWNED_SLASH = FabricParticleTypes.simple();
    public static final DefaultParticleType CROWNED_BEAM = FabricParticleTypes.simple();
    public static final DefaultParticleType CROWNED_BEAM_WEAK = FabricParticleTypes.simple();
    public static final DefaultParticleType CROWNED_WICKED = FabricParticleTypes.simple();
    public static final DefaultParticleType TESLA_SLASH = FabricParticleTypes.simple();
    public static final DefaultParticleType DIAMOND_SPARK = FabricParticleTypes.simple();
    public static final DefaultParticleType GLOW_SPARK = FabricParticleTypes.simple();
    public static final DefaultParticleType BLOODY_LASER = FabricParticleTypes.simple();
    public static final DefaultParticleType GOLDEN_LAND = FabricParticleTypes.simple();

    public static final DefaultParticleType COSMIC_CANDLE_FLAME = FabricParticleTypes.simple();
    public static final DefaultParticleType ECHO_CANDLE_FLAME = FabricParticleTypes.simple();
    public static final DefaultParticleType SOUL_CANDLE_FLAME = FabricParticleTypes.simple();
    public static final DefaultParticleType COSMIC_ENERGY = FabricParticleTypes.simple();
    public static final DefaultParticleType SOUL_ENERGY = FabricParticleTypes.simple();

    public static final DefaultParticleType CAULDRON_BUBBLE = FabricParticleTypes.simple();
    public static final DefaultParticleType CAULDRON_SPARKLE = FabricParticleTypes.simple();



    public static void registerParticles() {
        registerParticle("crowned_slash", CROWNED_SLASH);
        registerParticle("crowned_beam", CROWNED_BEAM);
        registerParticle("crowned_beam_weak", CROWNED_BEAM_WEAK);
        registerParticle("crowned_wicked", CROWNED_WICKED);
        registerParticle("tesla_slash", TESLA_SLASH);
        registerParticle("diamond_spark", DIAMOND_SPARK);
        registerParticle("glow_spark", GLOW_SPARK);
        registerParticle("bloody_laser", BLOODY_LASER);
        registerParticle("golden_land", GOLDEN_LAND);

        registerParticle("cosmic_candle_flame", COSMIC_CANDLE_FLAME);
        registerParticle("echo_candle_flame", ECHO_CANDLE_FLAME);
        registerParticle("soul_candle_flame", SOUL_CANDLE_FLAME);

        registerParticle("cosmic_energy", COSMIC_ENERGY);
        registerParticle("soul_energy", SOUL_ENERGY);

        registerParticle("cauldron_bubble", CAULDRON_BUBBLE);
        registerParticle("cauldron_sparkle", CAULDRON_SPARKLE);
    }


    @Environment(EnvType.CLIENT)
    public static void registerParticleFactories() {

        ParticleFactoryRegistry.getInstance().register(CROWNED_SLASH, SlashParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(CROWNED_BEAM, BeamParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(CROWNED_BEAM_WEAK, BeamParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(CROWNED_WICKED, ExtendedBeamParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(TESLA_SLASH, ExtendedBeamParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(DIAMOND_SPARK, DiamondSparkParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(GLOW_SPARK, GlowSparkParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(BLOODY_LASER, SlashParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(GOLDEN_LAND, GoldenLandParticle.Factory::new);

        ParticleFactoryRegistry.getInstance().register(COSMIC_CANDLE_FLAME, FlameParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ECHO_CANDLE_FLAME, FlameParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(SOUL_CANDLE_FLAME, FlameParticle.Factory::new);

        ParticleFactoryRegistry.getInstance().register(COSMIC_ENERGY, EnchantGlyphParticle.EnchantFactory::new);
        ParticleFactoryRegistry.getInstance().register(SOUL_ENERGY, EnchantGlyphParticle.EnchantFactory::new);

        ParticleFactoryRegistry.getInstance().register(CAULDRON_BUBBLE, CauldronBubbleParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(CAULDRON_SPARKLE, CauldronSparkleParticle.Factory::new);
    }

    @Environment(EnvType.CLIENT)
    public static void registerParticleTextures() {
        registerAtlasTexture("bloody_laser");
        registerAtlasTexture("bubble");
        registerAtlasTexture("bubble_pop_0");
        registerAtlasTexture("bubble_pop_1");
        registerAtlasTexture("bubble_pop_2");
        registerAtlasTexture("bubble_pop_3");
        registerAtlasTexture("bubble_pop_4");
        registerAtlasTexture("cauldron_sparkle");
        registerAtlasTexture("cosmic_candle_flame");
        registerAtlasTexture("cosmic_energy");
        registerAtlasTexture("crowned_beam");
        registerAtlasTexture("crowned_slash");
        registerAtlasTexture("crowned_wicked");
        registerAtlasTexture("diamond_spark");
        registerAtlasTexture("echo_candle_flame");
        registerAtlasTexture("glow_spark");
        registerAtlasTexture("golden_land");
        registerAtlasTexture("soul_candle_flame");
        registerAtlasTexture("soul_energy");
        registerAtlasTexture("tesla_slash");
    }


    private static void registerParticle(String name, DefaultParticleType type) {
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(MODID, name), type);
    }

    @Environment(EnvType.CLIENT)
    private static void registerAtlasTexture(String sprite) {
        ClientSpriteRegistryCallback.event(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).register(((atlasTexture, registry) -> registry.register(new Identifier(MODID, "particle/" + sprite))));
    }
}
