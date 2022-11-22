package net.eman3600.dndreams.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;

public class CauldronBubbleParticle extends SpriteBillboardParticle {
    public final SpriteProvider spriteProvider;

    protected CauldronBubbleParticle(ClientWorld level, double xCoord, double yCoord, double zCoord,
                                     SpriteProvider spriteSet, double xd, double yd, double zd) {
        super(level, xCoord, yCoord, zCoord, xd, yd, zd);

        this.x = 0;
        this.y = 0;
        this.z = 0;

        this.velocityX = 0;
        this.velocityY = 0.01;
        this.velocityZ = 0;

        this.velocityMultiplier = 0.7F;
        this.scale *= 0.15F;
        this.maxAge = 10;
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
    public void tick() {
        super.tick();

        this.setSpriteForAge(this.spriteProvider);
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider sprites;

        public Factory(SpriteProvider spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(DefaultParticleType particleType, ClientWorld level, double x, double y, double z,
                                       double dx, double dy, double dz) {
            return new CauldronBubbleParticle(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }
}
