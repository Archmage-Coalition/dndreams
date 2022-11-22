package net.eman3600.dndreams.mixin_interfaces;

import net.minecraft.entity.player.PlayerEntity;

import java.util.List;

public interface WorldAccess {
    float highestTorment(List<? extends PlayerEntity> players);
}
