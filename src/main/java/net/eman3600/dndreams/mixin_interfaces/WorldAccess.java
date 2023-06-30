package net.eman3600.dndreams.mixin_interfaces;

import net.minecraft.entity.player.PlayerEntity;

import java.util.List;

public interface WorldAccess {
    boolean isTrulyDay();

    boolean isTrulyNight();

    float lowestSanity(List<? extends PlayerEntity> players);
}
