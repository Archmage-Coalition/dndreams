package net.eman3600.dndreams.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.util.math.Vector3d;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.Vec3d;

public class EnergyParticle extends SpriteBillboardParticle {
    public static final int MAX_AGE = 25;

    public EnergyParticle(ClientWorld level, double xCoord, double yCoord, double zCoord,
                             SpriteProvider spriteSet, double xd, double yd, double zd) {
        super(level, xCoord, yCoord, zCoord, xd, yd, zd);

        this.velocityMultiplier = 0.02F;
        this.x = xd;
        this.y = yd;
        this.z = zd;
        this.scale *= 0.75F;
        this.maxAge = MAX_AGE;
        this.setSpriteForAge(spriteSet);

        this.red = 1f;
        this.green = 1f;
        this.blue = 1f;
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




    public static Vec3d requiredVelocity(Vec3d start, Vec3d end) {
        Vec3d dir = start.relativize(end).normalize();

        double speed = start.distanceTo(end) * MAX_AGE / 20;

        return dir.multiply(speed);
    }

    public void setVelocity(Vec3d vec) {
        super.setVelocity(vec.x, vec.y, vec.z);
    }

    private Vec3d getPos() {
        return new Vec3d(x, y, z);
    }

    public void setPoints(Vec3d end) {
        setVelocity(requiredVelocity(getPos(), end));
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider sprites;

        public Factory(SpriteProvider spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(DefaultParticleType particleType, ClientWorld level, double x, double y, double z,
                                       double dx, double dy, double dz) {
            return new EnergyParticle(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }
}
