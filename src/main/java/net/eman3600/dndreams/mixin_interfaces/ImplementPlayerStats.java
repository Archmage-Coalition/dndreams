package net.eman3600.dndreams.mixin_interfaces;

import net.minecraft.nbt.NbtCompound;

public interface ImplementPlayerStats {
    NbtCompound getMana();
    NbtCompound getTorment();
    NbtCompound getInitiated();
}
