package net.eman3600.dndreams.entities.projectiles;

public interface GravityProjectileEntity {
    float getGravity();
    default boolean penetratesBlocks() {
        return false;
    }
}
