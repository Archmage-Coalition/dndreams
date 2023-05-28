package net.eman3600.dndreams.initializers.event;

import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.particles.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.particle.EnchantGlyphParticle;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModParticles {
    public static final DefaultParticleType CROWNED_SLASH = FabricParticleTypes.simple();
    public static final DefaultParticleType CROWNED_BEAM = FabricParticleTypes.simple();
    public static final DefaultParticleType CROWNED_WICKED = FabricParticleTypes.simple();

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
        registerParticle("crowned_wicked", CROWNED_WICKED);

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
        ParticleFactoryRegistry.getInstance().register(CROWNED_SLASH, CrownedSlashParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(CROWNED_BEAM, CrownedBeamParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(CROWNED_WICKED, CrownedWickedParticle.Factory::new);

        ParticleFactoryRegistry.getInstance().register(COSMIC_CANDLE_FLAME, FlameParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ECHO_CANDLE_FLAME, FlameParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(SOUL_CANDLE_FLAME, FlameParticle.Factory::new);

        ParticleFactoryRegistry.getInstance().register(COSMIC_ENERGY, EnchantGlyphParticle.EnchantFactory::new);
        ParticleFactoryRegistry.getInstance().register(SOUL_ENERGY, EnchantGlyphParticle.EnchantFactory::new);

        ParticleFactoryRegistry.getInstance().register(CAULDRON_BUBBLE, CauldronBubbleParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(CAULDRON_SPARKLE, CauldronSparkleParticle.Factory::new);
    }


    private static void registerParticle(String name, DefaultParticleType type) {
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(Initializer.MODID, name), type);
    }
}
