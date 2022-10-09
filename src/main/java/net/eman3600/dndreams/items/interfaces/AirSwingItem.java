package net.eman3600.dndreams.items.interfaces;

import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;

public interface AirSwingItem {
    void swingItem(ServerPlayerEntity user, Hand hand, ServerWorld world, ItemStack stack, boolean hit);
}
