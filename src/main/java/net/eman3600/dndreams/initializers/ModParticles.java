package net.eman3600.dndreams.initializers;

import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.particle.CrownedSlashParticle;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModParticles {
    public static final DefaultParticleType CROWNED_SLASH_PARTICLE = FabricParticleTypes.simple();
    public static final DefaultParticleType CROWNED_BEAM_PARTICLE = FabricParticleTypes.simple();
    public static final DefaultParticleType CROWNED_WICKED_PARTICLE = FabricParticleTypes.simple();

    public static void registerParticles() {
        registerParticle("crowned_slash", CROWNED_SLASH_PARTICLE);
        registerParticle("crowned_beam", CROWNED_BEAM_PARTICLE);
        registerParticle("crowned_wicked", CROWNED_WICKED_PARTICLE);
    }


    private static void registerParticle(String name, DefaultParticleType type) {
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(Initializer.MODID, name), type);
    }
}
