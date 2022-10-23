package net.eman3600.dndreams.mixin_interfaces;

import net.minecraft.world.GameMode;

import javax.annotation.Nullable;

public interface PlayerEntityAccess {
    @Nullable
    GameMode getGameMode();
}
