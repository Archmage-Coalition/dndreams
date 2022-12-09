package net.eman3600.dndreams.items.consumable;

import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class DragonfruitItem extends Item {
    public DragonfruitItem(Settings settings) {
        super(settings);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return super.getMaxUseTime(stack) * 2;
    }
}
