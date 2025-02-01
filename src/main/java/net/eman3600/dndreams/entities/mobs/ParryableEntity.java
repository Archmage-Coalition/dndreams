package net.eman3600.dndreams.entities.mobs;

import net.minecraft.entity.player.PlayerEntity;

public interface ParryableEntity {
    boolean canParry();
    float parryInterruptDamage();

    default void onParry(PlayerEntity player) {}
}
