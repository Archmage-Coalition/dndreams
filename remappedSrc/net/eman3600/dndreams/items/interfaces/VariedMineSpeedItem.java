package net.eman3600.dndreams.items.interfaces;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface VariedMineSpeedItem {
    float additionalModifiers(ItemStack stack, PlayerEntity entity, BlockState state, World world);
}
