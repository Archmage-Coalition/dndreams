package net.eman3600.dndreams.mixin_interfaces;

import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.item.ItemStack;

public interface TridentEntityAccess {

    void dndreams$setTridentStack(ItemStack stack);

    void dndreams$collideWithDrowned(DrownedEntity drowned);
}
