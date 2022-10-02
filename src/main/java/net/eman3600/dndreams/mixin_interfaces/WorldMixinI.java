package net.eman3600.dndreams.mixin_interfaces;

import net.minecraft.entity.player.PlayerEntity;

import java.util.List;

public interface WorldMixinI {
    float highestTorment(List<? extends PlayerEntity> players);
}
