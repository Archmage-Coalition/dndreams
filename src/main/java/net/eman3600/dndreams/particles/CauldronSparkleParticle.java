package net.eman3600.dndreams.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

public class CauldronSparkleParticle extends SpriteBillboardParticle {
    public final SpriteProvider spriteProvider;

    protected CauldronSparkleParticle(ClientWorld level, double xCoord, double yCoord, double zCoord,
                                      SpriteProvider spriteSet, double xd, double yd, double zd) {
        super(level, xCoord, yCoord, zCoord, xd, yd, zd);

        this.x = 0;
        this.y = 0;
        this.z = 0;

        this.velocityX = 0;
        this.velocityY = 0.01;
        this.velocityZ = 0;

        this.velocityMultiplier = 0.2F;
        this.scale *= 0.75F;
        this.maxAge = 30;
        this.setSpriteForAge(spriteSet);

        this.red = (float)xd;
        this.green = (float)yd;
        this.blue = (float)zd;

        this.spriteProvider = spriteSet;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void move(double dx, double dy, double dz) {
        this.setBoundingBox(this.getBoundingBox().offset(dx, dy, dz));
        this.repositionFromBoundingBox();
    }

    @Override
    public float getSize(float tickDelta) {
        float f = ((float)this.age + tickDelta) / (float)this.maxAge;
        return this.scale * (1.0f - f);
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider sprites;

        public Factory(SpriteProvider spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(DefaultParticleType particleType, ClientWorld level, double x, double y, double z,
                                       double dx, double dy, double dz) {
            return new CauldronSparkleParticle(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }
}
